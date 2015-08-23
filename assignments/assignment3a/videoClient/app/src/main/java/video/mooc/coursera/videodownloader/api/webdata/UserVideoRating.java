package video.mooc.coursera.videodownloader.api.webdata;

// You might want to annotate this with Jpa annotations, add an id field,
// and store it in the database...
//
// There are also plenty of other solutions that do not require
// persisting instances of this...
public class UserVideoRating {

    private long videoId;

    private double rating;

    private String user;

    private Video video;

    public UserVideoRating() {
    }

    public UserVideoRating(long videoId, double rating, String user) {
        super();
        this.videoId = videoId;
        this.rating = rating;
        this.user = user;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
