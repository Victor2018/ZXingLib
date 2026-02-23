package com.victor.zxing.library.analyze

import com.google.zxing.DecodeHintType
import com.google.zxing.Reader
import com.google.zxing.qrcode.QRCodeReader
import com.victor.zxing.library.DecodeConfig
import com.victor.zxing.library.DecodeFormatManager

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: QRCodeAnalyzer
 * Author: Victor
 * Date: 2026/2/23 14:50
 * Description: 二维码分析器
 * -----------------------------------------------------------------
 */

class QRCodeAnalyzer : BarcodeFormatAnalyzer {

    constructor() : this(DecodeConfig().apply { hints = DecodeFormatManager.QR_CODE_HINTS })

    constructor(decodeHints: Map<DecodeHintType, Any>) : this(DecodeConfig().apply {
        hints = decodeHints
    })

    constructor(config: DecodeConfig?) : super(config)

    override fun createReader(): Reader {
        return QRCodeReader()
    }
}