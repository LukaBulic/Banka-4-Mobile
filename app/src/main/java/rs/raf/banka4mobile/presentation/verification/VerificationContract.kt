package rs.raf.banka4mobile.presentation.verification

interface VerificationContract {

    data class UiState(
        val totp: String = "",
        val secondsLeft: Int = 30,
        val isLoading: Boolean = false,
        val error: Throwable? = null
    )

    sealed class UiEvent {
        // no events for now
    }

    sealed class SideEffect {
        data class ShowToast(val message: String) : SideEffect()
    }
}