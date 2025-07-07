package com.example.shift_e.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp

@Composable
fun PillTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isPassword: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value               = value,
        onValueChange       = onValueChange,
        modifier            = modifier,
        placeholder         = { if (placeholder.isNotEmpty()) Text(placeholder) },
        singleLine          = true,
        enabled             = enabled,
        leadingIcon         = leadingIcon,
        trailingIcon        = trailingIcon,
        visualTransformation= if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions     = keyboardOptions,
        keyboardActions     = keyboardActions,
        shape               = RoundedCornerShape(50),
        colors              = OutlinedTextFieldDefaults.colors(
            focusedBorderColor    = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor  = MaterialTheme.colorScheme.outline,
            focusedLabelColor     = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor   = MaterialTheme.colorScheme.onSurface,
            errorBorderColor      = MaterialTheme.colorScheme.error
        )
    )
}
