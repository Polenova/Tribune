package ru.polenova.tribune.postModel

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.polenova.tribune.api.API
import ru.polenova.tribune.api.AuthRequestParams
import ru.polenova.tribune.api.InjectAuthTokenInterceptor
import ru.polenova.tribune.api.RegistrationRequestParams
import ru.polenova.tribune.postModel.Repository.getCurrentUser

const val BASE_URL = "https://polenova-api-tribune.herokuapp.com/"

object Repository {
    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private var api: API = retrofit.create(API::class.java)

    suspend fun authenticate(username: String, password: String) =
        api.authenticate(AuthRequestParams(username, password))

    suspend fun register(username: String, password: String) =
        api.register(RegistrationRequestParams(username, password))

    suspend fun getPostsBefore(idPost: Long) = api.getPostsBefore(idPost)

    suspend fun getRecent() = api.getRecent()

    suspend fun getCurrentUser() =api.getCurrentUser()

    suspend fun pressPostUp(idPost: Long) = api.pressPostUp(idPost)

    suspend fun pressPostDown(idPost: Long) = api.pressPostDown(idPost)

    suspend fun getReactionByUsers(idPost: Long) = api.getReactionByUsers(idPost)

    suspend fun createPost(
        namePost: String, textPost: String,
        link: String
    ): Response<Void> {
        var attachmentLink: String? = link
        when {
            attachmentLink!!.isEmpty() -> {
                attachmentLink = null
            }
            !attachmentLink.contains("http") -> {
                attachmentLink = "https://$attachmentLink"
            }
        }
        val postRequestDto = PostRequestDto(
            postName = namePost,
            postText = textPost,
            //attachmentImage = attachmentImage,
            //statusUser = StatusUser.NONE,
            link = attachmentLink
            //attachmentId = attachmentModelId
        )
        return api.createPost(postRequestDto)
    }

    fun createRetrofitWithAuth(authToken: String) {
        val httpLoggerInterceptor = HttpLoggingInterceptor()
        httpLoggerInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(InjectAuthTokenInterceptor(authToken))
            .addInterceptor(httpLoggerInterceptor)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(API::class.java)
    }

}