package com.victor.zxing.library

import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import java.util.EnumMap


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: DecodeFormatManager
 * Author: Victor
 * Date: 2026/2/23 14:32
 * Description: 解码格式管理器,将常见的一些解码配置已根据条形码类型进行了几大划分，可根据需要找到符合的划分配置类型直接使用。
 * -----------------------------------------------------------------
 */

object DecodeFormatManager {

    /**
     * 所有的支持的条码
     */
    val ALL_HINTS: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)

    /**
     * CODE_128 (最常用的一维码)
     */
    val CODE_128_HINTS: Map<DecodeHintType, Any> = createDecodeHint(BarcodeFormat.CODE_128)

    /**
     * QR_CODE (最常用的二维码)
     */
    val QR_CODE_HINTS: Map<DecodeHintType, Any> = createDecodeHint(BarcodeFormat.QR_CODE)

    /**
     * 一维码
     */
    val ONE_DIMENSIONAL_HINTS: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)

    /**
     * 二维码
     */
    val TWO_DIMENSIONAL_HINTS: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)

    /**
     * 默认
     */
    val DEFAULT_HINTS: MutableMap<DecodeHintType, Any> = EnumMap(DecodeHintType::class.java)

    init {
        //all hints
        addDecodeHintTypes(ALL_HINTS, getAllFormats())
        //one dimension
        addDecodeHintTypes(ONE_DIMENSIONAL_HINTS, getOneDimensionalFormats())
        //Two dimension
        addDecodeHintTypes(TWO_DIMENSIONAL_HINTS, getTwoDimensionalFormats())
        //default hints
        addDecodeHintTypes(DEFAULT_HINTS, getDefaultFormats())
    }

    /**
     * 所有支持的条码格式，具体格式可查看：[BarcodeFormat]
     *
     * @return 所有支持的条码格式
     */
    private fun getAllFormats(): List<BarcodeFormat> {
        return listOf(
            BarcodeFormat.AZTEC,
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.QR_CODE,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION
        )
    }

    /**
     * 一维码
     *
     * 包括如下几种格式：
     * [BarcodeFormat.CODABAR]
     * [BarcodeFormat.CODE_39]
     * [BarcodeFormat.CODE_93]
     * [BarcodeFormat.CODE_128]
     * [BarcodeFormat.EAN_8]
     * [BarcodeFormat.EAN_13]
     * [BarcodeFormat.ITF]
     * [BarcodeFormat.RSS_14]
     * [BarcodeFormat.RSS_EXPANDED]
     * [BarcodeFormat.UPC_A]
     * [BarcodeFormat.UPC_E]
     * [BarcodeFormat.UPC_EAN_EXTENSION]
     *
     * @return 需要支持的一维码格式
     */
    private fun getOneDimensionalFormats(): List<BarcodeFormat> {
        return listOf(
            BarcodeFormat.CODABAR,
            BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.ITF,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION
        )
    }

    /**
     * 二维码，具体格式可查看：[BarcodeFormat]
     *
     * 包括如下几种格式：
     * [BarcodeFormat.AZTEC]
     * [BarcodeFormat.DATA_MATRIX]
     * [BarcodeFormat.MAXICODE]
     * [BarcodeFormat.PDF_417]
     * [BarcodeFormat.QR_CODE]
     *
     * @return 需要支持的二维码格式
     */
    private fun getTwoDimensionalFormats(): List<BarcodeFormat> {
        return listOf(
            BarcodeFormat.AZTEC,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.MAXICODE,
            BarcodeFormat.PDF_417,
            BarcodeFormat.QR_CODE
        )
    }

    /**
     * 默认支持的格式
     *
     * 包括如下几种格式：
     * [BarcodeFormat.QR_CODE]
     * [BarcodeFormat.UPC_A]
     * [BarcodeFormat.EAN_13]
     * [BarcodeFormat.CODE_128]
     *
     * @return 默认支持的格式
     */
    private fun getDefaultFormats(): List<BarcodeFormat> {
        return listOf(
            BarcodeFormat.QR_CODE,
            BarcodeFormat.UPC_A,
            BarcodeFormat.EAN_13,
            BarcodeFormat.CODE_128
        )
    }

    /**
     * 支持解码的格式
     *
     * @param barcodeFormats [BarcodeFormat]
     * @return 返回添加了通用配置后的解码支持类型与配置
     */
    @JvmStatic
    fun createDecodeHints(vararg barcodeFormats: BarcodeFormat): Map<DecodeHintType, Any> {
        val hints = EnumMap<DecodeHintType, Any>(DecodeHintType::class.java)
        addDecodeHintTypes(hints, barcodeFormats.toList())
        return hints
    }

    /**
     * 支持解码的格式
     *
     * @param barcodeFormat [BarcodeFormat]
     * @return 返回添加了通用配置后的解码支持类型与配置
     */
    @JvmStatic
    fun createDecodeHint(barcodeFormat: BarcodeFormat): Map<DecodeHintType, Any> {
        val hints = EnumMap<DecodeHintType, Any>(DecodeHintType::class.java)
        addDecodeHintTypes(hints, listOf(barcodeFormat))
        return hints
    }

    /**
     * 为解码配置添加一些通用配置
     * @param hints 解码支持类型与配置
     * @param formats 需要支持的解码格式
     */
    private fun addDecodeHintTypes(
        hints: MutableMap<DecodeHintType, Any>,
        formats: List<BarcodeFormat>
    ) {
        // Image is known to be of one of a few possible formats.
        hints[DecodeHintType.POSSIBLE_FORMATS] = formats
        // Spend more time to try to find a barcode; optimize for accuracy, not speed.
        hints[DecodeHintType.TRY_HARDER] = true
        // Specifies what character encoding to use when decoding, where applicable (type String)
        hints[DecodeHintType.CHARACTER_SET] = "UTF-8"
    }
}