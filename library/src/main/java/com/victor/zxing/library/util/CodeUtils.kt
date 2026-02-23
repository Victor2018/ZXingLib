package com.victor.zxing.library.util

import com.victor.zxing.library.DecodeFormatManager
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.google.zxing.*
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: CodeUtils
 * Author: Victor
 * Date: 2026/2/23 14:39
 * Description: 二维码/条形码工具类：主要包括二维码/条形码的解析与生成
 * -----------------------------------------------------------------
 */


object CodeUtils {

    const val DEFAULT_REQ_WIDTH = 480
    const val DEFAULT_REQ_HEIGHT = 640

    /**
     * 生成二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @return
     */
    @JvmStatic
    fun createQRCode(content: String, size: Int): Bitmap? {
        return createQRCode(content, size, null)
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param codeColor 二维码的颜色
     * @return
     */
    @JvmStatic
    fun createQRCode(content: String, size: Int, @ColorInt codeColor: Int): Bitmap? {
        return createQRCode(content, size, null, codeColor)
    }

    /**
     * 生成我二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @param logo    Logo大小默认占二维码的20%
     * @return
     */
    @JvmStatic
    fun createQRCode(content: String, size: Int, logo: Bitmap?): Bitmap? {
        return createQRCode(content, size, logo, Color.BLACK)
    }

    /**
     * 生成我二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      Logo大小默认占二维码的20%
     * @param codeColor 二维码的颜色
     * @return
     */
    @JvmStatic
    fun createQRCode(content: String, size: Int, logo: Bitmap?, @ColorInt codeColor: Int): Bitmap? {
        return createQRCode(content, size, logo, 0.2f, codeColor)
    }

    /**
     * 生成二维码
     *
     * @param content 二维码的内容
     * @param size    二维码的大小
     * @param logo    二维码中间的Logo
     * @param ratio   Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @return
     */
    @JvmStatic
    fun createQRCode(
        content: String,
        size: Int,
        logo: Bitmap?,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float
    ): Bitmap? {
        //配置参数
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        //容错级别
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        //设置空白边距的宽度
        hints[EncodeHintType.MARGIN] = 1 //default is 4
        return createQRCode(content, size, logo, ratio, hints)
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      二维码中间的Logo
     * @param ratio     Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param codeColor 二维码的颜色
     * @return
     */
    @JvmStatic
    fun createQRCode(
        content: String,
        size: Int,
        logo: Bitmap?,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float,
        @ColorInt codeColor: Int
    ): Bitmap? {
        //配置参数
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        //容错级别
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        //设置空白边距的宽度
        hints[EncodeHintType.MARGIN] = 1 //default is 4
        return createQRCode(content, size, logo, ratio, hints, codeColor)
    }

    @JvmStatic
    fun createQRCode(
        content: String,
        size: Int,
        logo: Bitmap?,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float,
        hints: Map<EncodeHintType, *>?
    ): Bitmap? {
        return createQRCode(content, size, logo, ratio, hints, Color.BLACK)
    }

    /**
     * 生成二维码
     *
     * @param content   二维码的内容
     * @param size      二维码的大小
     * @param logo      二维码中间的Logo
     * @param ratio     Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @param hints
     * @param codeColor 二维码的颜色
     * @return
     */
    @JvmStatic
    fun createQRCode(
        content: String,
        size: Int,
        logo: Bitmap?,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float,
        hints: Map<EncodeHintType, *>?,
        @ColorInt codeColor: Int
    ): Bitmap? {
        try {

            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)
            val pixels = IntArray(size * size)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until size) {
                for (x in 0 until size) {
                    if (bitMatrix[x, y]) {
                        pixels[y * size + x] = codeColor
                    } else {
                        pixels[y * size + x] = Color.WHITE
                    }
                }
            }

            // 生成二维码图片的格式
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)

            return if (logo != null) {
                addLogo(bitmap, logo, ratio)
            } else bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 在二维码中间添加Logo图案
     *
     * @param src   原图
     * @param logo  中间的Logo
     * @param ratio Logo所占比例 因为二维码的最大容错率为30%，所以建议ratio的范围小于0.3
     * @return
     */
    private fun addLogo(
        src: Bitmap?,
        logo: Bitmap?,
        @FloatRange(from = 0.0, to = 1.0) ratio: Float
    ): Bitmap? {
        if (src == null) {
            return null
        }

        if (logo == null) {
            return src
        }

        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height

        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }

        //logo大小为二维码整体大小
        val scaleFactor = srcWidth * ratio / logoWidth
        val bitmap: Bitmap?
        try {
            bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2f, srcHeight / 2f)
            canvas.drawBitmap(
                logo,
                (srcWidth - logoWidth) / 2f,
                (srcHeight - logoHeight) / 2f,
                null
            )
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bitmap
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @return
     */
    @JvmStatic
    fun parseQRCode(bitmapPath: String): String? {
        val result = parseQRCodeResult(bitmapPath)
        return result?.text
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @return
     */
    @JvmStatic
    fun parseQRCodeResult(bitmapPath: String): Result? {
        return parseQRCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT)
    }

    /**
     * 解析二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param reqWidth   请求目标宽度，如果实际图片宽度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param reqHeight  请求目标高度，如果实际图片高度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @return
     */
    @JvmStatic
    fun parseQRCodeResult(bitmapPath: String, reqWidth: Int, reqHeight: Int): Result? {
        return parseCodeResult(bitmapPath, reqWidth, reqHeight, DecodeFormatManager.QR_CODE_HINTS)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @return
     */
    @JvmStatic
    fun parseCode(bitmapPath: String): String? {
        return parseCode(bitmapPath, DecodeFormatManager.ALL_HINTS)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param hints      解析编码类型
     * @return
     */
    @JvmStatic
    fun parseCode(bitmapPath: String, hints: Map<DecodeHintType, Any>?): String? {
        val result = parseCodeResult(bitmapPath, hints)
        return result?.text
    }

    /**
     * 解析二维码图片
     *
     * @param bitmap 解析的图片
     * @return
     */
    @JvmStatic
    fun parseQRCode(bitmap: Bitmap): String? {
        return parseCode(bitmap, DecodeFormatManager.QR_CODE_HINTS)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmap 解析的图片
     * @return
     */
    @JvmStatic
    fun parseCode(bitmap: Bitmap): String? {
        return parseCode(bitmap, DecodeFormatManager.ALL_HINTS)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmap 解析的图片
     * @param hints  解析编码类型
     * @return
     */
    @JvmStatic
    fun parseCode(bitmap: Bitmap, hints: Map<DecodeHintType, Any>?): String? {
        val result = parseCodeResult(bitmap, hints)
        return result?.text
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath
     * @param hints      解析编码类型
     * @return
     */
    @JvmStatic
    fun parseCodeResult(bitmapPath: String, hints: Map<DecodeHintType, Any>?): Result? {
        return parseCodeResult(bitmapPath, DEFAULT_REQ_WIDTH, DEFAULT_REQ_HEIGHT, hints)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmapPath 需要解析的图片路径
     * @param reqWidth   请求目标宽度，如果实际图片宽度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param reqHeight  请求目标高度，如果实际图片高度大于此值，会自动进行压缩处理，当 reqWidth 和 reqHeight都小于或等于0时，则不进行压缩处理
     * @param hints      解析编码类型
     * @return
     */
    @JvmStatic
    fun parseCodeResult(
        bitmapPath: String,
        reqWidth: Int,
        reqHeight: Int,
        hints: Map<DecodeHintType, Any>?
    ): Result? {
        return parseCodeResult(compressBitmap(bitmapPath, reqWidth, reqHeight), hints)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmap 解析的图片
     * @return
     */
    @JvmStatic
    fun parseCodeResult(bitmap: Bitmap): Result? {
        return parseCodeResult(getRGBLuminanceSource(bitmap), DecodeFormatManager.ALL_HINTS)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param bitmap 解析的图片
     * @param hints  解析编码类型
     * @return
     */
    @JvmStatic
    fun parseCodeResult(bitmap: Bitmap, hints: Map<DecodeHintType, Any>?): Result? {
        return parseCodeResult(getRGBLuminanceSource(bitmap), hints)
    }

    /**
     * 解析一维码/二维码图片
     *
     * @param source
     * @param hints
     * @return
     */
    @JvmStatic
    fun parseCodeResult(source: LuminanceSource?, hints: Map<DecodeHintType, Any>?): Result? {
        var result: Result? = null
        val reader = MultiFormatReader()
        try {
            reader.setHints(hints)
            if (source != null) {
                result = decodeInternal(reader, source)
                if (result == null) {
                    result = decodeInternal(reader, source.invert())
                }
                if (result == null && source.isRotateSupported) {
                    result = decodeInternal(reader, source.rotateCounterClockwise())
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader.reset()
        }

        return result
    }

    private fun decodeInternal(reader: MultiFormatReader, source: LuminanceSource): Result? {
        var result: Result? = null
        try {
            try {
                //采用HybridBinarizer解析
                result = reader.decodeWithState(BinaryBitmap(HybridBinarizer(source)))
            } catch (ignored: Exception) {
            }
            if (result == null) {
                //如果没有解析成功，再采用GlobalHistogramBinarizer解析一次
                result = reader.decodeWithState(BinaryBitmap(GlobalHistogramBinarizer(source)))
            }
        } catch (ignored: Exception) {
        }
        return result
    }

    /**
     * 压缩图片
     *
     * @param path
     * @return
     */
    private fun compressBitmap(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        return if (reqWidth > 0 && reqHeight > 0) {//都大于进行判断是否压缩
            val newOpts = BitmapFactory.Options()
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true //获取原始图片大小
            BitmapFactory.decodeFile(path, newOpts) // 此时返回bm为空
            newOpts.inSampleSize = getSampleSize(reqWidth, reqHeight, newOpts)
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            newOpts.inJustDecodeBounds = false

            BitmapFactory.decodeFile(path, newOpts)
        } else {
            BitmapFactory.decodeFile(path)
        }
    }

    /**
     * @param reqWidth
     * @param reqHeight
     * @param newOpts
     * @return
     */
    private fun getSampleSize(reqWidth: Int, reqHeight: Int, newOpts: BitmapFactory.Options): Int {
        val width = newOpts.outWidth.toFloat()
        val height = newOpts.outHeight.toFloat()
        // 缩放比，由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        var wSize = 1 // wSize=1表示不缩放
        if (width > reqWidth) {// 如果宽度大的话根据宽度固定大小缩放
            wSize = (width / reqWidth).toInt()
        }
        var hSize = 1 // wSize=1表示不缩放
        if (height > reqHeight) {// 如果高度高的话根据宽度固定大小缩放
            hSize = (height / reqHeight).toInt()
        }
        var size = Math.max(wSize, hSize)
        if (size <= 0)
            size = 1
        return size
    }

    /**
     * 获取RGBLuminanceSource
     *
     * @param bitmap
     * @return
     */
    private fun getRGBLuminanceSource(bitmap: Bitmap): RGBLuminanceSource {
        val width = bitmap.width
        val height = bitmap.height

        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return RGBLuminanceSource(width, height, pixels)
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    @JvmStatic
    fun createBarCode(content: String, desiredWidth: Int, desiredHeight: Int): Bitmap? {
        return createBarCode(content, BarcodeFormat.CODE_128, desiredWidth, desiredHeight, null)
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int
    ): Bitmap? {
        return createBarCode(content, format, desiredWidth, desiredHeight, null)
    }

    @JvmStatic
    fun createBarCode(
        content: String,
        desiredWidth: Int,
        desiredHeight: Int,
        isShowText: Boolean
    ): Bitmap? {
        return createBarCode(
            content,
            BarcodeFormat.CODE_128,
            desiredWidth,
            desiredHeight,
            null,
            isShowText,
            40,
            Color.BLACK
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param desiredWidth
     * @param desiredHeight
     * @param isShowText
     * @param codeColor
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        desiredWidth: Int,
        desiredHeight: Int,
        isShowText: Boolean,
        @ColorInt codeColor: Int
    ): Bitmap? {
        return createBarCode(
            content,
            BarcodeFormat.CODE_128,
            desiredWidth,
            desiredHeight,
            null,
            isShowText,
            40,
            codeColor
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int,
        hints: Map<EncodeHintType, *>?
    ): Bitmap? {
        return createBarCode(
            content,
            format,
            desiredWidth,
            desiredHeight,
            hints,
            false,
            40,
            Color.BLACK
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int,
        hints: Map<EncodeHintType, *>?,
        isShowText: Boolean
    ): Bitmap? {
        return createBarCode(
            content,
            format,
            desiredWidth,
            desiredHeight,
            hints,
            isShowText,
            40,
            Color.BLACK
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param isShowText
     * @param codeColor
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int,
        isShowText: Boolean,
        @ColorInt codeColor: Int
    ): Bitmap? {
        return createBarCode(
            content,
            format,
            desiredWidth,
            desiredHeight,
            null,
            isShowText,
            40,
            codeColor
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int,
        hints: Map<EncodeHintType, *>?,
        isShowText: Boolean,
        @ColorInt codeColor: Int
    ): Bitmap? {
        return createBarCode(
            content,
            format,
            desiredWidth,
            desiredHeight,
            hints,
            isShowText,
            40,
            codeColor
        )
    }

    /**
     * 生成条形码
     *
     * @param content
     * @param format
     * @param desiredWidth
     * @param desiredHeight
     * @param hints
     * @param isShowText
     * @param textSize
     * @param codeColor
     * @return
     */
    @JvmStatic
    fun createBarCode(
        content: String,
        format: BarcodeFormat,
        desiredWidth: Int,
        desiredHeight: Int,
        hints: Map<EncodeHintType, *>?,
        isShowText: Boolean,
        textSize: Int,
        @ColorInt codeColor: Int
    ): Bitmap? {
        if (TextUtils.isEmpty(content)) {
            return null
        }
        val WHITE = Color.WHITE

        val writer = MultiFormatWriter()
        try {
            val result = writer.encode(
                content, format, desiredWidth,
                desiredHeight, hints
            )
            val width = result.width
            val height = result.height
            val pixels = IntArray(width * height)
            // All are 0, or black, by default
            for (y in 0 until height) {
                val offset = y * width
                for (x in 0 until width) {
                    pixels[offset + x] = if (result[x, y]) codeColor else WHITE
                }
            }

            val bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.ARGB_8888
            )
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
            return if (isShowText) {
                addCode(bitmap, content, textSize, codeColor, textSize / 2)
            } else bitmap
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 条形码下面添加文本信息
     *
     * @param src
     * @param code
     * @param textSize
     * @param textColor
     * @return
     */
    private fun addCode(
        src: Bitmap?,
        code: String?,
        textSize: Int,
        @ColorInt textColor: Int,
        offset: Int
    ): Bitmap? {
        if (src == null) {
            return null
        }

        if (TextUtils.isEmpty(code)) {
            return src
        }

        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height

        if (srcWidth <= 0 || srcHeight <= 0) {
            return null
        }

        val bitmap: Bitmap?
        try {
            bitmap = Bitmap.createBitmap(
                srcWidth,
                srcHeight + textSize + offset * 2,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            val paint = TextPaint()
            paint.textSize = textSize.toFloat()
            paint.color = textColor
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(code!!, srcWidth / 2f, srcHeight + textSize / 2f + offset, paint)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        return bitmap
    }
}