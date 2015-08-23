package video.mooc.coursera.videodownloader.model.services;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
