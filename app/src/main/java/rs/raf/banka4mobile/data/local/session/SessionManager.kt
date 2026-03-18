package rs.raf.banka4mobile.data.local.session

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.catch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import rs.raf.banka4mobile.domain.model.Session
import rs.raf.banka4mobile.domain.model.User

private val Context.dataStore by preferencesDataStore(name = "user_session")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val TOKEN = stringPreferencesKey("token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val USER_ID = intPreferencesKey("user_id")
        val EMAIL = stringPreferencesKey("email")
        val FIRST_NAME = stringPreferencesKey("first_name")
        val LAST_NAME = stringPreferencesKey("last_name")
        val USERNAME = stringPreferencesKey("username")
        val IDENTITY_TYPE = stringPreferencesKey("identity_type")
        val PERMISSIONS = stringPreferencesKey("permissions")
    }

    suspend fun saveSession(session: Session) {
        context.dataStore.edit { preferences ->
            preferences[Keys.TOKEN] = session.token
            preferences[Keys.REFRESH_TOKEN] = session.refreshToken
            preferences[Keys.USER_ID] = session.user.id
            preferences[Keys.EMAIL] = session.user.email
            preferences[Keys.FIRST_NAME] = session.user.firstName
            preferences[Keys.LAST_NAME] = session.user.lastName
            preferences[Keys.USERNAME] = session.user.username
            preferences[Keys.IDENTITY_TYPE] = session.user.identityType
            preferences[Keys.PERMISSIONS] = session.user.permissions.joinToString(",")
        }
    }

    suspend fun getSession(): Session? {
        val preferences = context.dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .first()

        val token = preferences[Keys.TOKEN] ?: return null
        val refreshToken = preferences[Keys.REFRESH_TOKEN] ?: return null
        val userId = preferences[Keys.USER_ID] ?: return null
        val email = preferences[Keys.EMAIL] ?: return null
        val firstName = preferences[Keys.FIRST_NAME] ?: return null
        val lastName = preferences[Keys.LAST_NAME] ?: return null
        val username = preferences[Keys.USERNAME] ?: return null
        val identityType = preferences[Keys.IDENTITY_TYPE] ?: return null
        val permissionsString = preferences[Keys.PERMISSIONS].orEmpty()

        val permissions = if (permissionsString.isBlank()) {
            emptyList()
        } else {
            permissionsString.split(",")
        }

        return Session(
            token = token,
            refreshToken = refreshToken,
            user = User(
                id = userId,
                email = email,
                firstName = firstName,
                lastName = lastName,
                username = username,
                identityType = identityType,
                permissions = permissions
            )
        )
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}