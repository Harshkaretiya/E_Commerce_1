package com.example.e_commerce1

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @FormUrlEncoded
    @POST("insertuser.php")
    fun insertuser(
        @Field("name") user_name:String,
        @Field("email") user_email:String,
        @Field("password") user_password:String,
        @Field("image") user_image:String,
        @Field("number") user_number:String,
    ): Call<Void>

    @GET("userview.php")
    fun getuser(
        @Query("email") user_email:String,
        @Query("password") user_password:String,
    ): Call<ModelUser>

    @GET("useridview.php")
    fun getiduser(
        @Query("uid") user_id:Int,
    ): Call<ModelUser>


    @FormUrlEncoded
    @POST("userupdate.php")
    fun updatedata(
        @Field("uid") user_id:Int,
        @Field("name") user_name:String,
        @Field("email") user_email:String,
        @Field("image") user_image:String,
        @Field("number") user_number:String,
    ): Call<Void>

    @GET("dataview.php")
    fun getdata():Call<List<Model>>

    @GET("dataidview.php")
    fun getiddata(
        @Query("pid") product_id:Int,
    ): Call<Model>

    @GET("favidview.php")
    fun getidfav(
        @Query("uid") user_id:Int,
    ): Call<List<Model>>

    @FormUrlEncoded
    @POST("favinsert.php")
    fun insertfav(
        @Field("pid") product_id:Int,
        @Field("uid") user_id:Int,
    ): Call<Void>

    @GET("checkfav.php")
    fun getcheckfav(
        @Query("pid") product_id:Int,
        @Query("uid") user_id:Int,
    ): Call<Model>
    @GET("favdelete.php")
    fun getfavdelete(
        @Query("pid") product_id:Int,
        @Query("uid") user_id:Int,
    ): Call<Void>

    @FormUrlEncoded
    @POST("cartinsert.php")
    fun cartinsert(
        @Field("pid") product_id:Int,
        @Field("uid") user_id:Int,
        @Field("qty") product_quantity:Int,
    ): Call<Void>

    @GET("cartdelete.php")
    fun cartdelete(
        @Query("pid") product_id:Int,
        @Query("uid") user_id:Int,
    ): Call<Void>

    @GET("cartidview.php")
    fun cartidview(
        @Query("uid") user_id:Int,
    ): Call<List<Model>>

    @GET("cartcheck.php")
    fun cartcheck(
        @Query("pid") product_id:Int,
        @Query("uid") user_id:Int,
    ): Call<Model>

    @FormUrlEncoded
    @POST("cartupdate.php")
    fun cartupdate(
        @Field("uid") user_id:Int,
        @Field("pid") product_id: Int,
        @Field("qty") product_quantity: Int,
    ): Call<Void>
}