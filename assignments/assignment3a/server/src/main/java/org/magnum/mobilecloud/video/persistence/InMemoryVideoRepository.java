package org.magnum.mobilecloud.video.persistence;

import org.magnum.mobilecloud.video.model.Video;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;

import static org.magnum.mobilecloud.video.client.VideoSvcApi.DATA_PARAMETER;
import static org.magnum.mobilecloud.video.client.VideoSvcApi.VIDEO_SVC_PATH;

/**
 * Created by MZ on 13/08/2015.
 */
public class InMemoryVideoRepository implements VideoRepository {
    private AtomicLong currentId = new AtomicLong(0L);
    private ConcurrentMap<Long, Video> videos = new ConcurrentHashMap<>();

    @Override
    public Video save(Video video, Principal principal) {
        Long id = initializeVideo(video, principal);
        if (id == null) {
            return null;
        }

        videos.put(id, video);

        return video;
    }

    @Override
    public Video update(long id, Video video) {

        videos.replace(id, video);

        return videos.get(id);
    }

    @Override
    public Video findOne(long id) {
        return videos.get(id);
    }

    @Override
    public Iterable<Video> findAll() {
        return videos.values();
    }

    @Override
    public boolean remove(long id) {
        final Video remove = videos.remove(id);

        return remove != null;
    }

    @Override
    public void clearAll() {
        videos.clear();
    }

    /*
     * This method assigns a new ID and a url to a new video.
     * It returns the ID of the new video or Null if the
     * video was already in the collection.
     */
    private Long initializeVideo(Video v, Principal principal) {
        if (!videos.containsKey(v.getId()) || videos.get(v.getId()).getOwner() == v.getOwner()) {
            v.setId(checkAndSetId(v.getId()));
            v.setUrl(getDataUrl(v.getId()));
            v.setOwner(principal.getName());
        } else {
            return null;
        }

        return v.getId();
    }

    private long checkAndSetId(long id) {
        if (id == 0) {
            return currentId.incrementAndGet();
        } else {
            return id;
        }
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

