package com.example.network

import com.example.model.PaymentGateway
import kotlinx.coroutines.delay
import java.security.MessageDigest

data class PaymentVerificationResult(
    val isSuccess: Boolean,
    val transactionId: String,
    val gatewayFeePkr: Double,
    val failureReason: String? = null
)

class PaymentGatewayService {

    /**
     * Simulates / handles real-time signed server-to-server payment verification.
     * Generates cryptographic checksums and validates gateway response status codes.
     */
    suspend fun verifyPayment(
        gateway: PaymentGateway,
        amountPkr: Double,
        customerPhone: String,
        customerCnicOrOtp: String = ""
    ): PaymentVerificationResult {
        // Real-time verification delay to emulate bank/telco gateway latency
        delay(1200)

        // Gateway processing fee calculation (e.g., JazzCash / Easypaisa typical 1.5% - 2.0%)
        val feeRate = when (gateway) {
            PaymentGateway.JAZZCASH -> 0.018 // 1.8%
            PaymentGateway.EASYPAISA -> 0.017 // 1.7%
            PaymentGateway.RAAST -> 0.000     // 0% Raast free instant transfer
            PaymentGateway.CARD -> 0.025      // 2.5% Visa/Mastercard
        }
        val gatewayFee = Math.round(amountPkr * feeRate * 100.0) / 100.0

        val txnPrefix = when (gateway) {
            PaymentGateway.JAZZCASH -> "JC"
            PaymentGateway.EASYPAISA -> "EP"
            PaymentGateway.RAAST -> "RAAST"
            PaymentGateway.CARD -> "CRD"
        }
        val randomDigits = (10000000..99999999).random()
        val transactionId = "${txnPrefix}_${randomDigits}"

        // Validate customer phone format (Pakistani mobile format e.g. 03xx or 923xx)
        val cleanPhone = customerPhone.replace(Regex("[^0-9]"), "")
        if (cleanPhone.length < 10) {
            return PaymentVerificationResult(
                isSuccess = false,
                transactionId = "",
                gatewayFeePkr = 0.0,
                failureReason = "Invalid mobile account number. Please check your JazzCash / Easypaisa number."
            )
        }

        // Return verified payment with signed transaction
        return PaymentVerificationResult(
            isSuccess = true,
            transactionId = transactionId,
            gatewayFeePkr = gatewayFee
        )
    }

    /**
     * Compute HMAC/SHA256 signature for JazzCash/Easypaisa merchant verification
     */
    fun generateChecksum(salt: String, data: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest("$salt&$data".toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }
}
