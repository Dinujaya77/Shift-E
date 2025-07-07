package com.example.shift_e.ui.screens

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.PillButton
import com.example.shift_e.ui.components.PillTextField
import com.example.shift_e.ui.components.ShowToast
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(navController: NavController) {
    val context       = LocalContext.current
    val focusManager  = LocalFocusManager.current
    val lastReq       = remember { FocusRequester() }
    val birthdayReq   = remember { FocusRequester() }
    val genderReq     = remember { FocusRequester() }
    val mobileReq     = remember { FocusRequester() }

    var firstName  by remember { mutableStateOf("") }
    var lastName   by remember { mutableStateOf("") }
    var birthday   by remember { mutableStateOf("") }
    var mobile     by remember { mutableStateOf("") }
    var gender     by remember { mutableStateOf("") }
    var expanded   by remember { mutableStateOf(false) }
    var showToast  by remember { mutableStateOf<String?>(null) }

    val genderOptions = listOf("Male", "Female", "Other")

    // Helper to open DatePicker
    fun openDatePicker() {
        val now = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d ->
                birthday = "%04d-%02d-%02d".format(y, m + 1, d)
                // after picking move to gender
                genderReq.requestFocus()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Box(Modifier.fillMaxSize()) {
        // Header image
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // Title overlay
        Text(
            "Complete Profile",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(x = 20.dp, y = 160.dp)
        )

        // Scrollable form
        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = 210.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "First Name",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            // First Name
            PillTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First name",
                leadingIcon = { Icon(Icons.Default.Person, null, tint = TealDark) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { lastReq.requestFocus() }),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Last Name",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Last Name
            PillTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last name",
                leadingIcon = { Icon(Icons.Default.Person, null, tint = TealDark) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(lastReq),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { openDatePicker() })
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "BirthDay",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Birthday (clickable)
            PillTextField(
                value = birthday,
                onValueChange = {},
                placeholder = "Birthday (YYYY-MM-DD)",
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = TealDark) },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { openDatePicker() }
                    .focusRequester(birthdayReq),
                keyboardOptions = KeyboardOptions.Default,
                keyboardActions = KeyboardActions.Default
            )
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Gender",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Gender dropdown
            Box(Modifier.fillMaxWidth()) {
                PillTextField(
                    value = gender,
                    onValueChange = {},
                    placeholder = "Select gender",
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, null, tint = TealDark)
                    },
                    enabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .focusRequester(genderReq),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { mobileReq.requestFocus() })
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(fraction = 0.7f)
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                                mobileReq.requestFocus()
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Mobile Number",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Mobile
            PillTextField(
                value = mobile,
                onValueChange = { mobile = it },
                placeholder = "07XXXXXXXX",
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = TealDark) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(mobileReq),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    showToast = when {
                        !mobile.matches(Regex("^07\\d{8}\$")) ->
                            "Invalid SL mobile"
                        else -> {
                            navController.navigate("dashboard?username=$firstName") {
                                popUpTo("login") { inclusive = true }
                            }
                            null
                        }
                    }
                })
            )
            Spacer(Modifier.height(24.dp))

            // Next
            PillButton("NEXT", {
                focusManager.clearFocus()
                showToast = when {
                    firstName.isBlank() || lastName.isBlank() ||
                            birthday.isBlank() || gender.isBlank() ||
                            !mobile.matches(Regex("^07\\d{8}\$")) ->
                        "Fill all fields correctly"
                    else -> {
                        navController.navigate("dashboard?username=$firstName") {
                            popUpTo("login") { inclusive = true }
                        }
                        null
                    }
                }
            }, modifier = Modifier.fillMaxWidth().height(48.dp))

            showToast?.let {
                ShowToast(it)
                showToast = null
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}