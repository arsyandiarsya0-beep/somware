package com.arxware.ff

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = Color(0xFF0A0A0A))) {
                var user by remember { mutableStateOf("") }
                var pass by remember { mutableStateOf("") }
                var error by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ARXWARE", color = Color(0xFFFF0055), fontSize = 36.sp, fontWeight = FontWeight.Black)
                    Text("Free Fire Module", color = Color.Gray, fontSize = 14.sp)
                    Spacer(Modifier.height(48.dp))

                    OutlinedTextField(
                        value = user,
                        onValueChange = { user = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0055))
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFFF0055))
                    )
                    Spacer(Modifier.height(24.dp))

                    if (error.isNotEmpty()) {
                        Text(error, color = Color.Red, fontSize = 12.sp)
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            if (user == "arx som2" && pass == "arxware2") {
                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                error = "Username atau Password salah!"
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0055)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("LOGIN", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}
