package com.victor.zxing.library

import android.graphics.Rect
import com.google.zxing.DecodeHintType


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DecodeConfig
 * Author: Victor
 * Date: 2026/2/23 14:25
 * Description: 解码配置：主要用于在扫码识别时，提供一些配置，便于扩展。通过配置可决定内置分析器的能力，从而间接的控制并简化扫码识别的流程
 * -----------------------------------------------------------------
 */

class DecodeConfig {
    var hints: Map<DecodeHintType, Any> = DecodeFormatManager.DEFAULT_HINTS

    companion object {
        const val DEFAULT_AREA_RECT_RATIO: Float = 0.8f
    }

    /**
     * 是否支持使用多解码
     */
    var isMultiDecode = true

    /**
     * 是否支持识别反色码（条码黑白颜色反转的码）
     */
    var isSupportLuminanceInvert = false

    /**
     * 是否支持识别反色码（条码黑白颜色反转的码）使用多解码
     */
    var isSupportLuminanceInvertMultiDecode = false

    /**
     * 是否支持垂直的条码
     */
    var isSupportVerticalCode = false

    /**
     * 是否支持垂直的条码，使用多解码
     */
    var isSupportVerticalCodeMultiDecode = false

    /**
     * 需要分析识别区域
     */
    var analyzeAreaRect: Rect? = null

    /**
     * 是否支持全区域扫码识别
     */
    var isFullAreaScan = false

    /**
     * 识别区域比例，默认0.8
     */
    var areaRectRatio = DEFAULT_AREA_RECT_RATIO

    /**
     * 识别区域垂直方向偏移量
     */
    var areaRectVerticalOffset = 0

    /**
     * 识别区域水平方向偏移量
     */
    var areaRectHorizontalOffset = 0

}