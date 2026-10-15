package com.example.model

enum class OrderStatus {
    COMPLETED,
    PROCESSING,
    FAILED,
    PENDING_MANUAL
}

enum class PaymentGateway {
    JAZZCASH,
    EASYPAISA,
    RAAST,
    CARD
}

data class Order(
    val id: String,
    val orderNumber: String,
    val customerName: String,
    val customerEmail: String,
    val customerWhatsapp: String,
    val productId: String,
    val productName: String,
    val productSource: ProductSource,
    val quantity: Int = 1,
    // Customer Store Accounting
    val customerPaidPkr: Double,
    val paymentGateway: PaymentGateway,
    val paymentTransactionId: String,
    val gatewayFeePkr: Double,
    // Supplier Accounting (Account 1 - MakerCC)
    val supplierCostPkr: Double,
    val supplierOrderId: String? = null,
    // Calculated Profits
    val grossProfitPkr: Double,
    val netProfitPkr: Double,
    // Fulfillment
    val status: OrderStatus,
    val deliveryCredentials: String? = null, // Formatted login / activation keys
    val failureReason: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
