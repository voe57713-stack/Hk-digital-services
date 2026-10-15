package com.example.model

data class CustomerReview(
    val id: String,
    val customerName: String,
    val rating: Int, // 1 to 5
    val comment: String,
    val purchasedProduct: String,
    val dateFormatted: String,
    val isApproved: Boolean = true
)

data class WhatsAppFeedback(
    val id: String,
    val customerName: String,
    val caption: String,
    val imageType: String, // "chat_preview_1", "chat_preview_2", etc.
    val previewSnippet: String,
    val dateFormatted: String,
    val isPublished: Boolean = true,
    val displayOrder: Int = 1,
    val isObscuredForPrivacy: Boolean = true
)

data class MakerCcConfig(
    val apiEndpoint: String = "https://makercc.shop/api/v1/index.php",
    val apiKey: String = "",
    val walletBalancePkr: Double = 14500.0, // Supplier prepaid balance
    val totalSpentSupplierPkr: Double = 4650.0,
    val totalSupplierOrdersCount: Int = 15,
    val isConnected: Boolean = true,
    val lastSyncTime: String = "Today, 03:45 AM"
)

data class PaymentGatewayConfig(
    val jazzcashEnabled: Boolean = true,
    val jazzcashMerchantId: String = "MC_99341",
    val jazzcashPassword: String = "••••••••",
    val jazzcashIntegritySalt: String = "••••••••",
    
    val easypaisaEnabled: Boolean = true,
    val easypaisaStoreId: String = "EP_5521",
    val easypaisaHashKey: String = "••••••••",

    val raastEnabled: Boolean = true,
    val raastIban: String = "PK36MEZN0099320102948101",

    val cardEnabled: Boolean = true,
    val webhookSignatureEnforced: Boolean = true
)

data class SocialLinkItem(
    val platform: String,
    val url: String,
    val isEnabled: Boolean = true
)

data class StoreSettings(
    val storeName: String = "Hamad Hassan Digital Store",
    val whatsappNumber: String = "+92 335 4313211",
    val whatsappDirectLink: String = "https://wa.me/923354313211",
    val currencyCode: String = "PKR",
    val currencySymbol: String = "Rs.",
    val announcement: String = "⚡ 24/7 Automated Delivery Active — Instant login details generated upon verified payment.",
    val is247DeliveryActive: Boolean = true
)

data class CustomDigitalInventory(
    val id: String,
    val productId: String,
    val credentialPayload: String, // e.g. "email:pass" or "activation_key"
    val isAllocated: Boolean = false,
    val allocatedOrderId: String? = null
)
