package will_dejong.podcastfeed;
import java.util.Date;

public class Podcast {

    private String title;
    private Date pubDate;
    private String description;
    private String content;
    private String image;

    public String getTitle() {
        return title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

}