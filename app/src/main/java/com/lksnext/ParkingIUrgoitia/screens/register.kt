package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var hasMobilityPermit by remember { mutableStateOf(false) }
    val lksOrange = Color(0xFFF25C05)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = lksOrange,
            shadowElevation = 8.dp,
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = "Car Icon",
                tint = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))


        Text(
            text = "Create Account",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Join LKS next Parking to get started",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))


        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.weight(1f), // Ocupa la mitad del espacio
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lksOrange,
                    focusedLabelColor = lksOrange
                )
            )

            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre los dos campos

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = lksOrange,
                    focusedLabelColor = lksOrange
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Gmail address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lksOrange,
                focusedLabelColor = lksOrange
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lksOrange,
                focusedLabelColor = lksOrange
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lksOrange,
                focusedLabelColor = lksOrange
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = hasMobilityPermit,
                onCheckedChange = { hasMobilityPermit = it },
                colors = CheckboxDefaults.colors(checkedColor = lksOrange)
            )
            Text(
                text = "I have a reduced mobility/disabled parking permit",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { /* Lógica de registro */ },
            colors = ButtonDefaults.buttonColors(containerColor = lksOrange),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Register",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Already have an account? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Log In",
                color = lksOrange,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Navegar a Login */ }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}