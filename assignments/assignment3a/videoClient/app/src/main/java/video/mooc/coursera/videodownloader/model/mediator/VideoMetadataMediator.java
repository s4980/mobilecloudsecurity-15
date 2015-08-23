package video.mooc.coursera.videodownloader.model.mediator;

import java.util.ArrayList;
import java.util.List;

import video.mooc.coursera.videodownloader.api.proxy.VideoServiceProxy;
import video.mooc.coursera.videodownloader.api.webdata.AverageVideoRating;
import video.mooc.coursera.videodownloader.api.webdata.Video;

/**
 * Mediates communication between the Video Service and the app
 * The methods in this class block, so
 * they should be called from a background thread (e.g., via an
 * AsyncTask).
 */
public class VideoMetadataMediator {

    /**
     * Defines methods that communicate with the Video Service.
     */
    private VideoServiceProxy mVideoServiceProxy;

    /**
     * Constructor that initializes the VideoDataMediator.
     */
    public VideoMetadataMediator() {
        // Initialize the VideoServiceProxy.
        mVideoServiceProxy = RestAdapterFactory.getInstance().construct(SecurityLevel.HTTPS, VideoServiceProxy.class);
    }

    public AverageVideoRating rateVideo(long id, float rating) {

        AverageVideoRating averageVideoRating = mVideoServiceProxy.rateVideo(id, Math.round(rating));

        return averageVideoRating != null ? averageVideoRating : null;
    }

    public AverageVideoRating getVideoRating(long id){
        AverageVideoRating videoRating = mVideoServiceProxy.getVideoRating(id);

        return videoRating != null ? videoRating : null;
    }

    /**
     * Get the List of Videos from Video Service.
     *
     * @return the List of Videos from Server or null if there is
     *         failure in getting the Videos.
     */
    public List<Video> getVideoList() {
        return (ArrayList<Video>) mVideoServiceProxy.getVideoList();
    }

    public Video getVideoMetadata(long id) {
        return mVideoServiceProxy.getVideoById(id);
    }
}
