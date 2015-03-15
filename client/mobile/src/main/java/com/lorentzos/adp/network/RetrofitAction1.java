package com.lorentzos.adp.network;

import com.lorentzos.adp.model.RestError;

import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.functions.Action1;

/**
 * Created by dionysis_lorentzos on 15/3/15
 * for package com.lorentzos.adp.network
 * Use with caution dinosaurs might appear!
 */

public class RetrofitAction1 implements Action1<Throwable> {
    @Override
    public void call(Throwable e) {
        e.printStackTrace();
        if(e instanceof RetrofitError){
            try {
                Response response = ((RetrofitError) e).getResponse();
                if(response!=null)
                    switch (response.getStatus()) {
                        case 400:
                        case 403:
                        case 500:
                            try {
                                RestError body = (RestError) ((RetrofitError) e).getBodyAs(RestError.class);
                                onHttpStatusError(body);
                            }catch (RuntimeException e1){
                                onHttpStatusError(new RestError(e.getMessage()));
                            }
                            break;
                    }
                else
                    onHttpStatusError(new RestError(e.getMessage()));

            }catch (Exception e1){
                e1.printStackTrace();
            }
        }

        call();
    }

    @SuppressWarnings("UnusedParameters")
    protected void onHttpStatusError(RestError body){}

    protected void call(){}

}