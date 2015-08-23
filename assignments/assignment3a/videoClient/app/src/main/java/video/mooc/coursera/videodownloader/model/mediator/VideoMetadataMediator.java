package video.mooc.coursera.videodownloader.model.mediator;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import video.mooc.coursera.videodownloader.api.proxy.VideoServiceProxy;
import video.mooc.coursera.videodownloader.api.webdata.AverageVideoRating;
import video.mooc.coursera.videodownloader.api.webdata.Video;

import static video.mooc.coursera.videodownloader.api.constants.Constants.HTTPS_SERVER_URL;

/**
 * Mediates communication between the Video Service and the app
 * The methods in this class block, so
 * they should be called from a background thread (e.g., via an
 * AsyncTask).
 */
public class VideoMetadataMediator {

    /**
     * Status code to indicate that video is successfully
     * rated.
     */
    public static final String STATUS_RATING_SUCCESSFUL = "Thanks for rating ;)";

    /**
     * Status code to indicate that video rating failed.
     */
    public static final String STATUS_RATING_ERROR = "Ups, couldn't rated this ;(";

    /**
     * Defines methods that communicate with the Video Service.
     */
    private VideoServiceProxy mVideoServiceProxy;

    /**
     * Constructor that initializes the VideoDataMediator.
     */
    public VideoMetadataMediator() {
        // Initialize the VideoServiceProxy.
//        mVideoServiceProxy = new RestAdapter
//                .Builder()
//                .setEndpoint(HTTPS_SERVER_URL)
//                .build()
//                .create(VideoServiceProxy.class);
//        mVideoServiceProxy = (VideoServiceProxy) RestAdapterFactory.getInstance().construct(SecurityLevel.HTTP, VideoServiceProxy.class);
        mVideoServiceProxy = RestAdapterFactory.getInstance().construct(SecurityLevel.HTTPS);
    }

//    public Video rateVideo(Context context, long id, float rating) {
//
//        Video video = mVideoServiceProxy.getVideoById(id);
//        if (video != null) {
//            video.addRating(rating);
//
//            Video updateVideo = mVideoServiceProxy.updateVideo(id, video);
//
//            if (updateVideo != null) {
//                // Video successfully uploaded.
//                return video;
//            }
//        }
//
//        // Error occured while raing the video.
//        return null;
//    }

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
