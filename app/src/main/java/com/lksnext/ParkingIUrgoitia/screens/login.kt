package com.lksnext.ParkingIUrgoitia.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun LoginScreen() {
    // Estas variables guardan lo que el usuario escribe
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    val lksOrange = Color(0xFFF25C05)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Icono del coche
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

        // 2. Título principal
        Text(
            text = "Welcome to LKS next Parking",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFF111827)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Text(
            text = "Sign in to manage your parking reservations",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(32.dp))


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
            visualTransformation = PasswordVisualTransformation(), // Oculta los caracteres
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = lksOrange,
                focusedLabelColor = lksOrange
            )
        )

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Forgot password?",
                color = lksOrange,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { /* Aquí irá la lógica en el futuro */ }
                    .padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 7. Botón principal de Login
        Button(
            onClick = { /* Aquí irá la lógica de Login */ },
            colors = ButtonDefaults.buttonColors(containerColor = lksOrange),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Log In",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 8. Texto de registro en la parte inferior
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New user? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Register here",
                color = lksOrange,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* Navegar a la pantalla de registro */ }
            )
        }
    }
}

// Esta función permite ver una vista previa sin tener que arrancar el emulador
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}