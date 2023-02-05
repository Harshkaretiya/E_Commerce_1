package com.example.e_commerce1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecyclerGridAdapter(var context: Context, var list: MutableList<Model>) : RecyclerView.Adapter<MyView2>()
{
    private lateinit var apiInterface: ApiInterface
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView2 {
        var data = LayoutInflater.from(context)
        var view = data.inflate(R.layout.grid_design,parent,false)
        return MyView2(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyView2, @SuppressLint("RecyclerView") position: Int) {

        sharedPreferences = context.getSharedPreferences("User_Session", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getInt("uid",101)

        var isFavourite = false

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<Model> = apiInterface.getcheckfav(list[position].pid,uid)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                holder.favourite.setImageResource(R.drawable.heartfilled)
                isFavourite = true
            }
            override fun onFailure(call: Call<Model>, t: Throwable) {
            }
        })

        holder.favouritelayout.setOnClickListener {
            if (isFavourite) {
                var call: Call<Void> = apiInterface.getfavdelete(list[position].pid,uid)
                call!!.enqueue(object: Callback<Void?> {
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Toast.makeText(context,"Deleted", Toast.LENGTH_LONG).show()
                        isFavourite = false
                        holder.favourite.setImageResource(R.drawable.heart)
                    }
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(context,"Error", Toast.LENGTH_LONG).show()
                    }
                })
            }
            else if(!isFavourite) {
                val call: Call<Void> = apiInterface.insertfav(list[position].pid, uid)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        holder.favourite.setImageResource(R.drawable.heartfilled)
                        isFavourite = true
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        holder.itemView.setOnClickListener {
            var i = Intent(context,ProductViewActivity::class.java)
            i.putExtra("pid",list[position].pid)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
        }
        Picasso.get().load(list[position].image).placeholder(R.mipmap.ic_launcher).into(holder.product_img)
        holder.name.setText(list[position].name)
        holder.price.setText(list[position].price)
    }
}
class MyView2(itemView: View) : RecyclerView.ViewHolder(itemView)
{
    var product_img : ImageView =itemView.findViewById(R.id.product_img)
    var name : TextView =itemView.findViewById(R.id.product_name)
    var price : TextView = itemView.findViewById(R.id.product_price)
    var favourite : ImageView = itemView.findViewById(R.id.favourite)
    var favouritelayout : CardView = itemView.findViewById(R.id.favouriteLayout)
}
