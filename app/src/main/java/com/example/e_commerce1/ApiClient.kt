package com.example.e_commerce1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {
        var BASE_URL = "https://hk123234345.000webhostapp.com/ProjectTest/"

        var retrofit: Retrofit? = null

        fun getapiclient(): Retrofit {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }



            return retrofit!!

        }
    }
}