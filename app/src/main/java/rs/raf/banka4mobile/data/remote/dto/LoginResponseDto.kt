package rs.raf.banka4mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseDto(
    val token: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    val user: AuthUserDto
)