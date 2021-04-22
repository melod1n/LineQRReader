package com.meloda.lineqrreader.scanner

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.view.Surface
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.util.SoundUtils
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

    var isContinuous: Boolean = false

    private val cameraState = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            cameraCaptureSession = session

            try {
                val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                builder.addTarget(surface!!)

                session.setRepeatingRequest(builder.build(), null, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
        }

    }

    init {
        surfaceTexture = SurfaceTexture(0)
        surface = Surface(surfaceTexture)
        surfaceTexture?.setDefaultBufferSize(844, 640)
    }

    fun init() {
        openCamera()

//        scanner = XCScanner.newInstance()
        scanner?.onScanListener(this)
    }

    fun release() {
        releaseCamera()
        scanner?.deleteInstance()
        scanner = null
    }

    fun startDecoding() {
        scanner?.startDecode()
    }

    fun stopDecoding() {
        scanner?.stopDecode()
    }

    private fun openCamera() {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) return

        try {
            AppGlobal.cameraManager.openCamera("1", this, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun releaseCamera() {
        cameraCaptureSession?.close()
        cameraDevice?.close()
        surfaceTexture?.release()
        surface?.release()
    }

    override fun scanBeep() {
        SoundUtils.playSuccessSound(context)
    }

    override fun scanStart() {
        if (isContinuous) {
            scanner?.startDecode()
        }
    }

    override fun scanResult(sym: String, content: String) {
        listener.onResult(sym, content)
    }

    @Suppress("DEPRECATION")
    override fun onOpened(camera: CameraDevice) {
        cameraDevice = camera

        try {
            camera.createCaptureSession(Collections.singletonList(surface), cameraState, null)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDisconnected(camera: CameraDevice) {
    }

    override fun onError(camera: CameraDevice, error: Int) {
    }

}