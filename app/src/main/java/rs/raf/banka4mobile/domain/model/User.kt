package rs.raf.banka4mobile.domain.model

data class User(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val identityType: String,
    val permissions: List<String>
)