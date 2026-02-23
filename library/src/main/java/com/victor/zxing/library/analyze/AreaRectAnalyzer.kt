package com.victor.zxing.library.analyze

import com.google.zxing.DecodeHintType
import com.google.zxing.Result
import com.victor.zxing.library.DecodeConfig
import com.victor.zxing.library.DecodeFormatManager

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: AreaRectAnalyzer
 * Author: Victor
 * Date: 2026/2/23 14:44
 * Description: 矩阵区域分析器：主要用于锁定具体的识别区域
 * -----------------------------------------------------------------
 */

abstract class AreaRectAnalyzer(config: DecodeConfig?) : ImageAnalyzer() {

    var decodeConfig: DecodeConfig? = config
        private set

    var hints: Map<DecodeHintType, *>? = null
        private set

    var isMultiDecode = true
        private set

    private var areaRectRatio = DecodeConfig.DEFAULT_AREA_RECT_RATIO
    private var areaRectHorizontalOffset = 0
    private var areaRectVerticalOffset = 0

    init {
        if (config != null) {
            hints = config.hints
            isMultiDecode = config.isMultiDecode
            areaRectRatio = config.areaRectRatio
            areaRectHorizontalOffset = config.areaRectHorizontalOffset
            areaRectVerticalOffset = config.areaRectVerticalOffset
        } else {
            hints = DecodeFormatManager.DEFAULT_HINTS
        }
    }

    override fun analyze(data: ByteArray, width: Int, height: Int): Result? {
        if (decodeConfig != null) {
            if (decodeConfig!!.isFullAreaScan) {
                // decodeConfig为空或者支持全区域扫码识别时，直接使用全区域进行扫码识别
                return analyze(data, width, height, 0, 0, width, height)
            }

            val rect = decodeConfig!!.analyzeAreaRect
            if (rect != null) { // 如果分析区域不为空，则使用指定的区域进行扫码识别
                if (rect.width() > 0 && rect.height() > 0) {
                    return analyze(
                        data,
                        width,
                        height,
                        rect.left,
                        rect.top,
                        rect.width(),
                        rect.height()
                    )
                }
            }
        }

        // 如果分析区域为空，则通过识别区域比例和相关的偏移量计算出最终的区域进行扫码识别
        val size = (Math.min(width, height) * areaRectRatio).toInt()
        val left = (width - size) / 2 + areaRectHorizontalOffset
        val top = (height - size) / 2 + areaRectVerticalOffset

        return analyze(data, width, height, left, top, size, size)
    }

    abstract fun analyze(
        data: ByteArray,
        dataWidth: Int,
        dataHeight: Int,
        left: Int,
        top: Int,
        width: Int,
        height: Int
    ): Result?
}