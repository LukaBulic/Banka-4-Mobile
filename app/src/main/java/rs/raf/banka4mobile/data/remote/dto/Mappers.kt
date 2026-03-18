package rs.raf.banka4mobile.data.remote.dto

import rs.raf.banka4mobile.domain.model.Session
import rs.raf.banka4mobile.domain.model.User

fun AuthUserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        username = username,
        identityType = identityType,
        permissions = permissions
    )
}

fun LoginResponseDto.toDomain(): Session {
    return Session(
        token = token,
        refreshToken = refreshToken,
        user = user.toDomain()
    )
}