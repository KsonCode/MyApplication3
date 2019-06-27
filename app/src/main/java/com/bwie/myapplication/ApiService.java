package com.bwie.myapplication;

import com.bwie.myapplication.entity.BaseResponseEntity;
import com.bwie.myapplication.entity.LoginEntity;
import com.bwie.myapplication.entity.ProductEntity;

import java.util.HashMap;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @POST
    @FormUrlEncoded
    Observable<BaseResponseEntity> reg(@Url String url, @FieldMap HashMap<String, String> params);

    @POST
    @FormUrlEncoded
    Observable<LoginEntity> login(@Url String url, @FieldMap HashMap<String, String> params);

    @GET
    Observable<ProductEntity> search(@Url String url, @QueryMap HashMap<String, String> params);


}
