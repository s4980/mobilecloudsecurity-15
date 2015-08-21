package org.magnum.mobilecloud.video.service;

import org.magnum.mobilecloud.video.model.AverageVideoRating;
import org.magnum.mobilecloud.video.model.Video;
import org.magnum.mobilecloud.video.model.VideoStatus;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

public interface VideoService {

    VideoStatus addVideo(Long id, InputStream inputStream, Principal principal) throws Exception;

    void getVideo(Long id, HttpServletResponse response) throws Exception;

    Video addVideoMetadata(Video video, Principal principal);

    Video getVideoMetadata(long id);

    Video updateVideoMetadata(long id, Video video);

    Collection<Video> getVideosMetadata();

    boolean deleteVideo(long id) throws IOException;

    void deleteVideos() throws IOException;

    AverageVideoRating rateVideo(long id, int rating, Principal principal);

    AverageVideoRating getVideoRating(long id);
}
