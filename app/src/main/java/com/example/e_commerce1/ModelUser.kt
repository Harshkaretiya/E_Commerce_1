package com.example.e_commerce1

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelUser {
    @Expose
    @SerializedName("uid")
    var uid=0

    @Expose
    @SerializedName("name")
    var name=""

    @Expose
    @SerializedName("email")
    var email=""

    @Expose
    @SerializedName("password")
    var password=""

    @Expose
    @SerializedName("image")
    var image=""

    @Expose
    @SerializedName("number")
    var number=""

}
