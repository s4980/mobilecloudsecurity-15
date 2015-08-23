package org.magnum.mobilecloud.video.service;

import org.magnum.mobilecloud.video.controller.VideoFileManager;
import org.magnum.mobilecloud.video.model.AverageVideoRating;
import org.magnum.mobilecloud.video.model.UserVideoRating;
import org.magnum.mobilecloud.video.model.Video;
import org.magnum.mobilecloud.video.model.VideoStatus;
import org.magnum.mobilecloud.video.persistence.JpaUserVideoRatingRepository;
import org.magnum.mobilecloud.video.persistence.JpaVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.DATA_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;

/**
 * Created by MZ on 13/08/2015.
 */

@Service
public class VideoServiceImpl implements VideoService {
    public static final int _404 = HttpStatus.NOT_FOUND.value();

    // The manager of video data
    @Autowired
    private JpaVideoRepository metadataRepository;

    @Autowired
    private JpaUserVideoRatingRepository userVideoRatingRepository;

    @Autowired
    private VideoFileManager videoRepository;

    @Override
    public Collection<Video> getVideosMetadata() {
        // return Lists.newArrayList(metadataRepository.findAll());
        return (Collection<Video>) metadataRepository.findAll();
    }

    @Override
    public Video addVideoMetadata(Video video, Principal principal) {

        if (!metadataRepository.exists(video.getId())) {
            // The "owner" member variable of the Video must be set
            // to the name of the currently authenticated Principal
            initializeVideo(video, principal);

            return metadataRepository.save(video);
        }
        // If a Video already exists, it should not be overwritten
        // unless the name of the authenticated Principal matches
        // the name of the owner member variable of the Video
        else if (metadataRepository.exists(video.getId()) && metadataRepository.findOne(video.getId()).getOwner() == video.getOwner()) {
            return metadataRepository.save(video);
        }
        // If a Video already exists and the authenticated Principal
        // does not match the name of the owner member variable of the Video
        else {
            return null;
        }
    }

    @Override
    public Video getVideoMetadata(long id) {

        return metadataRepository.findOne(id);
    }

    @Override
    public Video updateVideoMetadata(long id, Video video) {

        return metadataRepository.save(video);
    }

    @Override
    public boolean deleteVideo(long id) throws IOException {

        Video video = metadataRepository.findOne(id);

        if (video != null) {
            videoRepository.deleteVideoData(video);
            metadataRepository.delete(id);

            return true;
        }

        return false;
    }

    @Override
    public void deleteVideos() throws IOException {
        for (Video video : metadataRepository.findAll()) {
            videoRepository.deleteVideoData(video);
        }

        metadataRepository.deleteAll();
    }

    @Override
    public AverageVideoRating rateVideo(long id, int rating, Principal principal) {
        if (!metadataRepository.exists(id)) {
            return null;
        }

        Collection<UserVideoRating> userVideoRatingRepositoryByUser = userVideoRatingRepository.findByUser(principal.getName());
        // If user didn't rate the video yet we are creating new UserVideoRating object an save it to Video object
        if (readyToRate(userVideoRatingRepositoryByUser, id)) {
            Video video = metadataRepository.findOne(id);
            UserVideoRating userVideoRating = new UserVideoRating(id, rating, principal.getName());
            userVideoRating.setVideo(video);
            video.getRating().add(userVideoRating);
            metadataRepository.save(video);
        } else {
            // If user already rated this video we are updating his rating
            for (UserVideoRating uVR : userVideoRatingRepositoryByUser) {
                if (uVR.getVideoIndex() == id) {
                    uVR.setRating(rating);
                    userVideoRatingRepository.save(uVR);
                }
            }
        }

        return calculateAverageVideoRating(id);
    }

    private boolean readyToRate(Collection<UserVideoRating> userVideoRatingRepositoryByUser, long videoId) {

        if (userVideoRatingRepositoryByUser.isEmpty()) {
            return true;
        }

        for (UserVideoRating videoRating : userVideoRatingRepositoryByUser) {
            if (videoRating.getVideoIndex() == videoId)
            {
                return false;
            }
        }

        return true;
    }

    private AverageVideoRating calculateAverageVideoRating(long id) {

        if (!metadataRepository.exists(id)) {
            return null;
        }

        Collection<UserVideoRating> videoRatings = metadataRepository.findOne(id).getRating();
        double totalRating = 0;
        for (UserVideoRating uVR : videoRatings) {
            totalRating += uVR.getRating();
        }

        return new AverageVideoRating(totalRating / videoRatings.size(), id, videoRatings.size());
    }

    @Override
    public AverageVideoRating getVideoRating(long id) {
        return calculateAverageVideoRating(id);
    }

    @Override
    public VideoStatus addVideo(Long id, InputStream inputStream, Principal principal) throws Exception {

        VideoStatus videoStatus;
        Video video = metadataRepository.findOne(id);

        if (video != null) {
            if (video.getOwner() == principal.getName()) {
                videoRepository.saveVideoData(video, inputStream);
                videoStatus = new VideoStatus(VideoStatus.VideoState.READY);
            } else {
                videoStatus = new VideoStatus(VideoStatus.VideoState.FORBIDDEN);
            }
        } else {
            videoStatus = new VideoStatus(VideoStatus.VideoState.ERROR);
        }
        return videoStatus;
    }

    @Override
    public void getVideo(Long id, HttpServletResponse response) throws IOException {

        Video video = metadataRepository.findOne(id);
        if (video == null) {
            response.sendError(_404, "Video not found");

            return;
        }

        response.setContentType(video.getContentType());

        if (videoRepository.hasVideoData(video)) {
            videoRepository.copyVideoData(video, response.getOutputStream());
        }
    }

    /*
     * This method assigns a new ID and a url to a new video.
     * It returns the ID of the new video or Null if the
     * video was already in the collection.
     */
    private void initializeVideo(Video v, Principal principal) {
        v.setOwner(principal.getName());
    }

    private String getDataUrl(Long id) {
        return String.format("%s%s/%d/%s", getUrlBaseForLocalServer(), VIDEO_SVC_PATH, id, DATA_PARAMETER);
    }

    /*
     * This method returns the url authority for the current request
     * prepended by the http scheme.
     */
    private String getUrlBaseForLocalServer() {
        HttpServletRequest request = getRequest();
        String base = String.format("http://%s%s", request.getServerName(), (request.getServerPort() != 80) ? ":" + request.getServerPort() : "");

        return base;
    }

    private HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
