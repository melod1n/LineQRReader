package com.xcheng.scannere3

@Suppress("unused")
object XCScanner {

    init {
        System.loadLibrary("XCScanner")
    }

    external fun newInstance(): XCScanner

    interface Result {
        // Be called firstly after scan finish to play beep
        // The beep notification should be implemented by user
        fun scanBeep()

        // Be called secondary after scan finish to start next scan round.
        // The scan start should be implemented by user and use the
        // #startDecode to start next scan round.
        fun scanStart()

        // Be called thirdly after scan finish to report scan result
        // Should not update UI in this implement.
        fun scanResult(sym: String, content: String)
    }

    external fun deleteInstance()

    external fun stopDecode()

    external fun startDecode()

    external fun onScanListener(v: Result): XCScanner

    external fun getVersion(): String
}