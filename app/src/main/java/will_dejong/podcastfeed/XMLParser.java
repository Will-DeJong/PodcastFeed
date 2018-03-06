package will_dejong.podcastfeed;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;

public class XMLParser extends Observable {

    private ArrayList<Podcast> podcasts;
    private Podcast currentPodcast;

    public XMLParser() {
        podcasts = new ArrayList<>();
        currentPodcast = new Podcast();
    }

    public void parseXML(String xml) throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(false);
        XmlPullParser xmlPullParser = factory.newPullParser();

        xmlPullParser.setInput(new StringReader(xml));
        boolean insideItem = false;
        int eventType = xmlPullParser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {

                if (xmlPullParser.getName().equalsIgnoreCase("item")) {

                    insideItem = true;

                } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {
                    if (insideItem) {
                        String title = xmlPullParser.nextText();
                        currentPodcast.setTitle(title);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {
                    Log.d("url", "test");
                    if (insideItem) {
                        String htmlData = xmlPullParser.nextText();
                        Document doc = Jsoup.parse(htmlData);
                        try {
                            String pic = doc.getAllElements().attr("src");

                            Log.d("url", pic);
                            currentPodcast.setImage(pic);
                        } catch (NullPointerException e) {
                            currentPodcast.setImage(null);
                        }
                        currentPodcast.setContent(htmlData);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {

                    if (insideItem) {
                        String description = xmlPullParser.nextText();
                        currentPodcast.setDescription(description);

                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {
                    @SuppressWarnings("deprecation")
                    Date pubDate = new Date(xmlPullParser.nextText());
                    currentPodcast.setPubDate(pubDate);
                }

            } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
                podcasts.add(currentPodcast);
                currentPodcast = new Podcast();
            }
            eventType = xmlPullParser.next();
        }
        triggerObserver();
    }


    private void triggerObserver() {
        setChanged();
        notifyObservers(podcasts);
    }
}