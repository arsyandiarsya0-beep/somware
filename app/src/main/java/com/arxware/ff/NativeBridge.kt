package com.arxware.ff

object NativeBridge {
    init { System.loadLibrary("arxware") }
    external fun init()
    external fun setAim(enabled: Boolean, headLock: Boolean)
}
