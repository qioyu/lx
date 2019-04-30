package com.example.lianxi.poem;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Service_poem {

    @FormUrlEncoded
    @POST("getTangPoetry")
    Call<poemdata> getcall(@Field("page") Integer page, @Field("count")Integer count);



}
