package com.codingchallenge.blockchainchart.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
{
    "status": "ok",
    "name": "Market Price (USD)",
    "unit": "USD",
    "period": "day",
    "description": "Average USD market price across major bitcoin exchanges.",
    "values": [
        {
            "x": 1599955200,
            "y": 10446.44
        },
        {
            "x": 1600041600,
            "y": 10330.77
        }
    ]
}
 */
data class ApiResponse(
    @SerializedName("status")
    @Expose
    val status: String,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("unit")
    @Expose
    val unit: String,
    @SerializedName("period")
    @Expose
    val period: String,
    @SerializedName("description")
    @Expose
    val description: String,
    @SerializedName("values")
    @Expose
    val values: List<BitCoinValue>
)