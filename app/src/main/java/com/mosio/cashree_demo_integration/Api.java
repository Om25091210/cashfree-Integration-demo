package com.mosio.cashree_demo_integration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @POST("/api/create-order")
    Call<DataModal> createToken(@Body DataModal dataModal);
}
