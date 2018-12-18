package com.example.kshitij.findmyleaf;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {

    @Multipart
    @POST("api/v1/notification/new/")
        //change string to your api endpoint
    Call<ImageUploadResponse> uploadImageWithData(
            @Header("Authorization") String token,
            @Part("_id") RequestBody id,
            @Part("epoch") RequestBody epoch,
            @Part("message") RequestBody message,
            @Part("date") RequestBody date,
            @Part MultipartBody.Part image,
            @Part("_v") RequestBody version,
            @Part("title") RequestBody title
    );
}
