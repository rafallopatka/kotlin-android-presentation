package com.example.rlopatka.openidsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse



class Dashboard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val resp = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (resp != null) {
            txtLoginInfo.text = resp.jsonSerializeString()
        } else {
            txtLoginInfo.text = "authorization failed, check ex for more details $ex"
        }
    }
}
