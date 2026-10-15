package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*
import com.example.viewmodel.StoreViewModel

enum class AdminSubTab {
    DASHBOARD_METRICS,
    SEPARATE_ACCOUNTS,
    PRODUCTS_MANAGER,
    SUPPLIER_MAKERCC,
    PAYMENTS_CONFIG,
    REVIEWS_MANAGER,
    WHATSAPP_FEEDBACK,
    SOCIAL_SETTINGS
}

@Composable
fun AdminDashboardScreen(
    viewModel: StoreViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var activeSubTab by remember { mutableStateOf(AdminSubTab.DASHBOARD_METRICS) }

    val products by viewModel.products.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val supplierConfig by viewModel.supplierConfig.collectAsState()
    val paymentConfig by viewModel.paymentConfig.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val feedbacks by viewModel.whatsappFeedbacks.collectAsState()
    val socialLinks by viewModel.socialLinks.collectAsState()
    val supplierTestMsg by viewModel.supplierTestMessage.collectAsState()

    val totalSales by viewModel.totalCustomerSalesPkr.collectAsState()
    val totalSupplierCost by viewModel.totalSupplierCostsPkr.collectAsState()
    val totalGatewayFee by viewModel.totalGatewayFeesPkr.collectAsState()
    val netProfit by viewModel.estimatedNetProfitPkr.collectAsState()
    val totalOrders by viewModel.totalOrdersCount.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavy)
            .testTag("admin_dashboard_screen")
    ) {
        // Admin Top Bar
        Surface(
            color = DarkNavy,
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(EmeraldSurface, RoundedCornerShape(6.dp))
                            .border(1.dp, EmeraldBorder, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Admin Management Console",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Hamad Hassan Digital Store • Secure Mode",
                            color = EmeraldLight,
                            fontSize = 10.sp
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.logoutAdmin() },
                    modifier = Modifier.size(34.dp).testTag("admin_logout_button")
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Exit Admin", tint = StatusError)
                }
            }
        }

        // Sub-tabs navigation row
        ScrollableTabRow(
            selectedTabIndex = activeSubTab.ordinal,
            containerColor = DarkNavy,
            contentColor = EmeraldPrimary,
            edgePadding = 12.dp,
            divider = { Divider(color = NavyBorder, thickness = 0.5.dp) }
        ) {
            AdminSubTab.values().forEach { tab ->
                val tabTitle = when (tab) {
                    AdminSubTab.DASHBOARD_METRICS -> "Metrics"
                    AdminSubTab.SEPARATE_ACCOUNTS -> "Separate Accounts"
                    AdminSubTab.PRODUCTS_MANAGER -> "Products"
                    AdminSubTab.SUPPLIER_MAKERCC -> "MakerCC API"
                    AdminSubTab.PAYMENTS_CONFIG -> "Payments"
                    AdminSubTab.REVIEWS_MANAGER -> "Reviews"
                    AdminSubTab.WHATSAPP_FEEDBACK -> "WhatsApp Proof"
                    AdminSubTab.SOCIAL_SETTINGS -> "Socials"
                }
                Tab(
                    selected = activeSubTab == tab,
                    onClick = { activeSubTab = tab },
                    text = {
                        Text(
                            text = tabTitle,
                            fontSize = 11.sp,
                            fontWeight = if (activeSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                            color = if (activeSubTab == tab) EmeraldLight else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("admin_subtab_${tab.name.lowercase()}")
                )
            }
        }

        // Content Area based on selected sub-tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (activeSubTab) {
                AdminSubTab.DASHBOARD_METRICS -> MetricsSummaryTab(
                    totalOrders = totalOrders,
                    totalSales = totalSales,
                    totalSupplierCost = totalSupplierCost,
                    totalGatewayFee = totalGatewayFee,
                    netProfit = netProfit,
                    supplierBalance = supplierConfig.walletBalancePkr
                )
                AdminSubTab.SEPARATE_ACCOUNTS -> SeparateAccountsTab(
                    orders = orders,
                    supplierConfig = supplierConfig,
                    totalSales = totalSales,
                    totalSupplierCost = totalSupplierCost,
                    totalGatewayFee = totalGatewayFee,
                    netProfit = netProfit
                )
                AdminSubTab.PRODUCTS_MANAGER -> ProductsManagerTab(
                    products = products,
                    onAddProduct = { viewModel.addProduct(it) },
                    onDeleteProduct = { viewModel.deleteProduct(it) },
                    onAddInventoryKey = { pid, key -> viewModel.addCustomInventory(pid, key) }
                )
                AdminSubTab.SUPPLIER_MAKERCC -> SupplierMakerCcTab(
                    config = supplierConfig,
                    testMessage = supplierTestMsg,
                    onTestConnection = { viewModel.testSupplierConnection() },
                    onSaveConfig = { key, endpoint -> viewModel.updateSupplierKey(key, endpoint) }
                )
                AdminSubTab.PAYMENTS_CONFIG -> PaymentsConfigTab(
                    config = paymentConfig,
                    onSave = { viewModel.updatePaymentConfig(it) }
                )
                AdminSubTab.REVIEWS_MANAGER -> ReviewsManagerTab(
                    reviews = reviews,
                    onAddReview = { viewModel.addReview(it) },
                    onToggleApprove = { viewModel.toggleReviewApproval(it) },
                    onDelete = { viewModel.deleteReview(it) }
                )
                AdminSubTab.WHATSAPP_FEEDBACK -> WhatsAppFeedbackManagerTab(
                    feedbacks = feedbacks,
                    onAddFeedback = { viewModel.addWhatsAppFeedback(it) },
                    onTogglePublish = { viewModel.toggleWhatsAppFeedbackPublish(it) },
                    onDelete = { viewModel.deleteWhatsAppFeedback(it) }
                )
                AdminSubTab.SOCIAL_SETTINGS -> SocialSettingsTab(
                    socialLinks = socialLinks,
                    onUpdate = { plat, url, enabled -> viewModel.updateSocialLink(plat, url, enabled) }
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 1. Dashboard Metrics Summary
// -----------------------------------------------------------------------------
@Composable
private fun MetricsSummaryTab(
    totalOrders: Int,
    totalSales: Double,
    totalSupplierCost: Double,
    totalGatewayFee: Double,
    netProfit: Double,
    supplierBalance: Double
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Store Financial & Fulfillment Summary",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Total Orders",
                value = "$totalOrders",
                subtitle = "Completed 24/7",
                color = EmeraldLight,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "MakerCC Wallet",
                value = "Rs. ${supplierBalance.toInt()}",
                subtitle = "Prepaid Balance",
                color = Color(0xFF38BDF8),
                modifier = Modifier.weight(1f)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricCard(
                title = "Customer Sales",
                value = "Rs. ${totalSales.toInt()}",
                subtitle = "Gross Revenue",
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                title = "Supplier Costs",
                value = "Rs. ${totalSupplierCost.toInt()}",
                subtitle = "MakerCC Deductions",
                color = StatusWarning,
                modifier = Modifier.weight(1f)
            )
        }

        MetricCard(
            title = "Estimated Net Profit",
            value = "Rs. ${netProfit.toInt()} PKR",
            subtitle = "Sales minus MakerCC supplier costs and gateway fees (${totalGatewayFee.toInt()} PKR)",
            color = EmeraldPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = subtitle, color = TextTertiary, fontSize = 10.sp)
        }
    }
}

// -----------------------------------------------------------------------------
// 2. Separate Accounts Tab (Prompt Section 5)
// -----------------------------------------------------------------------------
@Composable
private fun SeparateAccountsTab(
    orders: List<Order>,
    supplierConfig: MakerCcConfig,
    totalSales: Double,
    totalSupplierCost: Double,
    totalGatewayFee: Double,
    netProfit: Double
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Dual-Account Ledger & Accounting Separation",
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "Customer payments belong to your merchant account. MakerCC deductions occur separately from your prepaid supplier wallet. Accounts are never combined.",
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )

        // Account 1: Supplier Wallet
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4D38BDF8)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Account 1 — MakerCC Supplier Wallet", color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = NavyBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))
                AccountRow("Available Balance:", "Rs. ${supplierConfig.walletBalancePkr.toInt()} PKR")
                AccountRow("Total Spent on Supplier:", "Rs. ${supplierConfig.totalSpentSupplierPkr.toInt()} PKR")
                AccountRow("Supplier Orders Placed:", "${supplierConfig.totalSupplierOrdersCount}")
                AccountRow("API Endpoint:", supplierConfig.apiEndpoint)
            }
        }

        // Account 2: Store Sales
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storefront, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Account 2 — Hamad Hassan Store Sales", color = EmeraldLight, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = NavyBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))
                AccountRow("Customer Payments Received:", "Rs. ${totalSales.toInt()} PKR")
                AccountRow("Customer Orders Completed:", "${orders.size}")
                AccountRow("Supplier Cost Deducted:", "-Rs. ${totalSupplierCost.toInt()} PKR")
                AccountRow("Payment Gateway Processing Fees:", "-Rs. ${totalGatewayFee.toInt()} PKR")
                Divider(color = NavyBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(4.dp))
                AccountRow("Estimated Net Merchant Profit:", "+Rs. ${netProfit.toInt()} PKR", isHighlight = true)
            }
        }
    }
}

