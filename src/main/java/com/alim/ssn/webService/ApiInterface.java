package com.alim.ssn.webService;

import com.alim.ssn.controller.LikeExistsModel;
import com.alim.ssn.firebase.ResponseModel;
import com.alim.ssn.main.Posts;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Student;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("student/{id}/show_posts/")
    Call<Posts> getPosts(@Path("id") String id);

    @GET("student/")
    Call<List<Student>> getStudents();

    @GET("posts")
    Call<List<Post>> getAllPosts();

    @POST("post/store_post")
    @FormUrlEncoded
    Call<ResponseModel> storePost(@Field("student_id") String student_id, @Field("content") String content, @Field("field") String field, @Field("grade") String grade);

   @Multipart
    @POST("post/store_post_with_image")
    Call<ResponseModel> storePostWithImage(@Part MultipartBody.Part file,@Part("student_id") String studentId,@Part("content")String content, @Part("field") String field, @Part("grade") String grade);


    @PUT("post/{id}/update_likes_count")
    @FormUrlEncoded
    Call<ResponseModel> updatePostLikesCount(@Path("id") String id, @Field("likes_count") int likesCount);

    @POST("like/store_like")
    @FormUrlEncoded
    Call<ResponseModel> addLikeToLikesTable(@Field("student_id") String studentId, @Field("post_id") int postId);

    @GET("like/is_exist/{post_id}/{student_id}")
    Call<LikeExistsModel> likeExists(@Path("post_id") int postId , @Path("student_id") String student_id);

    @POST("student/store_new_student")
    @FormUrlEncoded
    Call<ResponseModel> storeStudent(@Field("first_name") String firstName, @Field("last_name") String lastName, @Field("uid") String uid, @Field("email") String email, @Field("password") String password);

/*    @POST("student/is_exist")
    @FormUrlEncoded
    Call<IsExistModel> checkLoginOrRegister(@Field("phone_number") String phoneNumber);

    @POST("student/register")
    @FormUrlEncoded
    Call<RegisterOrLoginResponseModel> register(@Field("phone_number") String phoneNumber, @Field("password") String password, @Field("name") String name, @Field("last_name") String lastname);

    @POST("student/login")
    @FormUrlEncoded
    Call<RegisterOrLoginResponseModel> login(@Field("phone_number") String phoneNumber, @Field("password") String password);

    @Headers(  "Content-Type: application/json"
            )
    @POST("logout")
    Call<RegisterOrLoginResponseModel> logout(@Header("Authorization") String userkey);*/

    @GET("check_token")
    Call<String> checkToken(@Header("Authorization") String userKey);


    @Headers("Content-Type: application/json")
    @GET("details")
    Call<Student> getStudentInfo(@Header("Authorization") String userKey);

}
