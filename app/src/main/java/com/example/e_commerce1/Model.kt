package com.example.e_commerce1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Model
{
    @Expose
    @SerializedName("pid")
    var pid = 0

    @Expose
    @SerializedName("name")
    var name = ""

    @Expose
    @SerializedName("price")
    var price = ""

    @Expose
    @SerializedName("image")
    var image = ""
    @Expose
    @SerializedName("image2")
    var image2 = ""
    @Expose
    @SerializedName("image3")
    var image3 = ""
    @Expose
    @SerializedName("image4")
    var image4 = ""
    @Expose
    @SerializedName("image5")
    var image5 = ""

    @Expose
    @SerializedName("weight")
    var weight = ""

    @Expose
    @SerializedName("description")
    var desc = ""

    @Expose
    @SerializedName("qty")
    var qty = 0

//    @Expose
//    @SerializedName("uid")
//    var uid = 0
}