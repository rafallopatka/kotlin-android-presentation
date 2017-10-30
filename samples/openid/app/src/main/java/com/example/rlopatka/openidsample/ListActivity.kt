package com.example.rlopatka.openidsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        this.txtToken.text = "Bearer ${TokenReference.token}"

        val factory: ApiFactory = RetrofitApiFactory()
        val client = UserApiClient(factory)

        val gson = Gson()

        client
                .getCurrentUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { gson.toJson(it) }
                .onErrorReturn { it.toString() }
                .subscribe{
                    txtUsers.text = it
                }
    }
}


