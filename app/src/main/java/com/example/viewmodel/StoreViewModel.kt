package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.StoreRepository
import com.example.model.*
import com.example.network.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreenTab {
    STOREFRONT,
    MY_ORDERS,
    ADMIN_PANEL
}

class StoreViewModel(
    private val repository: StoreRepository = StoreRepository()
) : ViewModel() {

    val products = repository.products
    val orders = repository.orders
    val customInventory = repository.customInventory
    val supplierConfig = repository.supplierConfig
    val paymentConfig = repository.paymentConfig
    val reviews = repository.reviews
    val whatsappFeedbacks = repository.whatsappFeedbacks
    val socialLinks = repository.socialLinks
    val storeSettings = repository.storeSettings

    // Navigation & UI State
    private val _currentTab = MutableStateFlow(AppScreenTab.STOREFRONT)
    val currentTab: StateFlow<AppScreenTab> = _currentTab.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Checkout State
    private val _selectedProductForCheckout = MutableStateFlow<Product?>(null)
    val selectedProductForCheckout: StateFlow<Product?> = _selectedProductForCheckout.asStateFlow()

    private val _isProcessingOrder = MutableStateFlow(false)
    val isProcessingOrder: StateFlow<Boolean> = _isProcessingOrder.asStateFlow()

    private val _orderSuccessResult = MutableStateFlow<Order?>(null)
    val orderSuccessResult: StateFlow<Order?> = _orderSuccessResult.asStateFlow()

    private val _orderErrorMessage = MutableStateFlow<String?>(null)
    val orderErrorMessage: StateFlow<String?> = _orderErrorMessage.asStateFlow()

    // Admin State
    private val _isAdminAuthenticated = MutableStateFlow(false)
    val isAdminAuthenticated: StateFlow<Boolean> = _isAdminAuthenticated.asStateFlow()

    private val _supplierTestMessage = MutableStateFlow<String?>(null)
    val supplierTestMessage: StateFlow<String?> = _supplierTestMessage.asStateFlow()

    // Filtered Products computation
    val filteredProducts: StateFlow<List<Product>> = combine(
        products,
        _searchQuery,
        _selectedCategory
    ) { allProducts, query, category ->
        allProducts.filter { product ->
            val matchesCategory = (category == "All" || product.category.equals(category, ignoreCase = true))
            val matchesQuery = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Metrics for Admin Dashboard Summary
    val totalOrdersCount: StateFlow<Int> = orders.map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalCustomerSalesPkr: StateFlow<Double> = orders.map { list ->
        list.filter { it.status == OrderStatus.COMPLETED }.sumOf { it.customerPaidPkr }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalSupplierCostsPkr: StateFlow<Double> = orders.map { list ->
        list.filter { it.status == OrderStatus.COMPLETED }.sumOf { it.supplierCostPkr }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalGatewayFeesPkr: StateFlow<Double> = orders.map { list ->
        list.filter { it.status == OrderStatus.COMPLETED }.sumOf { it.gatewayFeePkr }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val estimatedNetProfitPkr: StateFlow<Double> = orders.map { list ->
        list.filter { it.status == OrderStatus.COMPLETED }.sumOf { it.netProfitPkr }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun selectTab(tab: AppScreenTab) {
        _currentTab.value = tab
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openCheckout(product: Product) {
        _selectedProductForCheckout.value = product
        _orderErrorMessage.value = null
    }

    fun closeCheckout() {
        _selectedProductForCheckout.value = null
        _orderErrorMessage.value = null
    }

    fun dismissOrderSuccess() {
        _orderSuccessResult.value = null
    }

    fun executeAutomatedCheckout(
        customerName: String,
        customerEmail: String,
        customerWhatsapp: String,
        gateway: PaymentGateway,
        cnicOrOtp: String = ""
    ) {
        val product = _selectedProductForCheckout.value ?: return
        viewModelScope.launch {
            _isProcessingOrder.value = true
            _orderErrorMessage.value = null

            val result = repository.processAutomatedOrder(
                product = product,
                customerName = customerName,
                customerEmail = customerEmail,
                customerWhatsapp = customerWhatsapp,
                paymentGateway = gateway,
                customerCnicOrOtp = cnicOrOtp
            )

            _isProcessingOrder.value = false
            if (result.isSuccess) {
                _orderSuccessResult.value = result.getOrNull()
                _selectedProductForCheckout.value = null
            } else {
                _orderErrorMessage.value = result.exceptionOrNull()?.message ?: "Transaction could not be completed."
            }
        }
    }

    fun authenticateAdmin(pin: String): Boolean {
        if (pin == "1321" || pin == "0335") {
            _isAdminAuthenticated.value = true
            return true
        }
        return false
    }

    fun logoutAdmin() {
        _isAdminAuthenticated.value = false
        _currentTab.value = AppScreenTab.STOREFRONT
    }

    fun testSupplierConnection() {
        viewModelScope.launch {
            _supplierTestMessage.value = "Connecting to MakerCC API..."
            val res = repository.testSupplierConnection()
            when (res) {
                is com.example.network.MakerCcResult.Success -> {
                    _supplierTestMessage.value = "Success! MakerCC Wallet Balance: Rs. ${res.data.balance} (${res.data.currency})"
                }
                is com.example.network.MakerCcResult.Error -> {
                    _supplierTestMessage.value = "Notice: ${res.message}"
                }
            }
        }
    }

    fun updateSupplierKey(key: String, endpoint: String) {
        repository.updateSupplierConfig(key, endpoint)
    }

    fun addProduct(product: Product) {
        repository.addProduct(product)
    }

    fun updateProduct(product: Product) {
        repository.updateProduct(product)
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId)
    }

    fun addCustomInventory(productId: String, payload: String) {
        repository.addCustomInventoryItem(productId, payload)
    }

    fun updatePaymentConfig(config: PaymentGatewayConfig) {
        repository.updatePaymentGatewayConfig(config)
    }

    fun addReview(review: CustomerReview) {
        repository.addReview(review)
    }

    fun deleteReview(reviewId: String) {
        repository.deleteReview(reviewId)
    }

    fun toggleReviewApproval(reviewId: String) {
        repository.toggleReviewApproval(reviewId)
    }

    fun addWhatsAppFeedback(feedback: WhatsAppFeedback) {
        repository.addWhatsAppFeedback(feedback)
    }

    fun deleteWhatsAppFeedback(feedbackId: String) {
        repository.deleteWhatsAppFeedback(feedbackId)
    }

    fun toggleWhatsAppFeedbackPublish(feedbackId: String) {
        repository.toggleWhatsAppFeedbackPublish(feedbackId)
    }

    fun updateSocialLink(platform: String, url: String, isEnabled: Boolean) {
        repository.updateSocialLink(platform, url, isEnabled)
    }

    fun updateStoreSettings(settings: StoreSettings) {
        repository.updateStoreSettings(settings)
    }
}
