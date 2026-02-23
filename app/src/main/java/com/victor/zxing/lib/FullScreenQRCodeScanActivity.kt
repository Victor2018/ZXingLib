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
import com.victor.zxing.library.DecodeFormatManager
import com.victor.zxing.library.analyze.MultiFormatAnalyzer

class FullScreenQRCodeScanActivity : AppCompatActivity(),OnScanResultCallback<Result> {

    companion object {
        @JvmStatic
        fun intentStart (context: Context) {
            val intent = Intent(context, FullScreenQRCodeScanActivity::class.java)
            context.startActivity(intent)
        }
    }

    val  TAG = "FullScreenQRCodeScanActivity"

    /**
     * 预览视图
     */
    protected var previewView: PreviewView? = null
    protected var mViewFinderView: ViewFinderView? = null

    protected var mCameraPreviewHelper: CameraPreviewHelper<Result>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_qr_code_scan)

        previewView = findViewById(R.id.previewView)
        mViewFinderView = findViewById(R.id.mViewFinderView)

        mCameraPreviewHelper = CameraPreviewHelper(this,previewView!!,this)

        val decodeConfig = DecodeConfig()
        decodeConfig.hints = DecodeFormatManager.QR_CODE_HINTS//如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
        decodeConfig.isFullAreaScan = true//设置是否全区域识别，默认false

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

    override fun onDestroy() {
        mCameraPreviewHelper?.releaseCamera()
        super.onDestroy()
    }

    override fun onScanResultCallback(result: AnalyzeResult<Result>) {
        Log.e(TAG,"onScanResultCallback-result.imageData = ${result.imageData}")
        mCameraPreviewHelper?.setAnalyzeImage(false)
        Toast.makeText(this,result.result.text, Toast.LENGTH_SHORT).show()

        finish()

//        val points = ArrayList<Point>()
//        result.result.resultPoints.forEach {
//            val item = Point(it.x.toInt(),it.y.toInt())
//            points.add(item)
//        }
//        mViewFinderView?.showResultPoints(points)
//        mCameraPreviewHelper?.setAnalyzeImage(true)

    }
}
