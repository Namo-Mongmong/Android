package com.mongmong.namo.data.remote.login

import android.util.Log
import com.mongmong.namo.data.remote.LoginApiService
import com.mongmong.namo.domain.model.LoginResponse
import com.mongmong.namo.domain.model.TokenBody
import com.mongmong.namo.presentation.config.ApplicationClass
import com.mongmong.namo.presentation.ui.login.LoginFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService(val view: LoginFragment) {

    val loginApiService = ApplicationClass.bRetrofit.create(LoginApiService::class.java)

    fun tryPostKakaoSDK(body: TokenBody) {
        loginApiService.postKakaoSDK(body).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                view.onPostKakaoSDKSuccess(response.body() as LoginResponse)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("KakaoLogin", "onFailure")
                view.onPostKakaoSDKFailure(t.message ?: "통신 오류")
            }
        })
    }

    fun tryPostNaverSDK(body: TokenBody) {
        loginApiService.postNaverSDK(body).enqueue(object : Callback<LoginResponse> {

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                view.onPostNaverSDKSuccess(response.body() as LoginResponse)
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("NaverLogin", "onFailure")
                view.onPostNaverSDKFailure(t.message ?: "통신 오류")
            }
        })
    }
}