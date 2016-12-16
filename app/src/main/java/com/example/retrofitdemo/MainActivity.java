package com.example.retrofitdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.retrofitdemo.data.api.ApiService;
import com.example.retrofitdemo.data.api.response.GetIpInfoResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.Subscriber;

public class MainActivity extends Activity {

    private static final String ENDPOINT = "http://ip.taobao.com";
    private TextView mTvContent;
    private ProgressBar mProgressBar;
    private Button mButton;

    private Subscription mSubscription;//用于取消订阅

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvContent = (TextView) findViewById(R.id.tv_content);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mButton = (Button) findViewById(R.id.button_start);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvContent.setText("");
                mProgressBar.setVisibility(View.VISIBLE);

                //＝＝＝＝＝＝＝使用RxJava＝＝＝＝＝＝＝
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ENDPOINT)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                ApiService apiService = retrofit.create(ApiService.class);
                mSubscription =  apiService.getIpInfo("63.223.108.42")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<GetIpInfoResponse>() {
                            @Override
                            public void onCompleted() {
                                mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mProgressBar.setVisibility(View.GONE);
                                mTvContent.setText(e.getMessage());
                            }

                            @Override
                            public void onNext(GetIpInfoResponse getIpInfoResponse) {
                                mTvContent.setText(getIpInfoResponse.data.country);
                            }
                        });

                //＝＝＝＝＝＝＝不使用RxJava＝＝＝＝＝＝＝
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl(ENDPOINT)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                ApiService apiService = retrofit.create(ApiService.class);
//
//                Call<GetIpInfoResponse> call = apiService.getIpInfo("63.223.108.42");
//                call.enqueue(new Callback<GetIpInfoResponse>() {
//                    @Override
//                    public void onResponse(Response<GetIpInfoResponse> response, Retrofit retrofit) {
//                        mProgressBar.setVisibility(View.GONE);
//                        GetIpInfoResponse getIpInfoResponse = response.body();
//                        mTvContent.setText(getIpInfoResponse.data.country);
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        mProgressBar.setVisibility(View.GONE);
//                        mTvContent.setText(t.getMessage());
//                    }
//                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        //使用RxJava方式时，需要取消订阅
        if (mSubscription != null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
