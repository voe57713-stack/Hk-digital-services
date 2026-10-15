package com.example.model

enum class ProductSource {
    MAKERCC_API,
    MY_OWN_PRODUCT
}

enum class DeliveryMethod {
    AUTOMATIC,
    MANUAL
}

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val retailPricePkr: Double,       // My retail selling price in PKR
    val supplierCostPkr: Double,      // Cost deducted by MakerCC (or 0.0 for own product)
    val stockCount: Int,
    val source: ProductSource,
    val deliveryMethod: DeliveryMethod,
    val isActive: Boolean = true,
    val supplierServiceId: String? = null, // MakerCC service ID if linked
    val imageUrl: String? = null,
    val validityPeriod: String = "1 Month",
    val warrantyDays: Int = 30
) {
    val estimatedProfitPkr: Double
        get() = if (source == ProductSource.MAKERCC_API) {
            retailPricePkr - supplierCostPkr
        } else {
            retailPricePkr
        }
}
