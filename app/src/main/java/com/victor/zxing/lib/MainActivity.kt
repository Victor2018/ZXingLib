package com.victor.zxing.lib

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.victor.camera.preview.lib.CameraScan
import com.victor.zxing.lib.databinding.ActivityMainBinding
import com.victor.zxing.library.util.CodeUtils.parseCode


class MainActivity : AppCompatActivity() {
    val REQUEST_CODE_SCAN: Int = 0x01
    val REQUEST_CODE_PHOTO: Int = 0x02


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.mBtnMultiScan.setOnClickListener { view ->
            MultiFormatScanActivity.intentStart(this)
        }
        binding.mBtnQrScan.setOnClickListener { view ->
            QrCodeScanActivity.intentStart(this)
        }
        binding.mBtnFullScreenQrScan.setOnClickListener { view ->
            FullScreenQRCodeScanActivity.intentStart(this)
        }
        binding.mBtnScanImageQrCode.setOnClickListener { view ->
            startPickPhoto()
        }
        binding.mBtnGenerateQrCode.setOnClickListener { view ->
            QrCodeActivity.intentStart(this)
        }
        binding.mBtnGenerateBarCode.setOnClickListener { view ->
            BarCodeActivity.intentStart(this)
        }
    }

    /**
     * 开始选择图片
     */
    private fun startPickPhoto() {
        val pickIntent = Intent(Intent.ACTION_PICK)
        pickIntent.setType("image/*")
        startActivityForResult(pickIntent, REQUEST_CODE_PHOTO)
    }

    private fun parsePhoto(data: Intent) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            //异步解析
            runOnIO{
                val result = parseCode(bitmap)
                // 如果只需识别二维码，建议使用：parseQRCode；（因为识别的格式越明确，误识别率越低。）
//                final String result = CodeUtils.parseQRCode(bitmap);
                runOnMain {
                    Toast.makeText(this@MainActivity,result,Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_SCAN -> {
                    val result = CameraScan.parseScanResult(data)
                    Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
                }

                REQUEST_CODE_PHOTO -> parsePhoto(data)
            }
        }
    }



}