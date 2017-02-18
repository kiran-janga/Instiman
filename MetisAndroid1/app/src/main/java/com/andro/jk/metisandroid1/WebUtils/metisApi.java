package com.andro.jk.metisandroid1.WebUtils;

import com.andro.jk.metisandroid1.Dummy;
import com.andro.jk.metisandroid1.Models.CategoryModel;
import com.andro.jk.metisandroid1.Models.Device;
import com.andro.jk.metisandroid1.Models.HistoryModel;
import com.andro.jk.metisandroid1.Models.WorkerModel;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;


public interface metisApi {


    @FormUrlEncoded
    @POST("/social_login/")
    public void loginSocialUser(@FieldMap Map<String,String> params, Callback<JsonObject> Response);

    @Multipart
    @POST("/complaint/")
    public void uploadImageComplaint(@Header("Authorization") String auth_token,@Part("title") String title,@Part("image") TypedFile image,@Part("category") int categoryId,
                           @Part("location") String location,@Part("description") String description,@Part("number") String number,Callback<Dummy>Response);

    @Multipart
    @POST("/complaint/")
    public void uploadComplaint(@Header("Authorization") String auth_token,@Part("title") String title,@Part("category") int categoryId,
                                     @Part("location") String location,@Part("description") String description,Callback<Dummy>Response);

    @GET("/complaint/")
    public void getComplaints(@Header("Authorization") String auth_token, Callback<List<HistoryModel>> Response);

    @GET("/complaint/workers/")
    public void getWorkers(@Header("Authorization") String auth_token, Callback<List<WorkerModel>> Response);

    @GET("/complaint/management/")
    public void getCategoryComplaints(@Header("Authorization") String auth_token,Callback<List<HistoryModel>> Response);


    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void assignComplaint(@Header("Authorization") String auth_token,
                                @Part("assigned_to") int assigned_to,@Path("complaint_id") int complaint_id,
                                @Part("status") String status,@Part("comment") String comment,
                                @Part("priority") int priority,
                                Callback<JSONObject> Response);

    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void rejectComplaint(@Header("Authorization") String auth_token, @Path("complaint_id") int complaint_id,
                                @Part("status") String status,@Part("comment") String comment,
                                Callback<JSONObject> Response);



    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void updateComplaint(@Header("Authorization") String auth_token,@Part("title") String title,@Part("image") TypedFile image,@Part("category") int categoryId,
                                @Part("location") String location,@Part("description") String description,Callback<Dummy>Response);

    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void fixComplaint(@Header("Authorization") String auth_token,@Path("complaint_id") int complaint_id,@Part("status") String status,Callback<JSONObject>Response);

    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void changeCategory(@Header("Authorization") String auth_token,@Path("complaint_id") int complaint_id,@Part("category") int categoryId,Callback<JSONObject>Response);

    @Multipart
    @PUT("/complaint/assign/{complaint_id}/")
    public void rejectSimilar(@Header("Authorization") String auth_token,@Path("complaint_id") int complaint_id,@Part("comment") String comment,@Part("status") String status,Callback<JSONObject>Response);

    @POST("/gcm/v1/device/register/")
    public void registerDevice(@Header("Authorization") String auth_token,@Header("Content-Type") String content_type,
                               @Body Device device, Callback<JsonObject> Response);

    @GET("/complaint/category/")
    public void getCategories(@Header("Authorization") String auth_token, Callback<List<CategoryModel>> Response);

    @FormUrlEncoded
    @POST("/complaint/create_worker/")
    public void addWorker(@Header("Authorization") String auth_token,@Field("username") String username,@Field("password") String password,@Field("first_name") String first_name, Callback<JsonObject> Response);

    @FormUrlEncoded
    @POST("/login/")
    public void loginUser(@FieldMap Map<String, String> params, Callback<JsonObject> Response);

    @GET("/complaint/worker_complaint/")
    public void getWorkerComplaints(@Header("Authorization") String auth_token, Callback<List<HistoryModel>> Response);

}


