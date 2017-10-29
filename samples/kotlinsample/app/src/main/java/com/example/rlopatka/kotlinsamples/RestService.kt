package com.example.rlopatka.kotlinsamples

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by rlopatka on 29.10.2017.
 */
interface PeopleApi {
    @GET("People")
    fun getAll(): Observable<GetEnvelope<Collection<Person>>>

    @GET("People('{id}')")
    fun getById(@Path("id") fullName: String): Observable<Person>
}

class GetEnvelope<T>{
    var value: T? = null
}

data class Person (
        @Transient
        var id: Long = 0,

        @SerializedName("UserName")
        var userName: String = "",

        @SerializedName("FirstName")
        var firstName: String = "",

        @SerializedName("Emails")
        var emails: Collection<String> = arrayListOf()
)

class PeopleService{
    private var retrofit: Retrofit
    private var api: PeopleApi

    init {
        this.retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://services.odata.org/V4/(S(01klqq01m5nxmikzy4xcjpsz))/TripPinServiceRW/")
                .build()

        api = retrofit.create(PeopleApi::class.java)
    }

    fun getPersonalInfo(fullName: String): Observable<Person> {
        return api.getById(fullName)
    }
}