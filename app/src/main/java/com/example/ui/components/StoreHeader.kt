package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.AppScreenTab

@Composable
fun StoreHeader(
    currentTab: AppScreenTab,
    onTabSelected: (AppScreenTab) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onWhatsAppClick: () -> Unit,
    onAdminLoginClick: () -> Unit,
    isAdminLoggedIn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkNavy)
            .border(width = 1.dp, color = NavyBorder, shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .padding(top = 8.dp, bottom = 12.dp)
    ) {
        // Top Navigation Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onTabSelected(AppScreenTab.STOREFRONT) }
                    .padding(vertical = 4.dp)
                    .testTag("brand_logo_header")
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(EmeraldSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, EmeraldBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HH",
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        fontSize = 17.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "HAMAD HASSAN",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "DIGITAL STORE • PKR",
                        color = EmeraldLight,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Top Actions: WhatsApp Quick Link, Orders, and Admin Portal
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Live Delivery Badge
                Surface(
                    color = EmeraldSurface,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(EmeraldPrimary)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "24/7 AUTO",
                            color = EmeraldLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // WhatsApp Support Icon
                IconButton(
                    onClick = onWhatsAppClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(SurfaceNavy, CircleShape)
                        .border(1.dp, NavyBorderLight, CircleShape)
                        .testTag("header_whatsapp_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "WhatsApp Support",
                        tint = WhatsAppGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Admin Switch / Login
                IconButton(
                    onClick = onAdminLoginClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(if (isAdminLoggedIn) EmeraldSurface else SurfaceNavy, CircleShape)
                        .border(1.dp, if (isAdminLoggedIn) EmeraldBorder else NavyBorderLight, CircleShape)
                        .testTag("admin_portal_button")
                ) {
                    Icon(
                        imageVector = if (isAdminLoggedIn) Icons.Default.AdminPanelSettings else Icons.Outlined.Lock,
                        contentDescription = "Admin Portal",
                        tint = if (isAdminLoggedIn) EmeraldPrimary else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Tab Switcher: Storefront vs My Orders vs Admin
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(DeepNavy, RoundedCornerShape(10.dp))
                .border(1.dp, NavyBorder, RoundedCornerShape(10.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val tabs = mutableListOf(
                AppScreenTab.STOREFRONT to "Store Catalog",
                AppScreenTab.MY_ORDERS to "My Orders"
            )
            if (isAdminLoggedIn) {
                tabs.add(AppScreenTab.ADMIN_PANEL to "Admin Hub")
            }

            tabs.forEach { (tab, title) ->
                val isSelected = currentTab == tab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) SurfaceNavy else Color.Transparent)
                        .border(
                            width = if (isSelected) 1.dp else 0.dp,
                            color = if (isSelected) EmeraldBorder else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 8.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Storefront Filter Controls (Search + Categories)
        if (currentTab == AppScreenTab.STOREFRONT) {
            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = {
                    Text("Search CapCut, Netflix, Windows 11, VPN...", color = TextTertiary, fontSize = 13.sp)
                },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = TextSecondary, modifier = Modifier.size(18.dp))
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DeepNavy,
                    unfocusedContainerColor = DeepNavy,
                    focusedBorderColor = EmeraldPrimary,
                    unfocusedBorderColor = NavyBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .testTag("product_search_input")
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Categories horizontal chip row
            val categories = listOf("All", "Editing & AI", "Streaming", "Software & OS", "VPN & Security")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isCatSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isCatSelected) EmeraldPrimary else SurfaceNavy)
                            .border(
                                width = 1.dp,
                                color = if (isCatSelected) EmeraldLight else NavyBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { onCategorySelected(cat) }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                            .testTag("category_chip_${cat.replace(" ", "_").lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            color = if (isCatSelected) DeepNavy else TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
