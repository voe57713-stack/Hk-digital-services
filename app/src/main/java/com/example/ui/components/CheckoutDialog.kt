package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.window.Dialog
import com.example.model.Order
import com.example.model.PaymentGateway
import com.example.model.Product
import com.example.model.ProductSource
import com.example.ui.theme.*

@Composable
fun CheckoutDialog(
    product: Product,
    isProcessing: Boolean,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onConfirmPayment: (name: String, email: String, whatsapp: String, gateway: PaymentGateway, cnicOrOtp: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var selectedGateway by remember { mutableStateOf(PaymentGateway.JAZZCASH) }
    var cnicOrOtp by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = { if (!isProcessing) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("checkout_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorderLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Direct Checkout",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "24/7 Automated Fulfillment",
                            color = EmeraldLight,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    if (!isProcessing) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Product Summary Card
                Surface(
                    color = SurfaceNavy,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = product.name,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${product.validityPeriod} • ${product.warrantyDays}-day warranty",
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Rs. ${product.retailPricePkr.toInt()}",
                                color = EmeraldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "PKR Net",
                                color = TextTertiary,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Customer Details Fields
                Text(
                    text = "Customer Information",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", fontSize = 12.sp) },
                    singleLine = true,
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("checkout_name_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = { whatsapp = it },
                    label = { Text("WhatsApp Number (e.g. 03001234567)", fontSize = 12.sp) },
                    placeholder = { Text("For credentials copy & support receipt", fontSize = 11.sp) },
                    singleLine = true,
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("checkout_whatsapp_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", fontSize = 12.sp) },
                    singleLine = true,
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("checkout_email_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Select Automated Payment Gateway
                Text(
                    text = "Select Payment Gateway (Pakistan)",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                PaymentGatewayOption(
                    gateway = PaymentGateway.JAZZCASH,
                    title = "JazzCash Mobile Account",
                    subtitle = "Instant MPIN / OTP prompt verification",
                    isSelected = selectedGateway == PaymentGateway.JAZZCASH,
                    onSelect = { selectedGateway = PaymentGateway.JAZZCASH }
                )

                Spacer(modifier = Modifier.height(6.dp))

                PaymentGatewayOption(
                    gateway = PaymentGateway.EASYPAISA,
                    title = "Easypaisa Wallet",
                    subtitle = "Automated prompt & server-to-server check",
                    isSelected = selectedGateway == PaymentGateway.EASYPAISA,
                    onSelect = { selectedGateway = PaymentGateway.EASYPAISA }
                )

                Spacer(modifier = Modifier.height(6.dp))

                PaymentGatewayOption(
                    gateway = PaymentGateway.RAAST,
                    title = "Raast Instant Bank Transfer",
                    subtitle = "0% fee instant interbank transfer",
                    isSelected = selectedGateway == PaymentGateway.RAAST,
                    onSelect = { selectedGateway = PaymentGateway.RAAST }
                )

                Spacer(modifier = Modifier.height(6.dp))

                PaymentGatewayOption(
                    gateway = PaymentGateway.CARD,
                    title = "Debit / Credit Card",
                    subtitle = "Visa / Mastercard 3D Secure",
                    isSelected = selectedGateway == PaymentGateway.CARD,
                    onSelect = { selectedGateway = PaymentGateway.CARD }
                )

                // Gateway specific input (CNIC last 6 digits or OTP prompt note)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = cnicOrOtp,
                    onValueChange = { cnicOrOtp = it },
                    label = {
                        Text(
                            text = if (selectedGateway == PaymentGateway.JAZZCASH || selectedGateway == PaymentGateway.EASYPAISA)
                                "Account Owner CNIC (Last 6 Digits - Optional)"
                            else "Transaction Reference ID / Card Note",
                            fontSize = 11.sp
                        )
                    },
                    singleLine = true,
                    colors = outlinedColors(),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().testTag("checkout_reference_input")
                )

                // Errors
                val activeError = validationError ?: errorMessage
                if (activeError != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = activeError,
                        color = StatusError,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pay Button with spinner
                Button(
                    onClick = {
                        if (name.isBlank() || whatsapp.isBlank() || email.isBlank()) {
                            validationError = "Please enter your Name, WhatsApp number, and Email."
                            return@Button
                        }
                        if (whatsapp.length < 10) {
                            validationError = "Please enter a valid Pakistani WhatsApp number (e.g. 03001234567)."
                            return@Button
                        }
                        validationError = null
                        onConfirmPayment(name, email, whatsapp, selectedGateway, cnicOrOtp)
                    },
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = DeepNavy
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_payment_button")
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            color = DeepNavy,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (product.source == ProductSource.MAKERCC_API)
                                "Verifying Payment & Calling MakerCC..."
                            else "Verifying Payment & Allocating Stock...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pay Rs. ${product.retailPricePkr.toInt()} & Get Credentials",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentGatewayOption(
    gateway: PaymentGateway,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        color = if (isSelected) SurfaceNavy else DeepNavy,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) EmeraldPrimary else NavyBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect() }
            .testTag("payment_option_${gateway.name.lowercase()}")
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = EmeraldPrimary,
                    unselectedColor = TextTertiary
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
fun OrderSuccessDialog(
    order: Order,
    onDismiss: () -> Unit,
    onOpenWhatsAppSupport: (String) -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("order_success_dialog"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Success Badge
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(EmeraldSurface, RoundedCornerShape(24.dp))
                        .border(1.dp, EmeraldBorder, RoundedCornerShape(24.dp))
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Success",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Order Fulfilled Successfully!",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Order #${order.orderNumber} • Paid Rs. ${order.customerPaidPkr.toInt()} (${order.paymentGateway.name})",
                    color = EmeraldLight,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Credentials Box with Copy Action
                Text(
                    text = "Purchased Digital Credentials / License:",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Surface(
                    color = DeepNavy,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = order.deliveryCredentials ?: "Details will be sent to WhatsApp.",
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Copy Button
                        Button(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("HH Credentials", order.deliveryCredentials)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Credentials copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceNavy,
                                contentColor = EmeraldLight
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .testTag("copy_credentials_button")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Login Details", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // WhatsApp Support Help Button
                Button(
                    onClick = { onOpenWhatsAppSupport(order.orderNumber) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("whatsapp_order_help_button")
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Need Help? Open WhatsApp Support", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder),
                    modifier = Modifier.fillMaxWidth().height(40.dp)
                ) {
                    Text("Done / View in My Orders", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun outlinedColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = DeepNavy,
    unfocusedContainerColor = DeepNavy,
    focusedBorderColor = EmeraldPrimary,
    unfocusedBorderColor = NavyBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = EmeraldLight,
    unfocusedLabelColor = TextSecondary
)
