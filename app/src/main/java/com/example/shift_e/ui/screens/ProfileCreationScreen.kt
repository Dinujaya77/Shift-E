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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(navController: NavController) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val lastReq = remember { FocusRequester() }
    val birthdayReq = remember { FocusRequester() }
    val genderReq = remember { FocusRequester() }
    val mobileReq = remember { FocusRequester() }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf<String?>(null) }

    val genderOptions = listOf("Male", "Female", "Other")
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val uid = auth.currentUser?.uid

    fun openDatePicker() {
        val now = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _: DatePicker, y, m, d ->
                birthday = "%04d-%02d-%02d".format(y, m + 1, d)
                genderReq.requestFocus()
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(300.dp)
        )

        Text(
            "Complete Profile",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.offset(x = 20.dp, y = 160.dp)
        )

        Column(
            Modifier
                .fillMaxWidth()
                .offset(y = 210.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("First Name", color = BlackLight, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
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

            Text("Last Name", color = BlackLight, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            PillTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last name",
                leadingIcon = { Icon(Icons.Default.Person, null, tint = TealDark) },
                modifier = Modifier.fillMaxWidth().focusRequester(lastReq),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { openDatePicker() })
            )
            Spacer(Modifier.height(16.dp))

            Text("Birth Date", color = BlackLight, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            PillTextField(
                value = birthday,
                onValueChange = {},
                placeholder = "Birthday (YYYY-MM-DD)",
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = TealDark) },
                enabled = false,
                modifier = Modifier.fillMaxWidth().clickable { openDatePicker() }.focusRequester(birthdayReq)
            )
            Spacer(Modifier.height(16.dp))

            Text("Gender", color = BlackLight, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Box(Modifier.fillMaxWidth()) {
                PillTextField(
                    value = gender,
                    onValueChange = {},
                    placeholder = "Select gender",
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, null, tint = TealDark) },
                    enabled = false,
                    modifier = Modifier.fillMaxWidth().clickable { expanded = true }.focusRequester(genderReq),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { mobileReq.requestFocus() })
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.7f)
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

            Text("Mobile Number", color = BlackLight, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            PillTextField(
                value = mobile,
                onValueChange = { mobile = it },
                placeholder = "07XXXXXXXX",
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = TealDark) },
                modifier = Modifier.fillMaxWidth().focusRequester(mobileReq),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    handleProfileSubmit(
                        uid, firstName, lastName, birthday, gender, mobile,
                        db, navController, showToastSetter = { showToast = it }
                    )
                })
            )
            Spacer(Modifier.height(24.dp))

            PillButton("NEXT", {
                focusManager.clearFocus()
                handleProfileSubmit(
                    uid, firstName, lastName, birthday, gender, mobile,
                    db, navController, showToastSetter = { showToast = it }
                )
            }, modifier = Modifier.fillMaxWidth().height(48.dp))

            showToast?.let {
                ShowToast(it)
                showToast = null
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

private fun handleProfileSubmit(
    uid: String?,
    firstName: String,
    lastName: String,
    birthday: String,
    gender: String,
    mobile: String,
    db: FirebaseFirestore,
    navController: NavController,
    showToastSetter: (String?) -> Unit
) {
    if (uid == null) {
        showToastSetter("User not authenticated")
        return
    }

    if (firstName.isBlank() || lastName.isBlank() || birthday.isBlank() || gender.isBlank()
        || !mobile.matches(Regex("^07\\d{8}\$"))
    ) {
        showToastSetter("Fill all fields correctly")
        return
    }

    val data = mapOf(
        "firstName" to firstName,
        "lastName" to lastName,
        "birthday" to birthday,
        "gender" to gender,
        "mobile" to mobile
    )

    db.collection("users").document(uid).update(data)
        .addOnSuccessListener {
            navController.navigate("dashboard?username=$firstName") {
                popUpTo("login") { inclusive = true }
            }
        }
        .addOnFailureListener {
            showToastSetter("Failed to save profile")
        }
}
