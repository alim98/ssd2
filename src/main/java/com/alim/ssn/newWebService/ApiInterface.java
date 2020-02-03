package com.alim.ssn.newWebService;

import com.alim.ssn.like.LikeModel;
import com.alim.ssn.main.Comment.Comment;
import com.alim.ssn.main.Posts;
import com.alim.ssn.main.profile.FollowModel;
import com.alim.ssn.model.Post;
import com.alim.ssn.model.Recent;
import com.alim.ssn.model.Search;
import com.alim.ssn.model.StringResponseModel;
import com.alim.ssn.model.Student;
import com.alim.ssn.model.SuccessModel;
import com.alim.ssn.model.University;

import java.math.BigInteger;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiInterface {

    //**************AUTHENTICATE***********


    @Headers("Content-Type: application/json")
    @GET("student/checkt/{id}")
    Call<SuccessModel> checkToken(@Header("Authorization") String userKey, @Path("id") int stId);

    @POST("login")
    @FormUrlEncoded
    Call<StringResponseModel> login(@Field("phone_number") int stId, @Field("password") String password);

    @POST("logout")
    @FormUrlEncoded
    Call<StringResponseModel> logout(@Header("Authorization") String userKey, @Field("phone_number") int stId);

    @POST("register")
    @FormUrlEncoded
    Call<StringResponseModel> register(@Field("phone_number") int stId, @Field("first_name") String name, @Field("username") String username, @Field("password") String password);
    // *************STUDENT**************
    @GET("student/{id}")
    Call<Student> getStudent(@Path("id") int stId);

  @GET("student/search/{keyword}")
    Call<List<Student>> searchStudents(@Path("keyword") String keyword);

    @GET("student/{id}/followings/posts")
    Call<List<Post>> getPostsOfFollowings(@Header("Authorization") String token, @Path("id") int stId);

    @Headers("Content-Type: application/json")
    @GET("student/{id}/posts")
    Call<List<Post>>  getStudentPosts(@Header("Authorization") String token , @Path("id") int StId);

    @GET("student/stpub/{id}")
    Call<Student> getStPub(@Path("id") int stId);
    //*************POST
    @POST("post")
    @FormUrlEncoded
    Call<StringResponseModel> storePost(@Field("title") String title, @Field("desc") String desc, @Field("student_id") int studentId);

    @GET("post/{id}")
    Call<Post> getPost(@Path("id") String postId);


    @GET("tag/{tag}")
    Call<List<Post>> searchPostsByTag(@Path("tag") String tag);
    //************LIKE**********
    @GET("like/{student_id}/{post_id}")
    Call<LikeModel> isLiked(@Path("student_id") int studentId, @Path("post_id") String post_id);

    @POST("like")
    @FormUrlEncoded
    Call<LikeModel> like(@Header("Authorization") String userKey,@Field("student_id") int studentId, @Field("post_id") String postId);

    @DELETE("like/{student_id}/{post_id}")
    Call<LikeModel> unlike(@Header("Authorization") String userKey,@Path("student_id") int stId, @Path("post_id") String postId);

    @GET("like/get_like/{student_id}/{post_id}")
    Call<StringResponseModel> getLikeId(@Path("student_id") int stId, @Path("post_id") String postId);

    //**************FOLLOW************
    @GET("followers/{id}")
    Call<List<FollowModel>> getFollowers(@Path("id") int stId);

    @GET("followings/{id}")
    Call<List<FollowModel>> getFollowings(@Path("id") int stId);

    @POST("follow")
    @FormUrlEncoded
    Call<SuccessModel> follow(@Header("Authorization") String userKey,@Field("follower_id") int followerId, @Field("following_id") int followingId);

    @GET("follow/{follower_id}/{following_id}")
    Call<SuccessModel> isFollowed(@Path("follower_id") int followerId, @Path("following_id") int following_id);

    @DELETE("follow/{follower_id}/{following_id}")
    Call<SuccessModel> unFollow(@Header("Authorization") String userKey,@Path("follower_id") int followerId, @Path("following_id") int following_id);

    //*************COMMENT**********
    @POST("comment")
    @FormUrlEncoded
    Call<SuccessModel> comment(@Header("Authorization") String userKey,@Field("student_id") int StudentId, @Field("post_id") String postId, @Field("comment") String comment    );

    @DELETE("comment/{id}")
    Call<SuccessModel> deleteComment(@Header("Authorization") String userKey,@Path("post_id") String postId, @Path("student_id") int studentId);

    @GET("post/comments/{id}")
    Call<List<Comment>> getComments(@Path("id") String postId);
    //********BOOKMARK******

    @POST("savedpost")
    @FormUrlEncoded
    Call<SuccessModel> savePost(@Header("Authorization") String userKey,@Field("student_id") int StudentId, @Field("post_id") String postId);

    @Headers("Content-Type: application/json")
    @DELETE("savedpost/{st_id}/{post_id}")
    Call<SuccessModel> deleteSavedPost(@Header("Authorization") String userKey,@Path("st_id") int stId,  @Path("post_id") String postId);

    @Headers("Content-Type: application/json")
    @GET("student/saved/{id}")
    Call<List<Post>> getSavedPosts(@Header("Authorization") String userKey, @Path("id") int stId);

    @Headers("Content-Type: application/json")
    @GET("savedpost/check/{st_id}/{post_id}")
    Call<SuccessModel> checkSaved(@Header("Authorization") String userKey,@Path("st_id") int stId, @Path("post_id") String postId);


    //***********UNIVERSITY**********
    @GET("university/{code}")
    Call<University> getUniversity(@Path("code") int code);

    //***********RECENT***********
    @GET("recent/{id}")
    Call<List<Recent>> getRecents(@Path("id") int stId, @Header("Authorization") String userKey);

    //************SEARCH*************
    @GET("search/{keyword}")
    Call<Search> search(@Path("keyword") String keyword);
}
