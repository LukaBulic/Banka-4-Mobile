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
    private val secret = TOTPGenerator("JBSWY3DPEHPK3PXP")
    private val codeValiditySeconds = 300 // 5 min
    private var codeStartTime: Long = System.currentTimeMillis()

    init {
        generateNewCode()
        viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val secondsPassed = ((now - codeStartTime) / 1000).toInt()
                val secondsLeft = (codeValiditySeconds - secondsPassed).coerceAtLeast(0)

                setState { copy(secondsLeft = secondsLeft) }

                if (secondsLeft == 0) {
                    codeStartTime = now
                    generateNewCode()
                    _sideEffects.emit(SideEffect.ShowToast("Kod je ponovo kreiran"))
                }

                delay(1000)
            }
        }
    }

    private fun generateNewCode() {
        val totp = secret.generate()
        setState { copy(totp = totp, secondsLeft = codeValiditySeconds) }
    }
}