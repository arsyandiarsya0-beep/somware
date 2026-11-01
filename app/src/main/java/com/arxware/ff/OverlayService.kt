package com.arxware.ff

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var view: ComposeView? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val ch = NotificationChannel("arx", "Arxware Service", NotificationManager.IMPORTANCE_LOW)
        getSystemService(NotificationManager::class.java).createNotificationChannel(ch)
        startForeground(1, Notification.Builder(this, "arx").setSmallIcon(android.R.drawable.ic_menu_manage).setContentTitle("Arxware Active").build())
        
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        showOverlay()
        
        try { NativeBridge.init() } catch (e: Exception) { e.printStackTrace() }
    }

    private fun showOverlay() {
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.START; x = 50; y = 200 }

        val cv = ComposeView(this).apply { setContent { MenuUI() } }
        wm.addView(cv, lp)
        view = cv
    }

    override fun onDestroy() { view?.let { wm.removeView(it) }; super.onDestroy() }
}

@Composable
fun MenuUI() {
    var aimOn by remember { mutableStateOf(false) }
    var headLock by remember { mutableStateOf(false) }
    
    LaunchedEffect(aimOn, headLock) {
        if (aimOn) NativeBridge.setAim(true, headLock)
        else NativeBridge.setAim(false, false)
    }

    Column(
        modifier = Modifier.background(Color(0xEE0A0A0A), RoundedCornerShape(16.dp)).padding(16.dp).width(200.dp)
    ) {
        Text("ARXWARE", color = Color(0xFFFF0055), fontSize = 18.sp)
        Spacer(Modifier.height(12.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Aimbot", color = Color.White, fontSize = 14.sp)
            Switch(checked = aimOn, onCheckedChange = { aimOn = it })
        }
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Head Lock", color = Color.White, fontSize = 14.sp)
            Switch(checked = headLock, onCheckedChange = { headLock = it })
        }
    }
}
