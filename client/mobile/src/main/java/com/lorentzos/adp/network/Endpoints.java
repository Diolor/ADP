package com.lorentzos.adp.network;

import com.lorentzos.adp.model.BuildsList;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by dionysis_lorentzos on 7/12/14
 * for package com.lorentzos.adp.network
 * Use with caution dinosaurs might appear!
 */
public interface Endpoints {

    @GET("/list")
    public Observable<BuildsList> versionList(@Query("token") String token);

    @GET("/download/{id}")
    public Observable<Response> downloadAPK (@Path("id") String id);
}
