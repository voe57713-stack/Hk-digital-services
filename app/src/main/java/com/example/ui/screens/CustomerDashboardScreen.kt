package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Order
import com.example.model.OrderStatus
import com.example.ui.theme.*

@Composable
fun CustomerDashboardScreen(
    orders: List<Order>,
    onWhatsAppSupportClick: (orderNumber: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showRegisterModal by remember { mutableStateOf(false) }
    var customerUsername by remember { mutableStateOf("Hamad Customer") }
    var customerEmail by remember { mutableStateOf("customer@hhstore.pk") }
    var customerWhatsapp by remember { mutableStateOf("03001234567") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavy)
            .padding(16.dp)
            .testTag("customer_dashboard_screen")
    ) {
        // Customer Profile Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("customer_profile_card"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkNavy),
            border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(EmeraldSurface, RoundedCornerShape(20.dp))
                                .border(1.dp, EmeraldBorder, RoundedCornerShape(20.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = customerUsername,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "$customerWhatsapp • $customerEmail",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = { showRegisterModal = true },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder),
                        modifier = Modifier.height(34.dp).testTag("customer_edit_profile_button")
                    ) {
                        Text("Profile", fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Orders Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Digital Purchases (${orders.size})",
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Surface(
                color = EmeraldSurface,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Instant 24/7 Access",
                    color = EmeraldLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No orders placed yet",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Select any product from the catalog to get instant automated credentials",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(orders, key = { it.id }) { order ->
                    CustomerOrderItemCard(
                        order = order,
                        onCopyCredentials = { text ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("HH Credentials", text))
                            Toast.makeText(context, "Credentials copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        onWhatsAppHelp = { onWhatsAppSupportClick(order.orderNumber) }
                    )
                }
            }
        }
    }

    // Register / Account Modal
    if (showRegisterModal) {
        var tempUser by remember { mutableStateOf(customerUsername) }
        var tempEmail by remember { mutableStateOf(customerEmail) }
        var tempPhone by remember { mutableStateOf(customerWhatsapp) }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showRegisterModal = false },
            title = { Text("Customer Account Settings", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tempUser,
                        onValueChange = { tempUser = it },
                        label = { Text("Username / Full Name", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempPhone,
                        onValueChange = { tempPhone = it },
                        label = { Text("WhatsApp Number", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("New Password (Optional)", fontSize = 11.sp) },
                        colors = outlinedFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        customerUsername = tempUser
                        customerEmail = tempEmail
                        customerWhatsapp = tempPhone
                        showRegisterModal = false
                        Toast.makeText(context, "Account details saved!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = DeepNavy)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterModal = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DarkNavy
        )
    }
}

@Composable
private fun CustomerOrderItemCard(
    order: Order,
    onCopyCredentials: (String) -> Unit,
    onWhatsAppHelp: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("customer_order_${order.orderNumber}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.productName,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Order #${order.orderNumber} • ${order.paymentGateway.name} (Txn: ${order.paymentTransactionId})",
                        color = TextTertiary,
                        fontSize = 10.sp
                    )
                }

                Surface(
                    color = when (order.status) {
                        OrderStatus.COMPLETED -> EmeraldSurface
                        OrderStatus.PROCESSING -> Color(0x2238BDF8)
                        OrderStatus.PENDING_MANUAL -> Color(0x22F59E0B)
                        OrderStatus.FAILED -> Color(0x22EF4444)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = when (order.status) {
                            OrderStatus.COMPLETED -> "Delivered ⚡"
                            OrderStatus.PROCESSING -> "Processing"
                            OrderStatus.PENDING_MANUAL -> "Pending"
                            OrderStatus.FAILED -> "Failed"
                        },
                        color = when (order.status) {
                            OrderStatus.COMPLETED -> EmeraldLight
                            OrderStatus.PROCESSING -> Color(0xFF38BDF8)
                            OrderStatus.PENDING_MANUAL -> StatusWarning
                            OrderStatus.FAILED -> StatusError
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Credentials details box
            if (!order.deliveryCredentials.isNullOrBlank()) {
                Surface(
                    color = DeepNavy,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Delivered Credentials:",
                            color = EmeraldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = order.deliveryCredentials,
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onCopyCredentials(order.deliveryCredentials) },
                                colors = ButtonDefaults.buttonColors(containerColor = SurfaceNavy, contentColor = EmeraldLight),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Copy", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = onWhatsAppHelp,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = WhatsAppGreen),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x4D25D366)),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Support", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Customer: ${order.customerName} (${order.customerWhatsapp})",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Text(
                    text = "Paid: Rs. ${order.customerPaidPkr.toInt()}",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
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
