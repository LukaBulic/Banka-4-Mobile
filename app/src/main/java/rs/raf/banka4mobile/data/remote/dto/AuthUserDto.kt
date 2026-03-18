package rs.raf.banka4mobile.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    val id: Int,
    val email: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    val username: String,
    @SerialName("identity_type")
    val identityType: String,
    val permissions: List<String> = emptyList()
)