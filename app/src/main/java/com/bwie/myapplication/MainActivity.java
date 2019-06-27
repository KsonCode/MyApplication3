package com.bwie.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bwie.myapplication.entity.BaseResponseEntity;
import com.bwie.myapplication.entity.LoginEntity;
import com.bwie.myapplication.entity.ProductEntity;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 嵌套请求，注册接口，登录接口，获取商品列表，同步购物车，查询购物车
     * 二层嵌套
     *
     * @param view
     */
    public void request(View view) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();


        HashMap<String, String> regParams = new HashMap<>();
        regParams.put("phone", "18677772237");
        regParams.put("pwd", "111111");
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("phone", "18677772237");
        loginParams.put("pwd", "111111");
        HashMap<String, String> searchParams = new HashMap<>();
        searchParams.put("keyword", "手机");
        searchParams.put("page", "1");
        searchParams.put("count", "10");


        // 步骤1：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(Api.BASE_URL) // 设置 网络请求 Url
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();


        ApiService apiService = retrofit.create(ApiService.class);

        // 定义Observable接口类型的网络请求对象
        Observable<BaseResponseEntity> regObs = apiService.reg(Api.REG_URL, regParams);
        final Observable<LoginEntity> logobs = apiService.login(Api.LOGIN_URL, loginParams);
        Observable<ProductEntity> searchObs = apiService.search(Api.SEARCH_URL, searchParams);

        regObs.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                //第一次请求
                .doOnNext(new Consumer<BaseResponseEntity>() {
                    @Override
                    public void accept(BaseResponseEntity baseResponseEntity) throws Exception {
                        System.out.println("aaaa-reg===" + baseResponseEntity.message);

                    }
                })
                //转换成第二个被观察者
                .observeOn(Schedulers.io())
                .flatMap(new Function<BaseResponseEntity, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(BaseResponseEntity baseResponseEntity) throws Exception {
                        return logobs;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(LoginEntity loginEntity) throws Exception {

                        System.out.println("aaaa-login=====" + loginEntity.message);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });


    }

    /**
     * 多层嵌套
     *
     * @param view
     */
    public void requests(View view) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();


        HashMap<String, String> regParams = new HashMap<>();
        regParams.put("phone", "18677772237");
        regParams.put("pwd", "111111");
        HashMap<String, String> loginParams = new HashMap<>();
        loginParams.put("phone", "18677772237");
        loginParams.put("pwd", "111111");
        HashMap<String, String> searchParams = new HashMap<>();
        searchParams.put("keyword", "手机");
        searchParams.put("page", "1");
        searchParams.put("count", "10");


        // 步骤1：创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(Api.BASE_URL) // 设置 网络请求 Url
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build();


        ApiService apiService = retrofit.create(ApiService.class);

        // 定义Observable接口类型的网络请求对象
        Observable<BaseResponseEntity> regObs = apiService.reg(Api.REG_URL, regParams);
        final Observable<LoginEntity> logobs = apiService.login(Api.LOGIN_URL, loginParams);
        final Observable<ProductEntity> searchObs = apiService.search(Api.SEARCH_URL, searchParams);

        regObs.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                //第一次请求
                .doOnNext(new Consumer<BaseResponseEntity>() {
                    @Override
                    public void accept(BaseResponseEntity baseResponseEntity) throws Exception {
                        System.out.println("aaaa-reg===" + baseResponseEntity.message);

                    }
                })
                //转换成第二个被观察者,指定子线程
                .observeOn(Schedulers.io())
                .flatMap(new Function<BaseResponseEntity, ObservableSource<LoginEntity>>() {
                    @Override
                    public ObservableSource<LoginEntity> apply(BaseResponseEntity baseResponseEntity) throws Exception {
                        return logobs;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<LoginEntity>() {
                    @Override
                    public void accept(LoginEntity loginEntity) throws Exception {

                        System.out.println("aaaa-login==="+loginEntity.message);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<LoginEntity, ObservableSource<ProductEntity>>() {
                    @Override
                    public ObservableSource<ProductEntity> apply(LoginEntity loginEntity) throws Exception {
                        return searchObs;
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ProductEntity>() {
            @Override
            public void accept(ProductEntity productEntity) throws Exception {

                System.out.println("aaaa-search==="+productEntity.result.size());

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        });

    }
}
