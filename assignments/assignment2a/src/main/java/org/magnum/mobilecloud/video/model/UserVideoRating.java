package org.magnum.mobilecloud.video.model;

// You might want to annotate this with Jpa annotations, add an id field,
// and store it in the database...
//
// There are also plenty of other solutions that do not require
// persisting instances of this...

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "VIDEO_RATING")
@SequenceGenerator(name = "VIDEO_RATING_SEQUENCE",
        sequenceName = "VIDEO_RATING_SEQUENCE",
        allocationSize = 1,
        initialValue = 0)
public class UserVideoRating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "VIDEO_RATING_SEQUENCE")
    @Column(name = "VIDEO_RATING_ID")
    private long id;

    @ManyToOne(optional = false,
            targetEntity = Video.class)
    @JoinColumn(name = "video",
            referencedColumnName = "VIDEO_METADATA_ID")
    private Video video;
    @Column
    private long videoIndex;
    @Column
    private double rating;
    @Column
    private String user;

    public UserVideoRating() {
    }

    public UserVideoRating(long videoIndex, double rating, String user) {
        super();
        this.videoIndex = videoIndex;
        this.rating = rating;
        this.user = user;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public long getVideoIndex() {
        return videoIndex;
    }

    public void setVideoIndex(long videoIndex) {
        this.videoIndex = videoIndex;
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

    public long getDbId() {
        return id;
    }

    public void setDbId(long id) {
        this.id = id;
    }
}
