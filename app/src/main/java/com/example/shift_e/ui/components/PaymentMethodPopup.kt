// PaymentMethodPopup.kt
package com.example.shift_e.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.shift_e.R
import com.example.shift_e.ui.enums.PaymentOption

@Composable
fun PaymentMethodPopup(
    selectedOption: PaymentOption,
    onOptionSelected: (PaymentOption) -> Unit,
    onCardDetails: ()->Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // Full‐screen blur + dim
        Box(
            Modifier
                .matchParentSize()
                .blur(40.dp)
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onDismiss() }
        )

        // Centered Card
        Card(
            shape     = RoundedCornerShape(24.dp),
            elevation = cardElevation(defaultElevation = 8.dp),
            modifier  = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    "Select Payment Method",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(Modifier.height(16.dp))

                PaymentOptionItem(
                    icon = Icons.Default.CreditCard,
                    label = "Credit / Debit Card",
                    isSelected = selectedOption == PaymentOption.CARD,
                    onSelect = { onOptionSelected(PaymentOption.CARD) }
                )

                PaymentOptionItem(
                    icon = Icons.Default.AccountBalanceWallet,
                    label = "Wallet",
                    isSelected = selectedOption == PaymentOption.WALLET,
                    onSelect = { onOptionSelected(PaymentOption.WALLET) }
                )

                PaymentOptionItem(
                    icon       = Icons.Default.Payment,
                    label = "PayPal",
                    isSelected = selectedOption == PaymentOption.PAYPAL,
                    onSelect = { onOptionSelected(PaymentOption.PAYPAL) }
                )
                Spacer(Modifier.height(24.dp))

                Button(
                    onClick  = if (selectedOption == PaymentOption.CARD) onCardDetails else onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val label = if (selectedOption == PaymentOption.CARD) "Card Details" else "Done"
                    Icon(
                        imageVector = if (selectedOption == PaymentOption.CARD)
                            Icons.Default.CreditCard
                        else Icons.Default.Done,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(label)
                }
            }
        }
    }
}

@Composable
private fun PaymentOptionItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val bg = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent

    Row(
        Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(12.dp))
            .clickable { onSelect() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(16.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.weight(1f))
        RadioButton(
            selected = isSelected,
            onClick  = onSelect,
            colors   = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
    }
}
