package com.meloda.lineqrreader.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.view.Surface
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.xcheng.scannere3.XCScanner
import java.util.*

class ScannerUtil(
    private val context: Context,
    private val listener: ScannerResultListener
) :
    XCScanner.Result,
    CameraDevice.StateCallback() {

    private var scanner: XCScanner? = null

    private var cameraCaptureSession: CameraCaptureSession? = null
    private var cameraDevice: CameraDevice? = null

    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null

    private var longScan = true

//    private val isContinuous = false

    private val cameraState = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session

            try {
                val builder =
                    cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                builder.addTarget(surface!!)

                session.setRepeatingRequest(builder.build(), null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            val i = 9
        }

    }

    init {
        surfaceTexture = SurfaceTexture(0)
        surface = Surface(surfaceTexture)
        surfaceTexture?.setDefaultBufferSize(844, 640)
    }

    fun init() {
        openCamera()

        scanner = XCScanner.newInstance()
        scanner?.onScanListener(this)
    }

    fun release() {
        releaseCamera()
        scanner?.deleteInstance()
    }

    fun startDecoding() {
        scanner?.startDecode()

        longScan = true

//        if (isContinuous) longScan = true
    }

    fun stopDecoding() {
        scanner?.stopDecode()

        longScan = false

//        if (isContinuous) longScan = false
    }

    fun openCamera() {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return

        try {
            AppGlobal.cameraManager.openCamera("1", this, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun releaseCamera() {
        cameraCaptureSession?.close()
        cameraDevice?.close()
        surfaceTexture?.release()
        surface?.release()
    }

    override fun scanBeep() {
        SoundUtils.playSuccessSound(context)
    }

    override fun scanStart() {
        if (longScan) scanner?.startDecode()
    }

    override fun scanResult(sym: String, content: String) {
        listener.onResult(sym, content)
        longScan = false
    }

    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera

        try {
            camera.createCaptureSession(Collections.singletonList(surface), cameraState, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDisconnected(camera: CameraDevice) {
        val i = 0
    }

    override fun onError(camera: CameraDevice, error: Int) {
        val i = 9
    }

}