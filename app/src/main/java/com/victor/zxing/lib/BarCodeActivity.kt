package com.victor.zxing.lib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.victor.zxing.lib.databinding.ActivityBarCodeBinding
import com.victor.zxing.library.util.CodeUtils
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BarCodeActivity : AppCompatActivity() {

    companion object {
        fun intentStart (context: Context) {
            val intent = Intent(context, BarCodeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityBarCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createBarCode("42309900")
    }

    /**
     * 生成条形码
     * @param content
     */
    private fun createBarCode(content: String) {
        runOnIO{
            //生成条形码相关放在子线程里面
            val bitmap =
                CodeUtils.createBarCode(content, BarcodeFormat.CODE_128, 800, 200, null, true)
            runOnMain {
                //显示条形码
                binding.mIvBarCode.setImageBitmap(bitmap)
            }
        }
    }
}