package video.mooc.coursera.videodownloader.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collection;
import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import video.mooc.coursera.videodownloader.R;
import video.mooc.coursera.videodownloader.api.proxy.VideoServiceProxy;
import video.mooc.coursera.videodownloader.api.webdata.Video;
import video.mooc.coursera.videodownloader.model.mediator.RestAdapterFactory;
import video.mooc.coursera.videodownloader.model.mediator.SecurityLevel;
import video.mooc.coursera.videodownloader.model.services.CallableTask;
import video.mooc.coursera.videodownloader.model.services.TaskCallback;

/**
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * <p/>
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 */
public class LoginScreenActivity extends Activity {

    @InjectView(R.id.userName)
    protected EditText userName_;

    @InjectView(R.id.password)
    protected EditText password_;

    @InjectView(R.id.server)
    protected EditText server_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.loginButton)
    public void login() {
        String user = userName_.getText().toString();
        String pass = password_.getText().toString();
        String server = server_.getText().toString();

        final VideoServiceProxy svc = RestAdapterFactory.getInstance(server, user, pass)
                                                        .construct(SecurityLevel.HTTPS, VideoServiceProxy.class);

        CallableTask.invoke(new Callable<Collection<Video>>() {

            @Override
            public Collection<Video> call() throws Exception {
                return svc.getVideoList();
            }
        }, new TaskCallback<Collection<Video>>() {

            @Override
            public void success(Collection<Video> result) {
                // OAuth 2.0 grant was successful and web
                // can talk to the server, open up the video listing
                startActivity(new Intent(
                        LoginScreenActivity.this,
                        VideoListActivity.class));
            }

            @Override
            public void error(Exception e) {
                Log.e(LoginScreenActivity.class.getName(), "Error logging in via OAuth.", e);

                Toast.makeText(
                        LoginScreenActivity.this,
                        "Login failed, check your Internet connection and credentials.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
