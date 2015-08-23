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

    private final String clientId = "mobile";
    private String username;
    private String password;
    private String server;

    private static RestAdapterFactory INSTANCE;

    private RestAdapterFactory() {
    }

    public RestAdapterFactory(String server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
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

    public synchronized <T> T construct(SecurityLevel securityLevel, Class<T> tClass) {
        switch (securityLevel) {
            case HTTP:
                return unsecuredAdapter(tClass);
            case HTTPS:
                return securedAdapter(tClass);
            default:
                return unsecuredAdapter(tClass);
        }
    }

    private <T> T securedAdapter(Class<T> tClass) {
        return new SecuredRestBuilder()
                .setLoginEndpoint(server + VideoServiceProxy.TOKEN_PATH)
                .setEndpoint(server)
                .setUsername(username)
                .setPassword(password)
                .setClientId(clientId)
                .setClient(new OkClient(UnsafeHttpsClient.getUnsafeOkHttpClient()))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(tClass);
    }

    private <T> T unsecuredAdapter(Class<T> tClass) {
        return new RestAdapter
                .Builder()
                .setEndpoint(server)
                .build()
                .create(tClass);
    }
}
