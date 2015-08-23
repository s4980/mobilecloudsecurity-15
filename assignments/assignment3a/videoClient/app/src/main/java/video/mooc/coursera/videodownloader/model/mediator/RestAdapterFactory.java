package video.mooc.coursera.videodownloader.model.mediator;

import android.content.Context;
import android.content.Intent;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import video.mooc.coursera.videodownloader.api.proxy.SecuredRestBuilder;
import video.mooc.coursera.videodownloader.api.proxy.UnsafeHttpsClient;
import video.mooc.coursera.videodownloader.api.proxy.VideoServiceProxy;
import video.mooc.coursera.videodownloader.view.LoginScreenActivity;

/**
 * Created by MZ on 22/08/2015.
 */
public class RestAdapterFactory {

    private static final String CLIENT_ID = "mobile";
    private static String USERNAME;
    private static String PASSWORD;
    private static String SERVER;

    private static RestAdapterFactory INSTANCE;

    private RestAdapterFactory() {
    }

    public RestAdapterFactory(String server, String userName, String password) {
        this.SERVER = server;
        this.USERNAME = userName;
        this.PASSWORD = password;
    }

    /**
     * Returns existing instance or empty RestAdapterFactory
     *
     * @return
     */
    public static synchronized RestAdapterFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RestAdapterFactory();
        }
        return INSTANCE;
    }

    /**
     * Creates RestAdapterFactory with passed credentials
     *
     * @param server
     * @param userName
     * @param password
     * @return
     */
    public static synchronized RestAdapterFactory getInstance(String server, String userName, String password) {
        INSTANCE = new RestAdapterFactory(server, userName, password);
        return INSTANCE;
    }

    public static synchronized RestAdapterFactory getOrShowLogin(Context ctx) {
        if (INSTANCE != null) {
            return INSTANCE;
        } else {
            Intent i = new Intent(ctx, LoginScreenActivity.class);
            ctx.startActivity(i);
            return null;
        }
    }

    public synchronized VideoServiceProxy construct(SecurityLevel securityLevel) {
        switch (securityLevel) {
            case HTTP:
                return unsecuredAdapter();
            case HTTPS:
                return securedAdapter();
            default:
                return unsecuredAdapter();
        }
    }

    private VideoServiceProxy securedAdapter() {
        return new SecuredRestBuilder()
                .setLoginEndpoint(SERVER + VideoServiceProxy.TOKEN_PATH)
                .setEndpoint(SERVER)
                .setUsername(USERNAME)
                .setPassword(PASSWORD)
                .setClientId(CLIENT_ID)
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(VideoServiceProxy.class);
    }

    private VideoServiceProxy unsecuredAdapter() {
        return new RestAdapter
                .Builder()
                .setEndpoint(SERVER)
                .build()
                .create(VideoServiceProxy.class);
    }
}
