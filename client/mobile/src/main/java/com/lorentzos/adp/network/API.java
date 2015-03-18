package com.lorentzos.adp.network;

import android.util.Log;

import com.lorentzos.adp.Config;
import com.lorentzos.adp.model.BuildsList;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;
import rx.Observable;
import rx.plugins.RxJavaErrorHandler;
import rx.plugins.RxJavaPlugins;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.network
 * Use with caution dinosaurs might appear!
 */
public class API {

    private static API instance;
    private final Endpoints endpoints;
    private final String token;
    private final String baseUrl;

    /**
     * Returns the instance of this singleton.
     *
     * @param baseURL the base url of the servers
     * @param token the authentication token
     */
    public static API getInstance(String baseURL, String token) {
        if ( instance == null
        || ( !instance.token.equals(token) || !instance.baseUrl.equals(baseURL) )
        ){
            instance = new API(baseURL, token);
        }
        return instance;
    }

    private API(String baseUrl, String token) {
        this.token = token;
        this.baseUrl = baseUrl;
        RestAdapter restAdapter = buildRestAdapter(baseUrl, token);
        endpoints = restAdapter.create(Endpoints.class);

        if(RxJavaPlugins.getInstance().getErrorHandler() == null)
            RxJavaPlugins.getInstance().registerErrorHandler(new RxJavaErrorHandler() {
                @Override
                public void handleError(Throwable e) {

                    Log.w(RxJavaErrorHandler.class.getSimpleName(), e);
                    super.handleError(e);
                }
            });
    }

    private RestAdapter buildRestAdapter(String baseUrl, String token) {
        return new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(getHttpClient())
                .setRequestInterceptor(request -> request.addHeader("Api-Token", token))
                .build();
    }

    private Client getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.setConnectTimeout(Config.API_HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(Config.API_HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        return new OkClient(httpClient);
    }


    public Observable<BuildsList> getVersions() {
        return endpoints.versionList(token);
    }

    public Observable<Response> getAPK(String id){
        return endpoints.downloadAPK(id);
    }

}
