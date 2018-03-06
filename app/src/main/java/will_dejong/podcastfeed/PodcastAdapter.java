package will_dejong.podcastfeed;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by William on 3/5/2018.
 */

public class PodcastAdapter extends RecyclerView.Adapter<PodcastAdapter.ViewHolder> {

    private ArrayList<Podcast> podcasts;

    private int rowLayout;
    private Context mContext;
    WebView podcastView;

    public PodcastAdapter(ArrayList<Podcast> list, int rowLayout, Context context) {

        this.podcasts = list;
        this.rowLayout = rowLayout;
        this.mContext = context;
    }


    @Override
    public long getItemId(int item) {
        // TODO Auto-generated method stub
        return item;
    }

    public void clearData() {
        if (podcasts != null)
            podcasts.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(rowLayout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Podcast currentPodcast = podcasts.get(position);

        Locale.setDefault(Locale.getDefault());
        Date date = currentPodcast.getPubDate();
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf = new SimpleDateFormat("dd MMMM yyyy");
        final String pubDateString = sdf.format(date);

        viewHolder.title.setText(currentPodcast.getTitle());

        Picasso.with(mContext)
                .load(currentPodcast.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .centerCrop()
                .into(viewHolder.image);

        viewHolder.pubDate.setText(pubDateString);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                podcastView = new WebView(mContext);

                podcastView.getSettings().setLoadWithOverviewMode(true);

                String title = podcasts.get(position).getTitle();
                String content = podcasts.get(position).getContent();

                podcastView.getSettings().setJavaScriptEnabled(true);
                podcastView.setHorizontalScrollBarEnabled(false);
                podcastView.setWebChromeClient(new WebChromeClient());
                podcastView.loadDataWithBaseURL(null, "<style>img{display: inline; height: auto; max-width: 100%;} " +

                        "</style>\n" + "<style>iframe{ height: auto; width: auto;}" + "</style>\n" + content, null, "utf-8", null);

                android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext).create();
                alertDialog.setTitle(title);
                alertDialog.setView(podcastView);
                alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
    }

    @Override
    public int getItemCount() {

        return podcasts == null ? 0 : podcasts.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView pubDate;
        ImageView image;

        public ViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            pubDate = (TextView) itemView.findViewById(R.id.pubDate);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
