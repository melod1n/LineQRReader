package com.xcheng.scannere3

@Suppress("unused")
object XCScanner {

    init {
//        System.loadLibrary("XCScanner")
    }

    interface Result {
        fun scanBeep()
        fun scanStart()
        fun scanResult(sym: String, content: String)
    }

    external fun newInstance(): XCScanner

    external fun deleteInstance()

    external fun stopDecode()

    external fun startDecode()

    external fun onScanListener(v: Result): XCScanner

    external fun getVersion(): String

    external fun disableAllSym()

    external fun enableAllSym()

    external fun configSymOnOff(var1: Int, var2: Int)

    external fun setRoundTimeout(var1: Int)

    external fun configDecoderTag(var1: Int, var2: Int)

    external fun getDecoderTag(var1: Int): Int

    external fun setWorkMode(var1: Int)

    external fun getWorkMode(): Int

    external fun configLights(var1: Int, var2: Int)

    external fun setLightTestMode(var1: Int)
}