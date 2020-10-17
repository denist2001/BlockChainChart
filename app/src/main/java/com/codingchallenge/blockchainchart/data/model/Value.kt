package com.codingchallenge.blockchainchart.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Value(
    @SerializedName("x")
    @Expose
    val x: Long,
    @SerializedName("y")
    @Expose
    val y: Float
)