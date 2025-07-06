package com.example.shift_e.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shift_e.R
import com.example.shift_e.ui.components.PillButton
import com.example.shift_e.ui.components.PillTextField
import com.example.shift_e.ui.theme.BlackLight
import com.example.shift_e.ui.theme.CreamBackground
import com.example.shift_e.ui.theme.TealDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCreationScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var birthday  by remember { mutableStateOf("") }
    var mobile    by remember { mutableStateOf("") }
    var gender    by remember { mutableStateOf("") }
    var expanded  by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")

    Box(Modifier.fillMaxSize()) {
        // 1) Header image (same height as login)
        Image(
            painter = painterResource(R.drawable.login_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        // 2) Overlayed title (same offset and style as login)
        Text(
            text = "Complete Profile",
            color = Color.White,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .offset(x = 20.dp, y = 160.dp)
        )

        // 3) Cream card with rounded top corners (32dp), offset same as login
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 210.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(CreamBackground)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 4) Avatar circle


            // 5) Form fields, each full‑width with leading icons

            Text(
                text = "First Name",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            PillTextField(
                value = firstName,
                onValueChange = { firstName = it },
                placeholder = "First name",
                isPassword = false,
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TealDark)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            Text(
                text = "Last Name",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            PillTextField(
                value = lastName,
                onValueChange = { lastName = it },
                placeholder = "Last name",
                isPassword = false,
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TealDark)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            Text(
                text = "BirthDay",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            PillTextField(
                value = birthday,
                onValueChange = { birthday = it },
                placeholder = "Birthday (YYYY-MM-DD)",
                isPassword = false,
                leadingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = TealDark)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            Text(
                text = "Gender",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Gender dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                PillTextField(
                    value = gender,
                    onValueChange = {  },
                    placeholder = if (gender.isEmpty()) "Select gender" else gender,
                    isPassword = false,
                    enabled = false,
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = TealDark,
                            modifier = Modifier.clickable { expanded = true }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    genderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Mobile Number",
                color = BlackLight,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            PillTextField(
                value = mobile,
                onValueChange = { mobile = it },
                placeholder = "Mobile number",
                isPassword = false,
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = TealDark)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(Modifier.height(24.dp))

            // 6) NEXT button
            PillButton(
                text = "NEXT",
                onClick = {
                    // validation or navigate to dashboard
                    navController.navigate("dashboard")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }
    }
}
