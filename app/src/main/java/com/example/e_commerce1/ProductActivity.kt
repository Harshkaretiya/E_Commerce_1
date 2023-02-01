package com.example.e_commerce1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.e_commerce1.databinding.ActivityProductBinding
import com.example.e_commerce1.databinding.ActivityProductViewBinding
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

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<List<Model>> = apiInterface.getdata()
        call.enqueue(object: Callback<List<Model>>
        {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {

                if (this != null) {
                    list = response.body() as MutableList<Model>

                    var adapter = ListAdapter(this@ProductActivity, list)
                    binding!!.productList.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@ProductActivity,"No Internet", Toast.LENGTH_LONG).show()
            }
        })

        binding!!.productList.setOnItemClickListener { adapterView, view, i, l ->
            var a = adapterView.getItemAtPosition(i).toString().toInt()

            var i = Intent(this,ProductViewActivity::class.java)
            i.putExtra("pid",list[a].pid)
            startActivity(i)
        }
    }
}