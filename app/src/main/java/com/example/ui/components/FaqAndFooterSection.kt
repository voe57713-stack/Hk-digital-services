package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SocialLinkItem
import com.example.ui.theme.*

data class FaqItem(
    val question: String,
    val answer: String
)

@Composable
fun FaqSection(modifier: Modifier = Modifier) {
    val faqs = remember {
        listOf(
            FaqItem(
                question = "How does 24/7 instant delivery work?",
                answer = "Our store is connected directly to automated API delivery and in-house digital vaults. The second your JazzCash, Easypaisa, Raast, or Card payment is verified, credentials are generated and displayed on your screen instantly, even at 3:00 AM."
            ),
            FaqItem(
                question = "What if a purchased account stops working?",
                answer = "All products come with our 100% Replacement Warranty (30 to 365 days depending on the item). Simply tap the WhatsApp button or message us with your Order ID for immediate replacement."
            ),
            FaqItem(
                question = "Which payment methods are accepted?",
                answer = "We support all major payment options in Pakistan: JazzCash Mobile Account, Easypaisa Wallet, Raast Instant Interbank Transfer, and Visa/Mastercard Debit/Credit Cards."
            ),
            FaqItem(
                question = "Are the licenses and subscriptions genuine?",
                answer = "Yes! Software licenses (like Windows 11 Pro and IDM) are genuine lifetime keys. Subscriptions (like CapCut Pro, Netflix, Canva) are private and verified through authorized supplier channels."
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("faq_section")
    ) {
        Text(
            text = "Frequently Asked Questions",
            color = TextPrimary,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Instant answers regarding payment, delivery, and replacement guarantee",
            color = TextSecondary,
            fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        faqs.forEachIndexed { index, faq ->
            FaqAccordionItem(faq = faq, testIndex = index)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FaqAccordionItem(faq: FaqItem, testIndex: Int) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { isExpanded = !isExpanded }
            .testTag("faq_item_$testIndex"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkNavy),
        border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = faq.question,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = EmeraldLight,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = NavyBorder, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = faq.answer,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FooterSection(
    socialLinks: List<SocialLinkItem>,
    onOpenUrl: (String) -> Unit,
    onWhatsAppClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkNavy)
            .border(width = 1.dp, color = NavyBorder, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(20.dp)
            .testTag("store_footer_section")
    ) {
        // Logo & Tagline
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(EmeraldSurface, RoundedCornerShape(6.dp))
                    .border(1.dp, EmeraldBorder, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("HH", fontWeight = FontWeight.Bold, color = EmeraldPrimary, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Hamad Hassan Digital Store",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "Automated Digital Accounts & Subscriptions in PKR",
                    color = TextSecondary,
                    fontSize = 10.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // WhatsApp direct support button in footer
        Button(
            onClick = onWhatsAppClick,
            colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen, contentColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .testTag("footer_whatsapp_button")
        ) {
            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Support WhatsApp: +92 335 4313211", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Social Media Links (Admin Configured)
        Text(
            text = "Connect With Us",
            color = TextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            socialLinks.filter { it.isEnabled }.take(6).forEach { link ->
                Surface(
                    color = SurfaceNavy,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBorderLight),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onOpenUrl(link.url) }
                        .testTag("footer_social_${link.platform.lowercase()}")
                ) {
                    Text(
                        text = link.platform,
                        color = TextPrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = NavyBorder, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "© 2026 Hamad Hassan Digital Store. All rights reserved.\nSupplier operations powered by MakerCC.shop API • 24/7 Automated Fulfillment.",
            color = TextTertiary,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun FloatingWhatsAppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = WhatsAppGreen,
        contentColor = Color.White,
        shape = CircleShape,
        modifier = modifier
            .size(56.dp)
            .testTag("floating_whatsapp_fab")
    ) {
        Icon(
            imageVector = Icons.Default.Chat,
            contentDescription = "WhatsApp Support +92 335 4313211",
            modifier = Modifier.size(28.dp)
        )
    }
}
