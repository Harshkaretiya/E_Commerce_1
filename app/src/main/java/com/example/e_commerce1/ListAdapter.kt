package com.example.e_commerce1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListAdapter (var context: Context, var list: MutableList<Model>) : BaseAdapter()
{
    private lateinit var apiInterface: ApiInterface
    lateinit var sharedPreferences: SharedPreferences
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var inflater : LayoutInflater = LayoutInflater.from(context)
        var view= inflater.inflate(R.layout.list_design,p2,false)

        var product_img : ImageView =view.findViewById(R.id.product_img)
        var name : TextView =view.findViewById(R.id.product_name)
        var price : TextView = view.findViewById(R.id.product_price)
        var descrition : TextView = view.findViewById(R.id.product_desc)

        sharedPreferences = context.getSharedPreferences("User_Session",Context.MODE_PRIVATE)
        val uid = sharedPreferences.getInt("uid",101)
        var favourite : ImageView = view.findViewById(R.id.favourite)
        var isFavourite = false



        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<Model> = apiInterface.getcheckfav(list[p0].pid,uid)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                    favourite.setImageResource(R.drawable.heartselected)
                    isFavourite = true
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
            }

        })
        favourite.setOnClickListener {

            if (isFavourite)
            {
                var call: Call<Void> = apiInterface.getfavdelete(list[p0].pid,uid)
                call!!.enqueue(object:Callback<Void?>{
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show()
                        isFavourite = false
                        favourite.setImageResource(R.drawable.heart)
                        var i = Intent(context,MainActivity::class.java)
                        i.putExtra("page","favourite")
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(i)
                    }
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
                    }
                })
            }
            else if(!isFavourite) {
                val call: Call<Void> = apiInterface.insertfav(list[p0].pid, uid)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        favourite.setImageResource(R.drawable.heartselected)
                        isFavourite = true
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
                    }

                })
            }
        }


        Picasso.get().load(list[p0].image).placeholder(R.mipmap.ic_launcher).into(product_img)
        name.setText(list[p0].name)
        price.setText(list[p0].price)
        descrition.setText(list[p0].desc)
        return  view
    }
}