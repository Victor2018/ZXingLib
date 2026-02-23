package com.victor.zxing.lib

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.victor.zxing.lib.databinding.ActivityQrCodeBinding
import com.victor.zxing.library.util.CodeUtils.createQRCode


class QrCodeActivity : AppCompatActivity() {

    companion object {
        fun intentStart (context: Context) {
            val intent = Intent(context, QrCodeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createQRCode("423099")
    }

    /**
     * 生成二维码
     * @param content
     */
    private fun createQRCode(content: String) {
        runOnIO {
            //生成二维码相关放在子线程里面
            val logo =
                BitmapFactory.decodeResource(resources, android.R.mipmap.sym_def_app_icon)
            val bitmap = createQRCode(content, 600, logo)
            runOnMain {
                //显示二维码
                binding.mIvQrCode.setImageBitmap(bitmap)
            }
        }
    }
}