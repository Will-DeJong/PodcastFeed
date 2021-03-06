package will_dejong.podcastfeed;

/**
 * Created by William on 3/5/2018.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Parser extends AsyncTask<String, Void, String> implements Observer {

    private XMLParser xmlParser;
    private static ArrayList<Podcast> podcasts = new ArrayList<>();

    private OnTaskCompleted onComplete;

    public Parser() {

        xmlParser = new XMLParser();
        xmlParser.addObserver(this);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(ArrayList<Podcast> list);

        void onError();
    }

    public void onFinish(OnTaskCompleted onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    protected String doInBackground(String... ulr) {

        Response response = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(ulr[0])
                .build();

        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful())
                return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            onComplete.onError();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        if (result != null) {
            try {
                xmlParser.parseXML(result);
                Log.i("RSS Parser ", "RSS parsed correctly!");
            } catch (Exception e) {
                e.printStackTrace();
                onComplete.onError();
            }
        } else
            onComplete.onError();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable observable, Object data) {
        podcasts = (ArrayList<Podcast>) data;
        onComplete.onTaskCompleted(podcasts);
    }

}

