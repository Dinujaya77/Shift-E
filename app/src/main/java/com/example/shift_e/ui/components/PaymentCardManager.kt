// PaymentCardManager.kt
@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shift_e.ui.components

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shift_e.ui.theme.GreenPrimary
import java.util.*

// -- Models --

enum class CardType(val displayName: String, val icon: ImageVector) {
    AMEX("AMEX", Icons.Default.CreditCard),
    VISA_MASTER("Visa/MasterCard", Icons.Default.CreditCard)
}

data class CardInfo(
    val id: String = UUID.randomUUID().toString(),
    val type: CardType,
    val name: String,
    val number: String,
    val month: String,
    val year: String,
    val pin: String,
    val nickname: String,
    val saveCard: Boolean
)
class CreditCardVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // keep only digits, max 16
        val digits = text.text.filter(Char::isDigit).take(16)
        // build grouped string
        val grouped = buildString {
            digits.chunked(4).forEachIndexed { i, chunk ->
                append(chunk)
                if (i < 3 && chunk.length == 4) append(' ')
            }
        }
        // offset mapper (cursor logic)
        val translator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val o = offset.coerceIn(0, digits.length)
                val spacesBefore = o / 4
                return (o + spacesBefore).coerceAtMost(grouped.length)
            }
            override fun transformedToOriginal(offset: Int): Int {
                val s = (offset / 5).coerceAtMost(3)
                return (offset - s).coerceAtMost(digits.length)
            }
        }
        return TransformedText(AnnotatedString(grouped), translator)
    }
}

// -- Main Component --

@Composable
fun PaymentCardManager(
    cards: List<CardInfo>,
    selectedCardId: String?,
    onCardSelected: (String) -> Unit,
    onAddCard: (CardInfo) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showForm by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Box(
            Modifier
                .matchParentSize()
                .blur(20.dp)
                .background(Color.Black.copy(alpha = 0.6f))
                .clickable { onDismiss() }
        )

        Card(
            shape     = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier  = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .heightIn(min = 400.dp, max = 650.dp)
        ) {
            Column(Modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = if (showForm) "Add New Card" else "Manage Cards",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(16.dp))

                if (showForm) {
                    AddCardFormContent(
                        onSave   = { newCard -> onAddCard(newCard); showForm = false },
                        onCancel = { showForm = false }
                    )
                } else {
                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(cards) { card ->
                            CardRow(
                                card       = card,
                                isSelected = (card.id == selectedCardId),
                                onClick    = { onCardSelected(card.id) }
                            )
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = { showForm = true }) {
                            Text(text = "Add New Card")
                        }
                        Button(onClick = onDismiss, enabled = selectedCardId != null) {
                            Text(text = "Done")
                        }
                    }
                }
            }
        }
    }
}

// -- Add Card Form --

@Composable
private fun AddCardFormContent(
    onSave: (CardInfo) -> Unit,
    onCancel: () -> Unit
) {
    var type     by remember { mutableStateOf(CardType.VISA_MASTER) }
    var name     by remember { mutableStateOf("") }
    var number   by remember { mutableStateOf("") }
    var month    by remember { mutableStateOf("") }
    var year     by remember { mutableStateOf("") }
    var pin      by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var saveCard by remember { mutableStateOf(true) }

    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CardTypeDropdown(type = type, onTypeSelected = { type = it })

            BorderedFormField(
                label         = "Name on Card",
                text          = name,
                keyboardType  = KeyboardType.Text,
                onValueChange = { name = it }
            )

            BorderedTransformedField(
                label = "Card Number",
                text = number,
                keyboardType = KeyboardType.Number,
                visualTransformation = CreditCardVisualTransformation(),
                onValueChange = { raw ->
                    number = raw.filter(Char::isDigit).take(16)
                }
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "Expiration Date", color = Color.Black)


            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BorderedFormField(
                    label         = "MM",
                    text          = month,
                    keyboardType  = KeyboardType.Number,
                    modifier      = Modifier.weight(1f).height(45.dp),
                    onValueChange = {
                        if (it.length <= 2 && it.all(Char::isDigit) && it.toIntOrNull() in 0..12) month = it
                    }
                )
                BorderedFormField(
                    label         = "YY",
                    text          = year,
                    keyboardType  = KeyboardType.Number,
                    modifier      = Modifier.weight(1f).height(45.dp),
                    onValueChange = {
                        if (it.length <= 2 && it.all(Char::isDigit) && it.toIntOrNull() in 0..99) year = it
                    }
                )
            }

            BorderedFormField(
                label         = "CVV",
                text          = pin,
                keyboardType  = KeyboardType.Number,
                onValueChange = { if (it.length <= 3 && it.all(Char::isDigit) && it.toIntOrNull() in 0..999) pin = it }
            )

            BorderedFormField(
                label         = "Nickname",
                text          = nickname,
                keyboardType  = KeyboardType.Text,
                onValueChange = { nickname = it }
            )

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Save this card?", color = Color.Black)
                Spacer(Modifier.width(8.dp))
                Switch(checked = saveCard, onCheckedChange = { saveCard = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor   = Color.White,
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor   = GreenPrimary,
                        uncheckedTrackColor = Color.Gray.copy(alpha = 0.2f)
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text(text = "Cancel")
            }
            Spacer(Modifier.width(12.dp))
            Button(
                onClick = {
                    onSave(
                        CardInfo(
                            type     = type,
                            name     = name,
                            number   = number,
                            month    = month,
                            year     = year,
                            pin      = pin,
                            nickname = nickname,
                            saveCard = saveCard
                        )
                    )
                },
                enabled = name.isNotBlank()
                        && number.isNotBlank()
                        && month.length == 2
                        && year.length == 2
                        && pin.isNotBlank()
                        && nickname.isNotBlank()
            ) {
                Text(text = "Save")
            }
        }
    }
}

// -- Custom Bordered Field --

@Composable
private fun BorderedFormField(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Black else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(12.dp)
    ) {
        if (text.isEmpty()) {
            Text(text = label, color = Color.Gray, modifier = Modifier.align(Alignment.CenterStart))
        }
        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier
                .fillMaxWidth()
                .focusable()
                .onFocusChanged { isFocused = it.isFocused }
        )
    }
}

// -- Card Row --

@Composable
private fun CardRow(
    card: CardInfo,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = card.type.icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = card.nickname.ifBlank { "**** **** ${card.number.takeLast(4)}" },
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(text = "(${card.type.displayName})", fontSize = 12.sp, color = Color.Gray)
        }
        Spacer(Modifier.weight(1f))
        RadioButton(
            selected = isSelected,
            onClick  = onClick,
            colors   = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
private fun BorderedTransformedField(
    label: String,
    text: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Black else Color.LightGray,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(12.dp)
    ) {
        // placeholder
        if (text.isEmpty()) {
            Text(label, color = Color.Gray, modifier = Modifier.align(Alignment.CenterStart))
        }

        BasicTextField(
            value = text,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = Color.Black, fontSize = 16.sp),
            cursorBrush = SolidColor(Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )
    }
}

// -- Card Type Dropdown --

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardTypeDropdown(
    type: CardType,
    onTypeSelected: (CardType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value         = type.displayName,
            onValueChange = {},
            readOnly      = true,
            label         = { Text("Card Type") },
            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier      = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
            modifier         = Modifier.fillMaxWidth()
        ) {
            CardType.values().forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        onTypeSelected(option)
                        expanded = false
                    },
                    text = { Text(option.displayName) }
                )
            }
        }
    }
}
