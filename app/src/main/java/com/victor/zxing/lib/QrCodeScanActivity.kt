package com.victor.zxing.lib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.view.PreviewView
import com.google.zxing.Result
import com.victor.camera.lib.ViewFinderView
import com.victor.camera.lib.interfaces.OnTorchStateChangeListener
import com.victor.camera.preview.lib.AnalyzeResult
import com.victor.camera.preview.lib.CameraPreviewHelper
import com.victor.camera.preview.lib.CameraScan.OnScanResultCallback
import com.victor.zxing.library.DecodeConfig
import com.victor.zxing.library.analyze.MultiFormatAnalyzer

class QrCodeScanActivity : AppCompatActivity(),OnScanResultCallback<Result> {

    companion object {
        @JvmStatic
        fun intentStart (context: Context) {
            val intent = Intent(context, QrCodeScanActivity::class.java)
            context.startActivity(intent)
        }
    }

    val  TAG = "QrCodeScanActivity"

    /**
     * 预览视图
     */
    protected var previewView: PreviewView? = null
    protected var mViewFinderView: ViewFinderView? = null

    protected var mCameraPreviewHelper: CameraPreviewHelper<Result>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_scan)

        previewView = findViewById(R.id.previewView)
        mViewFinderView = findViewById(R.id.mViewFinderView)

        mCameraPreviewHelper = CameraPreviewHelper(this,previewView!!,this)

        val decodeConfig = DecodeConfig()
        decodeConfig.isFullAreaScan = false
        decodeConfig.areaRectRatio = 0.6f
        decodeConfig.areaRectVerticalOffset = 0//设置识别区域垂直方向偏移量，默认为0，为0表示居中，可以为负数
        decodeConfig.areaRectHorizontalOffset = 0
        mCameraPreviewHelper?.setAnalyzer(MultiFormatAnalyzer(decodeConfig))

        mCameraPreviewHelper?.setPlayBeep(true)
        mCameraPreviewHelper?.setVibrate(true)

        mViewFinderView?.mOnTorchStateChangeListener = object : OnTorchStateChangeListener {
            override fun onTorchStateChanged(isOn: Boolean) {
                Log.e(TAG,"onTorchStateChanged-isOn = $isOn")
                mCameraPreviewHelper?.enableTorch(isOn)
            }
        }

        mCameraPreviewHelper?.startCamera()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mCameraPreviewHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onPause() {
        mCameraPreviewHelper?.onDestroy()
        super.onPause()
    }

    override fun onDestroy() {
        mCameraPreviewHelper?.releaseCamera()
        super.onDestroy()
    }

    override fun onScanResultCallback(result: AnalyzeResult<Result>) {
        Log.e(TAG,"onScanResultCallback-result.imageData = ${result.imageData}")
        mCameraPreviewHelper?.setAnalyzeImage(false)
        Toast.makeText(this,result.result.text, Toast.LENGTH_SHORT).show()
        mCameraPreviewHelper?.setAnalyzeImage(true)
    }
}
