package rs.raf.banka4mobile.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST
import rs.raf.banka4mobile.data.remote.dto.LoginRequestDto
import rs.raf.banka4mobile.data.remote.dto.LoginResponseDto

interface AuthApi {

    @POST("/api/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto
}