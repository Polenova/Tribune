package ru.polenova.tribune.api

import retrofit2.Response
import retrofit2.http.*
import ru.polenova.tribune.postModel.Post
import ru.polenova.tribune.postModel.PostRequestDto
import ru.polenova.tribune.postModel.User
import ru.polenova.tribune.postModel.UsersReactionModel

interface API {
    @POST("api/v1/authentication")
    suspend fun authenticate(@Body authRequestParams: AuthRequestParams): Response<Token>

    @POST("api/v1/registration")
    suspend fun register(@Body registrationRequestParams: RegistrationRequestParams): Response<Token>

    @POST("api/v1/posts")
    suspend fun createPost(@Body postRequestDto: PostRequestDto): Response<Void>

    @GET("api/v1/posts/me")
    suspend fun getPostsOfMe(): Response<List<Post>>

    @GET("api/v1/posts/username/{username}")
    suspend fun getPostsOfUser(@Path("username") username: String): Response<List<Post>>

    @GET("api/v1/posts/recent")
    suspend fun getRecent(): Response<List<Post>>

    @GET("api/v1/posts/{idPost}/get-posts-before")
    suspend fun getPostsBefore(@Path("idPost") idPost: Long): Response<List<Post>>

    @POST("api/v1/posts/{idPost}/up")
    suspend fun pressPostUp(@Path("idPost") idPost: Long): Response<Post>

    @POST("api/v1/posts/{idPost}/down")
    suspend fun pressPostDown(@Path("idPost") idPost: Long): Response<Post>

    @GET("api/v1/posts/{idPost}/reaction-by-users")
    suspend fun getReactionByUsers(@Path("idPost") idPost: Long): Response<List<UsersReactionModel>>
}



