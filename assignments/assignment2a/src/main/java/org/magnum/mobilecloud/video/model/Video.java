package org.magnum.mobilecloud.video.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * A simple object to represent a video and its URL for viewing.
 * <p/>
 * You must annotate this object to make it a JPA entity.
 * <p/>
 * <p/>
 * Feel free to modify this with whatever other metadata that you want, such as
 * the
 *
 * @author jules, mitchell
 */
@Entity
@Table(name = "VIDEO_METADATA")
@SequenceGenerator(name = "VIDEO_METADATA_SEQUENCE",
        sequenceName = "VIDEO_METADATA_SEQUENCE",
        allocationSize = 1,
        initialValue = 0)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "VIDEO_METADATA_SEQUENCE")
    @Column(name = "VIDEO_METADATA_ID")
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "videoIndex",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private Collection<UserVideoRating> rating;

    @Column
    private long duration;
    @Column
    private String title;
    @Column
    private String url;
    @Column
    private String location;
    @Column
    private String subject;
    @Column
    private String contentType;

    // We don't want to bother unmarshalling or marshalling
    // any owner data in the JSON. Why? We definitely don't
    // want the client trying to tell us who the owner is.
    // We also might want to keep the owner secret.
    @JsonIgnore
    private String owner;

    public Video() {
    }

    public Video(String owner, String name, String url, long duration,
                 long likes, Set<String> likedBy) {
        super();
        this.owner = owner;
        this.title = name;
        this.url = url;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Collection<UserVideoRating> getRating() {
        return rating;
    }

    public void setRating(Collection<UserVideoRating> rating) {
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Two Videos will generate the same hashcode if they have exactly the same
     * values for their name, url, and duration.
     */
    @Override
    public int hashCode() {
        // Google Guava provides great utilities for hashing
        return Objects.hashCode(title, url, duration, owner);
    }

    /**
     * Two Videos are considered equal if they have exactly the same values for
     * their name, url, and duration.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Video) {
            Video other = (Video) obj;
            // Google Guava provides great utilities for equals too!
            return Objects.equal(title, other.title)
                    && Objects.equal(url, other.url)
                    && Objects.equal(owner, other.owner)
                    && duration == other.duration;
        } else {
            return false;
        }
    }

}
