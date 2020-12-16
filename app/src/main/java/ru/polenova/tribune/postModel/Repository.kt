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

const val BASE_URL = "https://polenova-api-tribune.herokuapp.com/"

object Repository {
    private var retrofit: Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private var api: API = retrofit.create(API::class.java)

    suspend fun authenticate(username: String, password: String) = api.authenticate(AuthRequestParams(username, password))
    suspend fun register(username: String, password: String) = api.register(AuthRequestParams(username, password))


    suspend fun getRecent() = api.getRecent()

    suspend fun pressedPostUp(idPost: Long) = api.pressedPostUp(idPost)
    suspend fun pressedPostUpRemove(idPost: Long) = api.pressedPostUpRemove(idPost)

    suspend fun pressedPostDown(idPost: Long) = api.pressedPostDown(idPost)
    suspend fun pressedPostDownRemove(idPost: Long) = api.pressedPostDownRemove(idPost)

    suspend fun createPost(content: String, attachmentImage: String,
                           attachmentLink: String): Response<Void> {
        var link: String? = attachmentLink
        if (link!!.isEmpty()) {
            link = null
        } else if (!link.contains("http://") || !link.contains("https://")) {
            link = "https://$link"
        }
        val postRequestDto = PostRequestDto(
            textPost = content,
            attachmentImage = attachmentImage,
            statusUser = StatusUser.NONE,
            attachmentLink = link
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