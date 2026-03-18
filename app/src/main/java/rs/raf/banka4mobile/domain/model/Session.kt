package rs.raf.banka4mobile.domain.model

data class Session(
    val token: String,
    val refreshToken: String,
    val user: User
)