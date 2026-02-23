package com.victor.zxing.library.analyze

import android.graphics.ImageFormat
import androidx.camera.core.ImageProxy
import com.google.zxing.Result
import com.victor.camera.preview.lib.AnalyzeResult
import com.victor.camera.preview.lib.FrameMetadata
import com.victor.camera.preview.lib.analyze.Analyzer
import com.victor.camera.preview.lib.util.ImageUtils
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

/*
 * -----------------------------------------------------------------
 * Copyright (C) 2025-2035, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: ImageAnalyzer
 * Author: Victor
 * Date: 2026/2/23 14:43
 * Description: 图像分析器
 * -----------------------------------------------------------------
 */

abstract class ImageAnalyzer : Analyzer<Result> {

    private val queue = ConcurrentLinkedQueue<ByteArray>()

    private val joinQueue = AtomicBoolean(false)

    /**
     * 分析图像数据
     *
     * @param data
     * @param width
     * @param height
     */
    abstract fun analyze(data: ByteArray, width: Int, height: Int): Result?

    override fun analyze(imageProxy: ImageProxy, listener: Analyzer.OnAnalyzeListener<Result>) {

        if (!joinQueue.get()) {
            val imageSize = imageProxy.width * imageProxy.height
            val bytes = ByteArray(imageSize + 2 * (imageSize / 4))
            queue.add(bytes)
            joinQueue.set(true)
        }

        val nv21Data = queue.poll() ?: return

        try {
            val rotation = imageProxy.imageInfo.rotationDegrees
            val width = imageProxy.width
            val height = imageProxy.height

            ImageUtils.yuv_420_888toNv21(imageProxy, nv21Data)

            val result = if (rotation == 90 || rotation == 270) {
                val rotatedData = ByteArray(nv21Data.size)
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        rotatedData[x * height + height - y - 1] = nv21Data[x + y * width]
                    }
                }
                analyze(rotatedData, height, width)
            } else {
                analyze(nv21Data, width, height)
            }

            if (result != null) {
                val frameMetadata = FrameMetadata(
                    width,
                    height,
                    rotation
                )
                joinQueue.set(false)
                listener.onSuccess(AnalyzeResult(nv21Data, ImageFormat.NV21, frameMetadata, result))
            } else {
                queue.add(nv21Data)
                listener.onFailure(null)
            }

        } catch (e: Exception) {
            queue.add(nv21Data)
            listener.onFailure(null)
        }
    }
}