@Composable
private fun AccountRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = TextSecondary, fontSize = 11.sp)
        Text(
            text = value,
            color = if (isHighlight) EmeraldPrimary else TextPrimary,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = 11.sp
        )
    }
}

// -----------------------------------------------------------------------------
// 3. Products Manager Tab (MakerCC + Own Products)
// -----------------------------------------------------------------------------
@Composable
private fun ProductsManagerTab(
    products: List<Product>,
    onAddProduct: (Product) -> Unit,
    onDeleteProduct: (String) -> Unit,
    onAddInventoryKey: (productId: String, keyPayload: String) -> Unit
) {
    val context = LocalContext.current
    var showAddModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Product Catalog (${products.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Button(
                onClick = { showAddModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(34.dp).testTag("add_product_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Product", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(products, key = { it.id }) { product ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavy),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = product.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            IconButton(onClick = { onDeleteProduct(product.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusError, modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(text = "Retail: Rs. ${product.retailPricePkr.toInt()}", color = EmeraldLight, fontSize = 11.sp)
                            Text(text = "Cost: Rs. ${product.supplierCostPkr.toInt()}", color = TextTertiary, fontSize = 11.sp)
                            Text(text = "Source: ${product.source.name}", color = Color(0xFF38BDF8), fontSize = 11.sp)
                            Text(text = "Stock: ${product.stockCount}", color = TextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (showAddModal) {
        var name by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Editing & AI") }
        var retailPrice by remember { mutableStateOf("1000") }
        var supplierCost by remember { mutableStateOf("300") }
        var isOwnProduct by remember { mutableStateOf(false) }
        var inventoryKey by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddModal = false },
            title = { Text("Add Digital Product", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = retailPrice,
                        onValueChange = { retailPrice = it },
                        label = { Text("Customer Retail Selling Price (PKR)", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isOwnProduct,
                            onCheckedChange = { isOwnProduct = it },
                            colors = CheckboxDefaults.colors(checkedColor = EmeraldPrimary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("This is My Own Product (In-House Stock)", color = TextPrimary, fontSize = 11.sp)
                    }
                    if (!isOwnProduct) {
                        OutlinedTextField(
                            value = supplierCost,
                            onValueChange = { supplierCost = it },
                            label = { Text("MakerCC Supplier Cost (PKR)", fontSize = 11.sp) },
                            colors = outlinedFieldColors(),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        OutlinedTextField(
                            value = inventoryKey,
                            onValueChange = { inventoryKey = it },
                            label = { Text("Authorized License Key / Account Credentials", fontSize = 11.sp) },
                            placeholder = { Text("e.g. Serial: XXXX-YYYY-ZZZZ", fontSize = 10.sp) },
                            colors = outlinedFieldColors(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pid = "p_${System.currentTimeMillis()}"
                        val newProduct = Product(
                            id = pid,
                            name = name.ifBlank { "Custom Digital Product" },
                            category = category,
                            description = "Authorized 24/7 digital delivery item.",
                            retailPricePkr = retailPrice.toDoubleOrNull() ?: 1000.0,
                            supplierCostPkr = if (isOwnProduct) 0.0 else (supplierCost.toDoubleOrNull() ?: 300.0),
                            stockCount = 25,
                            source = if (isOwnProduct) ProductSource.MY_OWN_PRODUCT else ProductSource.MAKERCC_API,
                            deliveryMethod = DeliveryMethod.AUTOMATIC
                        )
                        onAddProduct(newProduct)
                        if (isOwnProduct && inventoryKey.isNotBlank()) {
                            onAddInventoryKey(pid, inventoryKey)
                        }
                        showAddModal = false
                        Toast.makeText(context, "Product created successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy)
                ) {
                    Text("Create Product", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddModal = false }) { Text("Cancel", color = TextSecondary) }
            },
            containerColor = DarkNavy
        )
    }
}

// -----------------------------------------------------------------------------
// 4. Supplier MakerCC Settings Tab
// -----------------------------------------------------------------------------
@Composable
private fun SupplierMakerCcTab(
    config: MakerCcConfig,
    testMessage: String?,
    onTestConnection: () -> Unit,
    onSaveConfig: (key: String, endpoint: String) -> Unit
) {
    val context = LocalContext.current
    var endpoint by remember { mutableStateOf(config.apiEndpoint) }
    var apiKey by remember { mutableStateOf(config.apiKey) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("MakerCC Supplier API Integration", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text(
            text = "Official Endpoint: https://makercc.shop/api/v1/index.php\nActions Supported: services, add / add_order, status, balance.",
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 16.sp
        )

        OutlinedTextField(
            value = endpoint,
            onValueChange = { endpoint = it },
            label = { Text("Supplier API Endpoint URL", fontSize = 11.sp) },
            colors = outlinedFieldColors(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("makercc_endpoint_input")
        )

        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("Supplier API Secret Key", fontSize = 11.sp) },
            placeholder = { Text("Paste MakerCC API key here", fontSize = 11.sp) },
            colors = outlinedFieldColors(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("makercc_apikey_input")
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    onSaveConfig(apiKey, endpoint)
                    Toast.makeText(context, "Supplier API config updated!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(38.dp).weight(1f).testTag("save_supplier_config_button")
            ) {
                Text("Save Configuration", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onTestConnection,
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceNavy, contentColor = Color(0xFF38BDF8)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(38.dp).weight(1f).testTag("test_supplier_api_button")
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Test Connection", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (!testMessage.isNullOrBlank()) {
            Surface(
                color = SurfaceNavy,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = testMessage,
                    color = EmeraldLight,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// 5. Payment Gateway Configuration Tab
// -----------------------------------------------------------------------------
@Composable
private fun PaymentsConfigTab(
    config: PaymentGatewayConfig,
    onSave: (PaymentGatewayConfig) -> Unit
) {
    val context = LocalContext.current
    var jazzcashEnabled by remember { mutableStateOf(config.jazzcashEnabled) }
    var jcMerchantId by remember { mutableStateOf(config.jazzcashMerchantId) }
    var easypaisaEnabled by remember { mutableStateOf(config.easypaisaEnabled) }
    var epStoreId by remember { mutableStateOf(config.easypaisaStoreId) }
    var raastEnabled by remember { mutableStateOf(config.raastEnabled) }
    var raastIban by remember { mutableStateOf(config.raastIban) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Pakistan Payment Gateway Configuration", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)

        // JazzCash
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("JazzCash Direct Merchant Gateway", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Switch(
                        checked = jazzcashEnabled,
                        onCheckedChange = { jazzcashEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                    )
                }
                OutlinedTextField(
                    value = jcMerchantId,
                    onValueChange = { jcMerchantId = it },
                    label = { Text("JazzCash Merchant ID", fontSize = 10.sp) },
                    colors = outlinedFieldColors(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Easypaisa
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Easypaisa Merchant Wallet", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Switch(
                        checked = easypaisaEnabled,
                        onCheckedChange = { easypaisaEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                    )
                }
                OutlinedTextField(
                    value = epStoreId,
                    onValueChange = { epStoreId = it },
                    label = { Text("Easypaisa Store ID", fontSize = 10.sp) },
                    colors = outlinedFieldColors(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Raast
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Text("Raast Instant Transfer", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Switch(
                        checked = raastEnabled,
                        onCheckedChange = { raastEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                    )
                }
                OutlinedTextField(
                    value = raastIban,
                    onValueChange = { raastIban = it },
                    label = { Text("Raast Linked IBAN / ID", fontSize = 10.sp) },
                    colors = outlinedFieldColors(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                onSave(
                    config.copy(
                        jazzcashEnabled = jazzcashEnabled,
                        jazzcashMerchantId = jcMerchantId,
                        easypaisaEnabled = easypaisaEnabled,
                        easypaisaStoreId = epStoreId,
                        raastEnabled = raastEnabled,
                        raastIban = raastIban
                    )
                )
                Toast.makeText(context, "Payment gateway settings saved!", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().height(42.dp)
        ) {
            Text("Save Payment Gateways", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

// -----------------------------------------------------------------------------
// 6. Reviews Manager Tab
// -----------------------------------------------------------------------------
@Composable
private fun ReviewsManagerTab(
    reviews: List<CustomerReview>,
    onAddReview: (CustomerReview) -> Unit,
    onToggleApprove: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var showAddModal by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Customer Reviews (${reviews.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Button(
                onClick = { showAddModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Add Review", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(reviews, key = { it.id }) { rev ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavy),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = rev.customerName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Row {
                                TextButton(onClick = { onToggleApprove(rev.id) }) {
                                    Text(if (rev.isApproved) "Hide" else "Approve", color = EmeraldLight, fontSize = 11.sp)
                                }
                                IconButton(onClick = { onDelete(rev.id) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusError, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Text(text = "\"${rev.comment}\"", color = TextSecondary, fontSize = 11.sp)
                    }
                }
            }
        }
    }

    if (showAddModal) {
        var name by remember { mutableStateOf("") }
        var comment by remember { mutableStateOf("") }
        var product by remember { mutableStateOf("CapCut Pro 1-Month") }

        AlertDialog(
            onDismissRequest = { showAddModal = false },
            title = { Text("Add Genuine Review", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Customer Display Name") }, colors = outlinedFieldColors())
                    OutlinedTextField(value = product, onValueChange = { product = it }, label = { Text("Product Purchased") }, colors = outlinedFieldColors())
                    OutlinedTextField(value = comment, onValueChange = { comment = it }, label = { Text("Review Text") }, colors = outlinedFieldColors())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddReview(
                            CustomerReview(
                                id = "rev_${System.currentTimeMillis()}",
                                customerName = name.ifBlank { "Verified Customer" },
                                rating = 5,
                                comment = comment.ifBlank { "Great instant delivery and support!" },
                                purchasedProduct = product,
                                dateFormatted = "Today"
                            )
                        )
                        showAddModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy)
                ) { Text("Publish Review", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showAddModal = false }) { Text("Cancel", color = TextSecondary) } },
            containerColor = DarkNavy
        )
    }
}

// -----------------------------------------------------------------------------
// 7. WhatsApp Feedback Screenshots Manager Tab
// -----------------------------------------------------------------------------
@Composable
private fun WhatsAppFeedbackManagerTab(
    feedbacks: List<WhatsAppFeedback>,
    onAddFeedback: (WhatsAppFeedback) -> Unit,
    onTogglePublish: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    var showAddModal by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("WhatsApp Feedback Proof (${feedbacks.size})", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Button(
                onClick = { showAddModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text("Add Feedback", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(feedbacks, key = { it.id }) { fb ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkNavy),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${fb.customerName} • ${fb.caption}", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Row {
                                TextButton(onClick = { onTogglePublish(fb.id) }) {
                                    Text(if (fb.isPublished) "Unpublish" else "Publish", color = EmeraldLight, fontSize = 11.sp)
                                }
                                IconButton(onClick = { onDelete(fb.id) }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = StatusError, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Text(text = "\"${fb.previewSnippet}\"", color = Color(0xFFE2F7CB), fontSize = 11.sp)
                    }
                }
            }
        }
    }

    if (showAddModal) {
        var name by remember { mutableStateOf("") }
        var caption by remember { mutableStateOf("") }
        var snippet by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddModal = false },
            title = { Text("Add WhatsApp Proof", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Customer Name / Initial") }, colors = outlinedFieldColors())
                    OutlinedTextField(value = caption, onValueChange = { caption = it }, label = { Text("Caption (e.g. CapCut 3 AM)") }, colors = outlinedFieldColors())
                    OutlinedTextField(value = snippet, onValueChange = { snippet = it }, label = { Text("WhatsApp Message Snippet") }, colors = outlinedFieldColors())
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddFeedback(
                            WhatsAppFeedback(
                                id = "fb_${System.currentTimeMillis()}",
                                customerName = name.ifBlank { "Client" },
                                caption = caption.ifBlank { "Verified Instant Delivery" },
                                imageType = "chat_custom",
                                previewSnippet = snippet.ifBlank { "Bhai login ho gaya successfully!" },
                                dateFormatted = "Today",
                                isPublished = true
                            )
                        )
                        showAddModal = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy)
                ) { Text("Save & Publish", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { showAddModal = false }) { Text("Cancel", color = TextSecondary) } },
            containerColor = DarkNavy
        )
    }
}

// -----------------------------------------------------------------------------
// 8. Social Media Links Settings Tab
// -----------------------------------------------------------------------------
@Composable
private fun SocialSettingsTab(
    socialLinks: List<SocialLinkItem>,
    onUpdate: (platform: String, url: String, isEnabled: Boolean) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Social Media Platforms (8 Supported)", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Text("Only enabled platforms with valid URLs will appear on the storefront.", color = TextSecondary, fontSize = 11.sp)

        socialLinks.forEach { item ->
            var currentUrl by remember(item.url) { mutableStateOf(item.url) }
            var isEnabled by remember(item.isEnabled) { mutableStateOf(item.isEnabled) }

            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = DarkNavy),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.platform, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Switch(
                            checked = isEnabled,
                            onCheckedChange = {
                                isEnabled = it
                                onUpdate(item.platform, currentUrl, it)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldPrimary)
                        )
                    }

                    OutlinedTextField(
                        value = currentUrl,
                        onValueChange = {
                            currentUrl = it
                            onUpdate(item.platform, it, isEnabled)
                        },
                        label = { Text("${item.platform} URL", fontSize = 10.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = DeepNavy,
    unfocusedContainerColor = DeepNavy,
    focusedBorderColor = EmeraldPrimary,
    unfocusedBorderColor = NavyBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = EmeraldLight,
    unfocusedLabelColor = TextSecondary
)
