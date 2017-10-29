package com.example.rlopatka.openidsample

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.openid.appauth.*
import net.openid.appauth.AuthorizationServiceConfiguration.RetrieveConfigurationCallback
import android.content.Intent
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import android.app.PendingIntent

class MainActivity : AppCompatActivity() {

    var authState = AuthState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener{ login() }
    }

    private fun login() {
        val url = Uri.parse("https://kotlin-android-presentation.eu.auth0.com")
        AuthorizationServiceConfiguration.fetchFromIssuer(url, {
            cfg, exception ->
            if (cfg == null){
                Log.e("App", "Configuration fetch failed", exception)
                return@fetchFromIssuer
            }
            authorize(cfg)
        })
    }

    private fun authorize(cfg: AuthorizationServiceConfiguration) {
        val clientId = "USE_YOUR_OWN"


        val redirectUrl = Uri.parse("com.github.raflop.kotlin-android-presentation://openidredirect")
        val request = AuthorizationRequest
                .Builder(cfg, clientId, ResponseTypeValues.CODE, redirectUrl)
                .setScopes(AuthorizationRequest.Scope.OPENID, AuthorizationRequest.Scope.PROFILE, "read:users")
                .build()

        val authService = AuthorizationService(this)

        authService.performAuthorizationRequest(
                request,
                PendingIntent.getActivity(this, 0, Intent(this, Dashboard::class.java), 0),
                PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0))
    }


    companion object {
        const val RC_AUTH = 1234
    }
}
