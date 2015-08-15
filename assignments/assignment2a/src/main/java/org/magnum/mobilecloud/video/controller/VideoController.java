package org.magnum.mobilecloud.video.controller;

import org.magnum.mobilecloud.video.model.AverageVideoRating;
import org.magnum.mobilecloud.video.model.Video;
import org.magnum.mobilecloud.video.model.VideoStatus;
import org.magnum.mobilecloud.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.DATA_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.ID_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.RATING_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_DATA_PATH;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by MZ on 13/08/2015.
 */

@Controller
public class VideoController {
    public static final String VIDEO_ID_PATH = VIDEO_SVC_PATH + "/{id}";

    @Autowired
    private VideoService videoService;

    /**
     * This method returns a collection of all video
     * meta data stored by the service.
     *
     * @return
     */
    @RequestMapping(value = VIDEO_SVC_PATH,
            method = GET)
    public
    @ResponseBody
    Collection<Video> getVideoList() {
        return videoService.getVideosMetadata();
    }

    /**
     * This method grabs the meta data for a new Video from the body, storing it in memory.
     * It returns a unique ID to the client for use when uploading the actual video.
     *
     * @param video
     * @return
     */
    @RequestMapping(value = VIDEO_SVC_PATH,
            method = POST)
    public
    @ResponseBody
    Video addVideo(@RequestBody Video video,
                   Principal principal,
                   HttpServletResponse response) {
        Video v = videoService.addVideoMetadata(video, principal);
        if (v == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

        return v;
    }

//    /**
//     * This method grabs the meta data for the video with the id for
//     * the @param v, updating its representation in memory if found.
//     * It returns the updated video.
//     *
//     * @param video
//     * @return
//     */
//    @RequestMapping(value = VIDEO_ID_PATH,
//            method = POST)
//    public
//    @ResponseBody
//    Video updateVideoMetadata(@PathVariable(ID_PARAMETER) long id,
//                              @RequestBody Video video,
//                              HttpServletResponse response) {
//        Video v = null;
//        if (id == video.getId()) {
//            v = videoService.updateVideoMetadata(id, video);
//        }
//
//        if (v == null) {
//            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//
//        return v;
//    }

    /**
     * Returns the video meta data specified by the @param id if found.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = VIDEO_ID_PATH,
            method = GET)
    public
    @ResponseBody
    Video getVideoById(@PathVariable(ID_PARAMETER) long id,
                       HttpServletResponse response) {
        return videoService.getVideoMetadata(id);
    }


    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}/rating",
            method = GET)
    public
    @ResponseBody
    AverageVideoRating getVideoRating(@PathVariable(ID_PARAMETER) long id) {
        return videoService.getVideoRating(id);
    }

    @RequestMapping(value = VIDEO_SVC_PATH + "/{id}/rating/{rating}",
            method = POST)
    public
    @ResponseBody
    AverageVideoRating rateVideo(@PathVariable(ID_PARAMETER) Long id,
                                 @PathVariable(RATING_PARAMETER) int rating,
                                 HttpServletResponse response,
                                 Principal principal) throws Exception {

        AverageVideoRating videoRating = videoService.rateVideo(id, rating, principal);

        if (videoRating == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Video not found");
        } else {
            response.sendError(HttpStatus.OK.value(), "Video rated");
        }

        return videoRating;
    }

    /**
     * This method grabs the encoded video from the multi part body, writing it to disk.
     * It returns the VideoStatus to indicate success or 400 for failure.
     *
     * @param id
     * @return
     */
    @RequestMapping(value = VIDEO_DATA_PATH,
            method = POST)
    public
    @ResponseBody
    VideoStatus setVideoData(@PathVariable(ID_PARAMETER) long id,
                             @RequestParam(DATA_PARAMETER) MultipartFile videoFile,
                             HttpServletResponse response,
                             Principal principal) throws Exception {

        final VideoStatus videoStatus = videoService.addVideo(id, videoFile.getInputStream(), principal);

        if (videoStatus.getState() == VideoStatus.VideoState.ERROR) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Video not found");
        }
        if (videoStatus.getState() == VideoStatus.VideoState.FORBIDDEN) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Video access forbidden");
        }

        return videoStatus;
    }

    @RequestMapping(method = GET,
            value = VIDEO_DATA_PATH)
    public void getVideoData(@PathVariable(ID_PARAMETER) Long id,
                             HttpServletResponse response) throws Exception {
        videoService.getVideo(id, response);
    }

    /**
     * This method deletes the video data and the video meta data
     * for the given @param id if found.
     *
     * @param id
     */
    @RequestMapping(value = VIDEO_ID_PATH,
            method = DELETE)
    public void deleteVideo(@PathVariable(ID_PARAMETER) long id,
                            HttpServletResponse response) throws IOException {
        videoService.deleteVideo(id);
    }

    /**
     * This method deletes all the video data and the video meta data
     * stored by the service.
     */
    @RequestMapping(value = VIDEO_SVC_PATH,
            method = DELETE)
    public void deleteVideos() throws IOException {
        videoService.deleteVideos();
    }

}
