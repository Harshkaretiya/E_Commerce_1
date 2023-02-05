package com.example.e_commerce1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce1.databinding.ActivityProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    lateinit var list : MutableList<Model>
    lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = ArrayList()
        var manager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding!!.productList.layoutManager=manager

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<List<Model>> = apiInterface.getdata()
        call.enqueue(object: Callback<List<Model>>
        {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {

                if (this != null) {
                    list = response.body() as MutableList<Model>

                    var adapter = RecyclerListAdapter(this@ProductActivity, list,"product")
                    binding!!.productList.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@ProductActivity,"No Internet", Toast.LENGTH_LONG).show()
            }
        })

    }
    override fun onRestart() {
        super.onRestart()

        // Get the current position of the RecyclerView
        val currentPosition = (binding.productList.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

        // Refetch the data and refresh the list
        val call = apiInterface.getdata()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful) {
                    list = response.body() as MutableList<Model>
                    val adapter = RecyclerListAdapter(this@ProductActivity, list, "product")
                    binding.productList.adapter = adapter

                    // Scroll to the previously saved position
                    (binding.productList.layoutManager as LinearLayoutManager).scrollToPosition(currentPosition)
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@ProductActivity, "No Internet", Toast.LENGTH_LONG).show()
            }
        })
    }
}