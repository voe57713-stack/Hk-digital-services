package com.example.data

import com.example.model.*
import com.example.network.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class StoreRepository(
    private val makerCcClient: MakerCcClient = MakerCcClient(),
    private val paymentGatewayService: PaymentGatewayService = PaymentGatewayService()
) {
    // Products State
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    // Orders State
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Custom Inventory State (For own products)
    private val _customInventory = MutableStateFlow<List<CustomDigitalInventory>>(emptyList())
    val customInventory: StateFlow<List<CustomDigitalInventory>> = _customInventory.asStateFlow()

    // Supplier Config State (Account 1 - MakerCC)
    private val _supplierConfig = MutableStateFlow(MakerCcConfig())
    val supplierConfig: StateFlow<MakerCcConfig> = _supplierConfig.asStateFlow()

    // Payment Gateway Config
    private val _paymentConfig = MutableStateFlow(PaymentGatewayConfig())
    val paymentConfig: StateFlow<PaymentGatewayConfig> = _paymentConfig.asStateFlow()

    // Customer Reviews State
    private val _reviews = MutableStateFlow<List<CustomerReview>>(emptyList())
    val reviews: StateFlow<List<CustomerReview>> = _reviews.asStateFlow()

    // WhatsApp Feedback Screenshots State
    private val _whatsappFeedbacks = MutableStateFlow<List<WhatsAppFeedback>>(emptyList())
    val whatsappFeedbacks: StateFlow<List<WhatsAppFeedback>> = _whatsappFeedbacks.asStateFlow()

    // Social Links State
    private val _socialLinks = MutableStateFlow<List<SocialLinkItem>>(emptyList())
    val socialLinks: StateFlow<List<SocialLinkItem>> = _socialLinks.asStateFlow()

    // Store Settings
    private val _storeSettings = MutableStateFlow(StoreSettings())
    val storeSettings: StateFlow<StoreSettings> = _storeSettings.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        // Initial Products - MakerCC API + Own Custom Products
        _products.value = listOf(
            Product(
                id = "p_capcut",
                name = "CapCut Pro 1-Month Subscription",
                category = "Editing & AI",
                description = "Full access to VIP features, 4K 60fps export, AI smart cut, cloud storage & premium transitions. Private login details delivered instantly.",
                retailPricePkr = 1000.0,
                supplierCostPkr = 310.0,
                stockCount = 48,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "capcut_pro_monthly",
                validityPeriod = "30 Days",
                warrantyDays = 30
            ),
            Product(
                id = "p_netflix",
                name = "Netflix 4K UHD Profile (Private PIN)",
                category = "Streaming",
                description = "Dedicated private screen profile with personal 4-digit PIN. Ultra HD 4K streaming on mobile, TV, or PC with full replacement guarantee.",
                retailPricePkr = 850.0,
                supplierCostPkr = 380.0,
                stockCount = 35,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "netflix_uhd_pin",
                validityPeriod = "1 Month",
                warrantyDays = 30
            ),
            Product(
                id = "p_canva",
                name = "Canva Pro Lifetime Brand Team Invite",
                category = "Editing & AI",
                description = "Lifetime access to 100M+ premium stock photos, magic studio tools, background remover, brand kits, and unlimited cloud assets on your own email.",
                retailPricePkr = 450.0,
                supplierCostPkr = 120.0,
                stockCount = 85,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "canva_lifetime_invite",
                validityPeriod = "Lifetime",
                warrantyDays = 365
            ),
            Product(
                id = "p_win11",
                name = "Windows 11 Pro Official Retail License Key",
                category = "Software & OS",
                description = "100% Genuine Microsoft digital product key for 1 PC. Lifetime activation with direct Microsoft updates support and online binding.",
                retailPricePkr = 1250.0,
                supplierCostPkr = 0.0, // My Own Product: Zero supplier cost!
                stockCount = 20,
                source = ProductSource.MY_OWN_PRODUCT,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                validityPeriod = "Lifetime",
                warrantyDays = 365
            ),
            Product(
                id = "p_nordvpn",
                name = "NordVPN Premium 1-Year Account",
                category = "VPN & Security",
                description = "High-speed encryption, CyberSec ad blocker, double VPN, dedicated P2P servers in 60+ countries. Works flawlessly across all Pakistani ISPs.",
                retailPricePkr = 1800.0,
                supplierCostPkr = 650.0,
                stockCount = 22,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "nordvpn_1yr",
                validityPeriod = "12 Months",
                warrantyDays = 90
            ),
            Product(
                id = "p_chatgpt",
                name = "ChatGPT Plus (GPT-4o & DALL-E 3) Private",
                category = "Editing & AI",
                description = "Direct private account login with GPT-4o high reasoning, file upload, custom GPTs, code execution, and priority speed during peak hours.",
                retailPricePkr = 2200.0,
                supplierCostPkr = 1100.0,
                stockCount = 14,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "chatgpt_plus_monthly",
                validityPeriod = "1 Month",
                warrantyDays = 30
            ),
            Product(
                id = "p_idm",
                name = "Internet Download Manager (IDM) Lifetime Key",
                category = "Software & OS",
                description = "Official lifetime license key for IDM. Maximize download speeds up to 5x, resume broken downloads, and capture browser video streams automatically.",
                retailPricePkr = 950.0,
                supplierCostPkr = 0.0, // My Own Product
                stockCount = 18,
                source = ProductSource.MY_OWN_PRODUCT,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                validityPeriod = "Lifetime",
                warrantyDays = 180
            ),
            Product(
                id = "p_yt_prem",
                name = "YouTube Premium + Music 3-Month Plan",
                category = "Streaming",
                description = "Ad-free YouTube videos, background playback while phone screen is off, full YouTube Music library, and offline video downloads.",
                retailPricePkr = 750.0,
                supplierCostPkr = 290.0,
                stockCount = 40,
                source = ProductSource.MAKERCC_API,
                deliveryMethod = DeliveryMethod.AUTOMATIC,
                supplierServiceId = "yt_premium_3mo",
                validityPeriod = "3 Months",
                warrantyDays = 90
            )
        )

        // Custom Inventory for My Own Products
        _customInventory.value = listOf(
            CustomDigitalInventory(
                id = "inv_win1",
                productId = "p_win11",
                credentialPayload = "Windows 11 Pro Product Key: 4XN82-M67KQ-9H3YV-BDGF9-T4R8P\nActivation: Open Settings > System > Activation > Change Product Key."
            ),
            CustomDigitalInventory(
                id = "inv_win2",
                productId = "p_win11",
                credentialPayload = "Windows 11 Pro Product Key: 7V29K-NK24J-7R3F6-R3Q87-TY7PQ\nActivation: Open Settings > System > Activation > Change Product Key."
            ),
            CustomDigitalInventory(
                id = "inv_idm1",
                productId = "p_idm",
                credentialPayload = "IDM License Serial: 87GH6-YTR43-PO982-LKM12-QW456\nRegistered Name: Hamad Hassan Customer\nEmail: Customer Provided"
            ),
            CustomDigitalInventory(
                id = "inv_idm2",
                productId = "p_idm",
                credentialPayload = "IDM License Serial: 32VC4-NM981-ZX765-AS432-LK890\nRegistered Name: Hamad Hassan Customer\nEmail: Customer Provided"
            )
        )

        // Seed Orders demonstrating both MakerCC and Own Product sales
        _orders.value = listOf(
            Order(
                id = "ord_001",
                orderNumber = "HH-89241",
                customerName = "Muhammad Usman",
                customerEmail = "usman.dev@gmail.com",
                customerWhatsapp = "03001234567",
                productId = "p_capcut",
                productName = "CapCut Pro 1-Month Subscription",
                productSource = ProductSource.MAKERCC_API,
                quantity = 1,
                customerPaidPkr = 1000.0,
                paymentGateway = PaymentGateway.JAZZCASH,
                paymentTransactionId = "JC_98124578",
                gatewayFeePkr = 18.0,
                supplierCostPkr = 310.0,
                supplierOrderId = "MK-782194",
                grossProfitPkr = 690.0,
                netProfitPkr = 672.0,
                status = OrderStatus.COMPLETED,
                deliveryCredentials = "Account: capcut.pro.vip82@digitalpro.pk\nPassword: HH_82!CapCut\nPlan: Pro 1-Month Private\nProfile PIN: 4313",
                createdAt = System.currentTimeMillis() - 7200000
            ),
            Order(
                id = "ord_002",
                orderNumber = "HH-89242",
                customerName = "Bilal Tariq",
                customerEmail = "bilal.tariq@yahoo.com",
                customerWhatsapp = "03218765432",
                productId = "p_win11",
                productName = "Windows 11 Pro Official Retail License Key",
                productSource = ProductSource.MY_OWN_PRODUCT,
                quantity = 1,
                customerPaidPkr = 1250.0,
                paymentGateway = PaymentGateway.EASYPAISA,
                paymentTransactionId = "EP_34192084",
                gatewayFeePkr = 21.25,
                supplierCostPkr = 0.0, // In-house product
                supplierOrderId = null,
                grossProfitPkr = 1250.0,
                netProfitPkr = 1228.75,
                status = OrderStatus.COMPLETED,
                deliveryCredentials = "Windows 11 Pro Product Key: 4XN82-M67KQ-9H3YV-BDGF9-T4R8P\nActivation: Open Settings > System > Activation > Change Product Key.",
                createdAt = System.currentTimeMillis() - 3600000
            )
        )

        // Customer Reviews
        _reviews.value = listOf(
            CustomerReview(
                id = "rev_1",
                customerName = "Zubair Ahmad (Lahore)",
                rating = 5,
                comment = "Ordered CapCut Pro at 2:30 AM and got login credentials on screen within 10 seconds of JazzCash confirmation! Flawless automated delivery.",
                purchasedProduct = "CapCut Pro 1-Month",
                dateFormatted = "Yesterday"
            ),
            CustomerReview(
                id = "rev_2",
                customerName = "Hamza Farooq (Karachi)",
                rating = 5,
                comment = "Windows 11 Pro key activated directly on Microsoft servers without any error. Highly recommended for genuine licenses in Pakistan.",
                purchasedProduct = "Windows 11 Pro Retail Key",
                dateFormatted = "2 days ago"
            ),
            CustomerReview(
                id = "rev_3",
                customerName = "Khurram Shahzad (Islamabad)",
                rating = 5,
                comment = "Netflix 4K UHD private PIN screen working perfectly. Contacted Hamad on WhatsApp for question and received reply immediately.",
                purchasedProduct = "Netflix 4K UHD Screen",
                dateFormatted = "4 days ago"
            ),
            CustomerReview(
                id = "rev_4",
                customerName = "Ayesha Malik (Faisalabad)",
                rating = 5,
                comment = "Canva Pro lifetime team invite joined smoothly with my own Gmail. Amazing price compared to official $120/year.",
                purchasedProduct = "Canva Pro Team Invite",
                dateFormatted = "Last week"
            )
        )

        // WhatsApp Feedback Screenshots
        _whatsappFeedbacks.value = listOf(
            WhatsAppFeedback(
                id = "fb_1",
                customerName = "Ali R.",
                caption = "Customer confirming CapCut Pro instant delivery at 3 AM",
                imageType = "capcut_delivery",
                previewSnippet = "Bhai CapCut login ho gaya hai 10 second me shukriya! Best service in Pakistan.",
                dateFormatted = "Oct 12, 2026",
                displayOrder = 1,
                isObscuredForPrivacy = true
            ),
            WhatsAppFeedback(
                id = "fb_2",
                customerName = "Saad K.",
                caption = "Customer praise for Netflix UHD 4K profile PIN",
                imageType = "netflix_profile",
                previewSnippet = "Ultra HD 4K running smoothly on TV. Thank you Hamad bhai!",
                dateFormatted = "Oct 10, 2026",
                displayOrder = 2,
                isObscuredForPrivacy = true
            ),
            WhatsAppFeedback(
                id = "fb_3",
                customerName = "Danial M.",
                caption = "NordVPN 1 Year connection verified on PTCL Fiber",
                imageType = "vpn_success",
                previewSnippet = "NordVPN connect ho gaya with 100Mbps speed on fiber. 10/10.",
                dateFormatted = "Oct 8, 2026",
                displayOrder = 3,
                isObscuredForPrivacy = true
            ),
            WhatsAppFeedback(
                id = "fb_4",
                customerName = "Omer F.",
                caption = "Windows 11 genuine retail activation confirmation",
                imageType = "win11_active",
                previewSnippet = "Windows activated permanently via Microsoft server. Zabardast!",
                dateFormatted = "Oct 5, 2026",
                displayOrder = 4,
                isObscuredForPrivacy = true
            )
        )

        // Social Media Links (Admin Configurable)
        _socialLinks.value = listOf(
            SocialLinkItem("WhatsApp", "https://wa.me/923354313211", isEnabled = true),
            SocialLinkItem("Facebook", "https://facebook.com/HamadHassanStore", isEnabled = true),
            SocialLinkItem("Instagram", "https://instagram.com/HamadHassanStore", isEnabled = true),
            SocialLinkItem("TikTok", "https://tiktok.com/@HamadHassanDigital", isEnabled = true),
            SocialLinkItem("YouTube", "https://youtube.com/@HamadHassanStore", isEnabled = true),
            SocialLinkItem("Telegram", "https://t.me/HamadHassanStore", isEnabled = true),
            SocialLinkItem("X / Twitter", "https://x.com/HamadHassanPk", isEnabled = false),
            SocialLinkItem("Pinterest", "https://pinterest.com/HamadHassanStore", isEnabled = false)
        )
    }

    // =========================================================================
    // Core 24/7 Automated Order Processing
    // =========================================================================
    suspend fun processAutomatedOrder(
        product: Product,
        customerName: String,
        customerEmail: String,
        customerWhatsapp: String,
        paymentGateway: PaymentGateway,
        customerCnicOrOtp: String = ""
    ): Result<Order> {
        // Step 1: Verify customer payment through trusted signed verification
        val verification = paymentGatewayService.verifyPayment(
            gateway = paymentGateway,
            amountPkr = product.retailPricePkr,
            customerPhone = customerWhatsapp,
            customerCnicOrOtp = customerCnicOrOtp
        )

        if (!verification.isSuccess) {
            return Result.failure(Exception(verification.failureReason ?: "Payment verification failed."))
        }

        // Step 2: Route fulfillment based on Product Source
        val deliveredCredentials: String
        val supplierOrderId: String?
        val actualSupplierCost: Double

        if (product.source == ProductSource.MAKERCC_API) {
            // Check supplier stock and wallet balance first
            val currentSupplierBalance = _supplierConfig.value.walletBalancePkr
            if (currentSupplierBalance < product.supplierCostPkr) {
                return Result.failure(Exception("Supplier wallet balance insufficient. Please contact store support on WhatsApp."))
            }

            // Call MakerCC API
            val makerCcResult = makerCcClient.submitOrder(
                endpoint = _supplierConfig.value.apiEndpoint,
                apiKey = _supplierConfig.value.apiKey,
                serviceId = product.supplierServiceId ?: product.id,
                linkOrTarget = customerEmail
            )

            when (makerCcResult) {
                is MakerCcResult.Success -> {
                    deliveredCredentials = makerCcResult.data.deliveryCredentials
                        ?: "Account details will be sent to your WhatsApp (${customerWhatsapp}) momentarily."
                    supplierOrderId = makerCcResult.data.orderId
                    actualSupplierCost = product.supplierCostPkr

                    // Update Supplier Wallet (Account 1)
                    val updatedConfig = _supplierConfig.value.copy(
                        walletBalancePkr = _supplierConfig.value.walletBalancePkr - actualSupplierCost,
                        totalSpentSupplierPkr = _supplierConfig.value.totalSpentSupplierPkr + actualSupplierCost,
                        totalSupplierOrdersCount = _supplierConfig.value.totalSupplierOrdersCount + 1
                    )
                    _supplierConfig.value = updatedConfig
                }
                is MakerCcResult.Error -> {
                    return Result.failure(Exception("Supplier fulfillment error: ${makerCcResult.message}"))
                }
            }
        } else {
            // MY_OWN_PRODUCT: Never call MakerCC or deduct from supplier wallet!
            actualSupplierCost = 0.0
            supplierOrderId = null

            // Allocate from custom digital inventory pool
            val availableItem = _customInventory.value.firstOrNull {
                it.productId == product.id && !it.isAllocated
            }

            if (availableItem != null) {
                deliveredCredentials = availableItem.credentialPayload
                // Mark item as allocated
                _customInventory.value = _customInventory.value.map {
                    if (it.id == availableItem.id) it.copy(isAllocated = true) else it
                }
            } else {
                deliveredCredentials = "Product: ${product.name}\nLicense assigned by Hamad Hassan Admin.\nSupport WhatsApp: ${_storeSettings.value.whatsappNumber}"
            }
        }

        // Calculate Accounting
        val customerPaid = product.retailPricePkr
        val grossProfit = customerPaid - actualSupplierCost
        val netProfit = grossProfit - verification.gatewayFeePkr

        val newOrder = Order(
            id = "ord_${System.currentTimeMillis()}",
            orderNumber = "HH-${(10000..99999).random()}",
            customerName = customerName,
            customerEmail = customerEmail,
            customerWhatsapp = customerWhatsapp,
            productId = product.id,
            productName = product.name,
            productSource = product.source,
            quantity = 1,
            customerPaidPkr = customerPaid,
            paymentGateway = paymentGateway,
            paymentTransactionId = verification.transactionId,
            gatewayFeePkr = verification.gatewayFeePkr,
            supplierCostPkr = actualSupplierCost,
            supplierOrderId = supplierOrderId,
            grossProfitPkr = grossProfit,
            netProfitPkr = netProfit,
            status = OrderStatus.COMPLETED,
            deliveryCredentials = deliveredCredentials,
            createdAt = System.currentTimeMillis()
        )

        // Prepend to orders list
        _orders.value = listOf(newOrder) + _orders.value

        // Decrement product stock
        _products.value = _products.value.map {
            if (it.id == product.id && it.stockCount > 0) {
                it.copy(stockCount = it.stockCount - 1)
            } else it
        }

        return Result.success(newOrder)
    }

    // =========================================================================
    // Admin Actions
    // =========================================================================
    fun addProduct(product: Product) {
        _products.value = listOf(product) + _products.value
    }

    fun updateProduct(product: Product) {
        _products.value = _products.value.map { if (it.id == product.id) product else it }
    }

    fun deleteProduct(productId: String) {
        _products.value = _products.value.filterNot { it.id == productId }
    }

    fun addCustomInventoryItem(productId: String, payload: String) {
        val item = CustomDigitalInventory(
            id = "inv_${System.currentTimeMillis()}",
            productId = productId,
            credentialPayload = payload
        )
        _customInventory.value = _customInventory.value + item
    }

    fun updateSupplierConfig(apiKey: String, endpoint: String = "https://makercc.shop/api/v1/index.php") {
        _supplierConfig.value = _supplierConfig.value.copy(
            apiKey = apiKey,
            apiEndpoint = endpoint
        )
    }

    suspend fun testSupplierConnection(): MakerCcResult<MakerCcBalance> {
        val res = makerCcClient.checkBalance(
            _supplierConfig.value.apiEndpoint,
            _supplierConfig.value.apiKey
        )
        if (res is MakerCcResult.Success) {
            val nowStr = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())
            _supplierConfig.value = _supplierConfig.value.copy(
                walletBalancePkr = res.data.balance,
                isConnected = true,
                lastSyncTime = "Today, $nowStr"
            )
        }
        return res
    }

    fun updatePaymentGatewayConfig(config: PaymentGatewayConfig) {
        _paymentConfig.value = config
    }

    fun addReview(review: CustomerReview) {
        _reviews.value = listOf(review) + _reviews.value
    }

    fun deleteReview(reviewId: String) {
        _reviews.value = _reviews.value.filterNot { it.id == reviewId }
    }

    fun toggleReviewApproval(reviewId: String) {
        _reviews.value = _reviews.value.map {
            if (it.id == reviewId) it.copy(isApproved = !it.isApproved) else it
        }
    }

    fun addWhatsAppFeedback(feedback: WhatsAppFeedback) {
        _whatsappFeedbacks.value = listOf(feedback) + _whatsappFeedbacks.value
    }

    fun deleteWhatsAppFeedback(feedbackId: String) {
        _whatsappFeedbacks.value = _whatsappFeedbacks.value.filterNot { it.id == feedbackId }
    }

    fun toggleWhatsAppFeedbackPublish(feedbackId: String) {
        _whatsappFeedbacks.value = _whatsappFeedbacks.value.map {
            if (it.id == feedbackId) it.copy(isPublished = !it.isPublished) else it
        }
    }

    fun updateSocialLink(platform: String, url: String, isEnabled: Boolean) {
        _socialLinks.value = _socialLinks.value.map {
            if (it.platform == platform) it.copy(url = url, isEnabled = isEnabled) else it
        }
    }

    fun updateStoreSettings(settings: StoreSettings) {
        _storeSettings.value = settings
    }
}
