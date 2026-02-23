package com.victor.zxing.library.analyze

import android.util.Log
import com.google.zxing.*
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.common.HybridBinarizer
import com.victor.zxing.library.DecodeConfig

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: MultiFormatAnalyzer
 * Author: Victor
 * Date: 2026/2/23 14:48
 * Description: 多格式分析器：主要用于分析识别条形码/二维码
 * -----------------------------------------------------------------
 */


class MultiFormatAnalyzer : AreaRectAnalyzer {

    private val reader = MultiFormatReader()

    constructor() : this(null as DecodeConfig?)

    constructor(decodeHints: Map<DecodeHintType, Any>) : this(DecodeConfig().apply {
        hints = decodeHints
    })

    constructor(config: DecodeConfig?) : super(config)

    override fun analyze(
        data: ByteArray,
        dataWidth: Int,
        dataHeight: Int,
        left: Int,
        top: Int,
        width: Int,
        height: Int
    ): Result? {
        var rawResult: Result? = null
        try {
            val start = System.currentTimeMillis()
            reader.setHints(hints)
            val source = PlanarYUVLuminanceSource(
                data,
                dataWidth,
                dataHeight,
                left,
                top,
                width,
                height,
                false
            )
            rawResult = decodeInternal(source, isMultiDecode)
            Log.d(javaClass.simpleName, "analyze-rawResult = $rawResult")

            if (rawResult == null && decodeConfig != null) {
                if (decodeConfig!!.isSupportVerticalCode) {
                    val rotatedData = ByteArray(data.size)
                    for (y in 0 until dataHeight) {
                        for (x in 0 until dataWidth) {
                            rotatedData[x * dataHeight + dataHeight - y - 1] =
                                data[x + y * dataWidth]
                        }
                    }
                    rawResult = decodeInternal(
                        PlanarYUVLuminanceSource(
                            rotatedData,
                            dataHeight,
                            dataWidth,
                            top,
                            left,
                            height,
                            width,
                            false
                        ),
                        decodeConfig!!.isSupportVerticalCodeMultiDecode
                    )
                }

                if (rawResult == null && decodeConfig!!.isSupportLuminanceInvert) {
                    rawResult = decodeInternal(
                        source.invert(),
                        decodeConfig!!.isSupportLuminanceInvertMultiDecode
                    )
                }
            }
            if (rawResult != null) {
                val end = System.currentTimeMillis()
                Log.d(javaClass.simpleName, "Found barcode in ${end - start} ms")
            }
        } catch (ignored: Exception) {
        } finally {
            reader.reset()
        }
        return rawResult
    }

    private fun decodeInternal(source: LuminanceSource, isMultiDecode: Boolean): Result? {
        var result: Result? = null
        try {
            try {
                // 采用HybridBinarizer解析
                result = reader.decodeWithState(BinaryBitmap(HybridBinarizer(source)))
            } catch (ignored: Exception) {
            }
            if (isMultiDecode && result == null) {
                // 如果没有解析成功，再采用GlobalHistogramBinarizer解析一次
                result = reader.decodeWithState(BinaryBitmap(GlobalHistogramBinarizer(source)))
            }
        } catch (ignored: Exception) {
        }
        return result
    }
}