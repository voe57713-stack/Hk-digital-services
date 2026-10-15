package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.*
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.CustomerDashboardScreen
import com.example.ui.theme.DarkNavy
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.AppScreenTab
import com.example.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                HamadHassanStoreApp()
            }
        }
    }
}

@Composable
fun HamadHassanStoreApp(
    viewModel: StoreViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val orders by viewModel.orders.collectAsState()
    val reviews by viewModel.reviews.collectAsState()
    val feedbacks by viewModel.whatsappFeedbacks.collectAsState()
    val socialLinks by viewModel.socialLinks.collectAsState()
    val storeSettings by viewModel.storeSettings.collectAsState()

    val selectedProductForCheckout by viewModel.selectedProductForCheckout.collectAsState()
    val isProcessingOrder by viewModel.isProcessingOrder.collectAsState()
    val orderSuccessResult by viewModel.orderSuccessResult.collectAsState()
    val orderErrorMessage by viewModel.orderErrorMessage.collectAsState()
    val isAdminLoggedIn by viewModel.isAdminAuthenticated.collectAsState()

    var showAdminPinDialog by remember { mutableStateOf(false) }

    // WhatsApp Launcher helper
    val openWhatsApp: (String?) -> Unit = { message ->
        val textParam = if (!message.isNullOrBlank()) {
            Uri.encode("Hello Hamad Hassan Store, I need assistance with Order #$message")
        } else {
            Uri.encode("Hello Hamad Hassan Store, I want to inquire about available digital subscriptions.")
        }
        val url = "https://wa.me/923354313211?text=$textParam"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp link: https://wa.me/923354313211", Toast.LENGTH_LONG).show()
        }
    }

    // Generic URL Launcher helper
    val openUrl: (String) -> Unit = { url ->
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening $url", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle Android system back button
    BackHandler(enabled = currentTab != AppScreenTab.STOREFRONT) {
        viewModel.selectTab(AppScreenTab.STOREFRONT)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy),
        contentWindowInsets = WindowInsets.safeDrawing,
        floatingActionButton = {
            if (currentTab != AppScreenTab.ADMIN_PANEL) {
                FloatingWhatsAppButton(
                    onClick = { openWhatsApp(null) },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepNavy)
        ) {
            // Top Persistent Brand Header with Search & Navigation
            StoreHeader(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) },
                searchQuery = searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.setCategory(it) },
                onWhatsAppClick = { openWhatsApp(null) },
                onAdminLoginClick = {
                    if (isAdminLoggedIn) {
                        viewModel.selectTab(AppScreenTab.ADMIN_PANEL)
                    } else {
                        showAdminPinDialog = true
                    }
                },
                isAdminLoggedIn = isAdminLoggedIn
            )

            // Main Content Area
            when (currentTab) {
                AppScreenTab.STOREFRONT -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(DeepNavy)
                            .testTag("storefront_catalog_scroll")
                    ) {
                        // Hero Section
                        item(key = "hero_section") {
                            HeroSection(onWhatsAppSupportClick = { openWhatsApp(null) })
                        }

                        // Section Heading
                        item(key = "catalog_heading") {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedCategory == "All") "Available Products (${filteredProducts.size})" else "$selectedCategory (${filteredProducts.size})",
                                    color = TextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Prices in PKR",
                                    color = EmeraldLight,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Product Cards List
                        if (filteredProducts.isEmpty()) {
                            item(key = "empty_catalog") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No products found matching \"$searchQuery\"",
                                        color = TextSecondary,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        } else {
                            items(filteredProducts, key = { it.id }) { product ->
                                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                    ProductCard(
                                        product = product,
                                        onBuyClick = { viewModel.openCheckout(it) },
                                        isAdminMode = isAdminLoggedIn
                                    )
                                }
                            }
                        }

                        // Customer Reviews Section
                        item(key = "reviews_section") {
                            Spacer(modifier = Modifier.height(10.dp))
                            CustomerReviewsSection(reviews = reviews)
                        }

                        // WhatsApp Feedback Screenshots Gallery
                        item(key = "feedback_gallery_section") {
                            Spacer(modifier = Modifier.height(10.dp))
                            WhatsAppFeedbackSection(feedbacks = feedbacks)
                        }

                        // FAQ Accordion
                        item(key = "faq_section") {
                            Spacer(modifier = Modifier.height(10.dp))
                            FaqSection()
                        }

                        // Footer Section
                        item(key = "footer_section") {
                            Spacer(modifier = Modifier.height(16.dp))
                            FooterSection(
                                socialLinks = socialLinks,
                                onOpenUrl = openUrl,
                                onWhatsAppClick = { openWhatsApp(null) }
                            )
                            Spacer(modifier = Modifier.height(60.dp)) // Clearance for FAB
                        }
                    }
                }

                AppScreenTab.MY_ORDERS -> {
                    CustomerDashboardScreen(
                        orders = orders,
                        onWhatsAppSupportClick = { orderNumber -> openWhatsApp(orderNumber) }
                    )
                }

                AppScreenTab.ADMIN_PANEL -> {
                    AdminDashboardScreen(viewModel = viewModel)
                }
            }
        }
    }

    // Checkout Modal
    selectedProductForCheckout?.let { product ->
        CheckoutDialog(
            product = product,
            isProcessing = isProcessingOrder,
            errorMessage = orderErrorMessage,
            onDismiss = { viewModel.closeCheckout() },
            onConfirmPayment = { name, email, whatsapp, gateway, cnicOrOtp ->
                viewModel.executeAutomatedCheckout(
                    customerName = name,
                    customerEmail = email,
                    customerWhatsapp = whatsapp,
                    gateway = gateway,
                    cnicOrOtp = cnicOrOtp
                )
            }
        )
    }

    // Order Success Dialog with Delivered Credentials
    orderSuccessResult?.let { completedOrder ->
        OrderSuccessDialog(
            order = completedOrder,
            onDismiss = { viewModel.dismissOrderSuccess() },
            onOpenWhatsAppSupport = { orderNum -> openWhatsApp(orderNum) }
        )
    }

    // Admin Access PIN Modal
    if (showAdminPinDialog) {
        var pinInput by remember { mutableStateOf("") }
        var pinError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAdminPinDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Store Admin Authentication", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column {
                    Text(
                        text = "Enter your secret Admin PIN (Hint: 1321 or 0335 from support number):",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = {
                            pinInput = it
                            pinError = false
                        },
                        label = { Text("Admin PIN") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DeepNavy,
                            unfocusedContainerColor = DeepNavy,
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = NavyBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().testTag("admin_pin_input")
                    )
                    if (pinError) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Incorrect PIN. Please try 1321.", color = MaterialTheme.colorScheme.error, fontSize = 11.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val ok = viewModel.authenticateAdmin(pinInput.trim())
                        if (ok) {
                            showAdminPinDialog = false
                            viewModel.selectTab(AppScreenTab.ADMIN_PANEL)
                            Toast.makeText(context, "Admin console unlocked!", Toast.LENGTH_SHORT).show()
                        } else {
                            pinError = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.testTag("admin_pin_submit_button")
                ) {
                    Text("Unlock Console", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAdminPinDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkNavy
        )
    }
}
