package ru.polenova.tribune.api

import retrofit2.Response
import retrofit2.http.*
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.PostRequestDto

interface API {
    @POST("api/V1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/V1/registration")
    suspend fun register(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/V1/posts")
    suspend fun createPost(@Body postRequestDto: PostRequestDto): Response<Void>

    @GET("api/V1/posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("api/V1/posts/recent")
    suspend fun getRecent(): Response<List<Post>>

    @GET("api/V1/posts/{idPost}/get-posts-after")
    suspend fun getPostsAfter(@Path("id") idPost: Long): Response<List<Post>>

    @GET("api/V1/posts/{idPost}/get-posts-before")
    suspend fun getPostsBefore(@Path("id") idPost: Long): Response<List<Post>>

    @POST("api/V1/posts/{idPost}/up")
    suspend fun pressedPostUp(@Path("idUser") idPost: Long): Response<Post>

    @DELETE("api/V1/posts/{idPost}/up")
    suspend fun pressedPostUpRemove(@Path("idUser") idPost: Long): Response<Post>

    @POST("api/V1/posts/{idPost}/down")
    suspend fun pressedPostDown(@Path("idUser") idPost: Long): Response<Post>

    @DELETE("api/V1/posts/{idPost}/down")
    suspend fun pressedPostDownRemove(@Path("idUser") idPost: Long): Response<Post>

}