package com.mongmong.namo.data.remote.auth

import com.mongmong.namo.domain.model.LoginResponse
import com.mongmong.namo.presentation.config.BaseResponse

interface SplashView {
    fun onVerifyTokenSuccess(response: LoginResponse)
    fun onVerifyTokenFailure(message: String)
}

interface LogoutView {
    fun onPostLogoutSuccess(response: BaseResponse)
    fun onPostLogoutFailure(message: String)
}