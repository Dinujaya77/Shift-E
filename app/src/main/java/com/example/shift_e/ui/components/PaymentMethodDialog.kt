package com.example.shift_e.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shift_e.ui.enums.PaymentOption

@Composable
fun PaymentMethodDialog(
    selectedOption: PaymentOption,
    onOptionSelected: (PaymentOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(16.dp),

        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "How would you like to pay?",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(PaymentOption.CARD) }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == PaymentOption.CARD),
                        onClick = { onOptionSelected(PaymentOption.CARD) }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Credit or Debit Card",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(PaymentOption.CASH) }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == PaymentOption.CASH),
                        onClick = { onOptionSelected(PaymentOption.CASH) }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Cash",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(PaymentOption.PAYPAL) }
                        .padding(vertical = 8.dp)
                ) {
                    RadioButton(
                        selected = (selectedOption == PaymentOption.PAYPAL),
                        onClick = { onOptionSelected(PaymentOption.PAYPAL) }
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "PayPal",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
