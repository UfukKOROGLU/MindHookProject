package com.example.project

import android.util.Log
import android.widget.TextView

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

@androidx.camera.core.ExperimentalGetImage
class FaceAnalyzer(
    private val feedbackView: TextView,
    private val onFaceDetected: (Boolean) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
    )

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (faces.isNotEmpty()) {
                        feedbackView.text = "✅ Face detected!"
                        onFaceDetected(true)
                    } else {
                        feedbackView.text = "❌ No face detected..."
                        onFaceDetected(false)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FaceAnalyzer", "Detection failed: ${e.message}")
                    onFaceDetected(false)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}