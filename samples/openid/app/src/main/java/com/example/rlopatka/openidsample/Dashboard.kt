package com.example.rlopatka.openidsample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork.LOG_TAG
import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import net.openid.appauth.TokenResponse
import net.openid.appauth.AuthorizationService
import retrofit2.adapter.rxjava2.Result.response





class Dashboard : AppCompatActivity() {
    val authState = AuthState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val resp = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (resp != null) {
            authState.update(resp, ex)
            txtLoginInfo.text = resp.jsonSerializeString()
            updatetAccessToken(resp)
        } else {
            txtLoginInfo.text = "authorization failed, check ex for more details $ex"
        }

        this.btnGoToList.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            this.startActivity(intent)
            this.finish()
        }
    }

    fun updatetAccessToken(response: AuthorizationResponse) {
        val service = AuthorizationService(this)

        service.performTokenRequest(response.createTokenExchangeRequest()) { tokenResponse, exception ->
            if (exception != null) {
                Log.w(LOG_TAG, "Token Exchange failed", exception)
            } else {
                if (tokenResponse != null) {
                    authState.update(tokenResponse, exception)

                    TokenReference.token = authState.accessToken!!

                    Log.i(LOG_TAG, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken))
                }
            }
        }
    }
}
