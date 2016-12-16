package com.example.retrofitdemo.data.api;

import com.example.retrofitdemo.data.api.response.GetIpInfoResponse;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by wangyuhang on 16/12/16.
 */

public interface ApiService {
    @GET("service/getIpInfo.php")
    Observable<GetIpInfoResponse> getIpInfo(@Query("ip") String ip);//使用RxJava

//    @GET("service/getIpInfo.php")
//    Call<GetIpInfoResponse> getIpInfo(@Query("ip") String ip);//不使用RxJava
}
