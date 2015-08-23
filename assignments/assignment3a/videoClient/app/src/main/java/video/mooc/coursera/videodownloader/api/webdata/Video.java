package video.mooc.coursera.videodownloader.api.webdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class Video {

    private long id;
    private long duration;
    private String title;
    private String location;
    private String subject;
    private String contentType;
//    @JsonIgnore
//    private double averageRating = 0;
//    @JsonIgnore
//    private int totalRatings = 0;

    // We don't want to bother unmarshalling or marshalling
    // any owner data in the JSON. Why? We definitely don't
    // want the client trying to tell us who the owner is.
    // We also might want to keep the owner secret.
    @JsonIgnore
    private String owner;
    @JsonIgnore
    private String dataUrl;
    @JsonIgnore
    private Collection<UserVideoRating> rating;

    public Video() {
    }

    public Video(String owner, String name, String dataUrl, long duration,
                 long likes, Set<String> likedBy) {
        super();
        this.owner = owner;
        this.title = name;
        this.dataUrl = dataUrl;
        this.duration = duration;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @JsonProperty
    public String getDataUrl() {
        return dataUrl;
    }

    @JsonIgnore
    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    @JsonIgnore
    public double getAverageRating() {
        return 0;
    }

//    public void setAverageRating(float averageRating) {
//        this.averageRating = averageRating;
//    }
@JsonIgnore
    public int getTotalRatings() {
        return 0;
    }
    @JsonIgnore
    public void setTotalRatings(int totalRatings) {
        //this.totalRatings = totalRatings;
    }

    public Collection<UserVideoRating> getRating() {
        return rating;
    }
    @JsonIgnore
    public void setRating(Collection<UserVideoRating> rating) {
        this.rating = rating;
    }

//    public void addRating(float numberOfStars) {
//        setAverageRating(getAverageRating() + numberOfStars);
//        setTotalRatings(getTotalRatings() + 1);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDataUrl(), getDuration(), getOwner());
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Video)
                && Objects.equals(getTitle(), ((Video) obj).getTitle())
                && Objects.equals(getOwner(), ((Video) obj).getOwner())
                && Objects.equals(getDataUrl(), ((Video) obj).getDataUrl())
                && getDuration() == ((Video) obj).getDuration();
    }
}
