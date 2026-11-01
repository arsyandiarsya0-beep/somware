package com.arxware.ff

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(background = Color(0xFF0A0A0A))) {
                var hasOverlay by remember { mutableStateOf(Settings.canDrawOverlays(this)) }
                
                LaunchedEffect(Unit) {
                    while (true) {
                        hasOverlay = Settings.canDrawOverlays(this@MainActivity)
                        delay(1000)
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ARXWARE READY", color = Color(0xFFFF0055), fontSize = 24.sp)
                    Spacer(Modifier.height(24.dp))
                    
                    if (!hasOverlay) {
                        Button(onClick = {
                            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
                        }) {
                            Text("Beri Izin Overlay")
                        }
                    } else {
                        Button(
                            onClick = {
                                val intent = Intent(this@MainActivity, OverlayService::class.java)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent) else startService(intent)
                                finish()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0055))
                        ) {
                            Text("Mulai Aimbot")
                        }
                    }
                }
            }
        }
    }
}
