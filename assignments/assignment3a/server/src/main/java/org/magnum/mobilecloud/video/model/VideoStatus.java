/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mobilecloud.video.model;

public class VideoStatus {

    private VideoState state;

    public VideoStatus(VideoState state) {
        super();
        this.state = state;
    }

    public VideoState getState() {
        return state;
    }

    public void setState(VideoState state) {
        this.state = state;
    }

    public enum VideoState {
        READY, PROCESSING, ERROR, FORBIDDEN
    }

}
