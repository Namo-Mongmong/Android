package com.example.namo.data.remote.login

import android.util.Log
import com.example.namo.config.ApplicationClass
import com.example.namo.config.BaseResponse
import com.example.namo.ui.bottom.custom.CustomSettingFramgent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutService(val view: CustomSettingFramgent) {

    val retrofitInterface = ApplicationClass.bRetrofit.create(LoginRetrofitInterface::class.java)

    fun tryPostLogout(body: LogoutBody) {
        retrofitInterface.postLogout(body).enqueue(object : Callback<BaseResponse> {

            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                when (response.code()) {
                    200 -> view.onPostLogoutSuccess(response.body() as BaseResponse)
                    else -> view.onPostLogoutFailure("통신 중 200 외 기타 오류")
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.d("Logout", "onFailure")
                view.onPostLogoutFailure(t.message ?: "통신 오류")
            }
        })
    }
}