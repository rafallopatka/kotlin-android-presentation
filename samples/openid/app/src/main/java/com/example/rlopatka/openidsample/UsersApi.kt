package com.example.rlopatka.openidsample

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by rlopatka on 30.10.2017.
 */
interface UsersApi {
    @GET("userinfo")
    fun getCurrentUser(): Observable<User>
}

interface ApiFactory {
    fun <TApi> create(apiClass: Class<TApi>): TApi
}

object TokenReference {
    var token: String = ""
}

class ApiHeadersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        builder.addHeader("Authorization", "Bearer ${TokenReference.token}")
        return chain.proceed(builder.build())
    }
}


class RetrofitApiFactory: ApiFactory {
    private var retrofit: Retrofit

    init {

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(ApiHeadersInterceptor())
                .build()

        this.retrofit = Retrofit.Builder()
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://kotlin-android-presentation.eu.auth0.com/")
                .build()

    }

    override fun <TApi> create(apiClass: Class<TApi>): TApi {
        return retrofit.create(apiClass)
    }
}

class UserApiClient(apiFactory: ApiFactory) {
    private val api: UsersApi by lazy { apiFactory.create(UsersApi::class.java) }

    fun getCurrentUser(): Observable<User> {
        return api.getCurrentUser()
    }
}


class User {
    @SerializedName("sub")
    var userId: String = ""

    @SerializedName("nickname")
    var userName: String = ""

    @SerializedName("email")
    var email: String = ""

    @SerializedName("updated_at")
    var updatedAt: String = ""
}