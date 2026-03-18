package rs.raf.banka4mobile.data.repository

import retrofit2.HttpException
import rs.raf.banka4mobile.data.local.session.SessionManager
import rs.raf.banka4mobile.data.remote.api.AuthApi
import rs.raf.banka4mobile.data.remote.dto.LoginRequestDto
import rs.raf.banka4mobile.data.remote.dto.toDomain
import rs.raf.banka4mobile.domain.model.Session
import rs.raf.banka4mobile.domain.repository.AuthRepository
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Session> {
        return try {
            val response = authApi.login(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

            val session = response.toDomain()

            if (session.user.identityType.lowercase() != "client") {
                Result.failure(
                    IllegalStateException("Mobilna aplikacija je dostupna samo klijentima.")
                )
            } else {
                Result.success(session)
            }

        } catch (e: HttpException) {
            Result.failure(Exception("Pogrešan email ili lozinka."))
        } catch (e: IOException) {
            Result.failure(Exception("Greška u konekciji sa serverom."))
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Došlo je do neočekivane greške."))
        }
    }

    override suspend fun saveSession(session: Session) {
        sessionManager.saveSession(session)
    }

    override suspend fun getSession(): Session? {
        return sessionManager.getSession()
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }
}