package rs.raf.banka4mobile.feature.verification

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base32
import dev.turingcomplete.kotlinonetimepassword.HmacAlgorithm
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordConfig
import dev.turingcomplete.kotlinonetimepassword.TimeBasedOneTimePasswordGenerator
import java.time.Instant
import java.util.concurrent.TimeUnit

class TOTPGenerator(
    secretBase32: String,
    private val codeDigits: Int = 6,
    private val timeStepSeconds: Long = 30
) {
    private val secretBytes: ByteArray = Base32().decode(secretBase32)

    fun generate(currentTime: Instant = Instant.now()): String {
        val config = TimeBasedOneTimePasswordConfig(
            codeDigits = codeDigits,
            hmacAlgorithm = HmacAlgorithm.SHA1,
            timeStep = timeStepSeconds,
            timeStepUnit = TimeUnit.SECONDS
        )
        val generator = TimeBasedOneTimePasswordGenerator(secretBytes, config)
        return generator.generate(currentTime)
    }
}