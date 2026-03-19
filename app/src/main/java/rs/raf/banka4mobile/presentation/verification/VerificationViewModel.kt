package rs.raf.banka4mobile.presentation.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject
import rs.raf.banka4mobile.feature.verification.TOTPGenerator
import rs.raf.banka4mobile.presentation.verification.VerificationContract.SideEffect
import rs.raf.banka4mobile.presentation.verification.VerificationContract.UiState

@HiltViewModel
class VerificationViewModel @Inject constructor(
    // repository
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: UiState.() -> UiState) = _state.getAndUpdate(reducer)

    private val _sideEffects = MutableSharedFlow<SideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()

    // Dummy secret for testing
    private val codeValiditySeconds = 30L
    private val totpGenerator = TOTPGenerator(
        secretBase32 = "7A2VVUBSKMI7J2OSNMBJ3GG7WVZA77N5",
        timeStepSeconds = codeValiditySeconds
    )
    private var lastTimeStep: Long? = null

    init {
        viewModelScope.launch {
            while (true) {
                val epochSeconds = System.currentTimeMillis() / 1000
                val currentTimeStep = epochSeconds / codeValiditySeconds
                val secondsLeft = (codeValiditySeconds - (epochSeconds % codeValiditySeconds)).toInt()

                if (lastTimeStep == null || lastTimeStep != currentTimeStep) {
                    val isRefresh = lastTimeStep != null
                    lastTimeStep = currentTimeStep
                    generateNewCode()
                    if (isRefresh) {
                        _sideEffects.emit(SideEffect.ShowToast("Kod je ponovo kreiran"))
                    }
                }

                // Backend may accept recently generated codes for up to 5 minutes,
                // but UI should show canonical 30s TOTP rotation.
                setState { copy(secondsLeft = secondsLeft) }

                delay(1000)
            }
        }
    }

    private fun generateNewCode() {
        val totp = totpGenerator.generate()
        setState { copy(totp = totp) }
    }
}