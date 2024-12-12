package com.example.jejakrempah.data

import com.google.gson.annotations.SerializedName

data class DetectionResult(
    @SerializedName("rempah") val rempah: String,
    @SerializedName("funfact1") val funfact1: String?,
    @SerializedName("funfact2") val funfact2: String?,
    @SerializedName("manfaat") val manfaat: String,
    @SerializedName("link") val link: String,
    @SerializedName("confidence") val confidence: Double
)
