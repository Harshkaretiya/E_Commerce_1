package com.example.e_commerce1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce1.databinding.ActivityLoginBinding
import com.example.e_commerce1.databinding.ActivityProductViewBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductViewBinding
    lateinit var apiInterface : ApiInterface
    lateinit var sliderView: SliderView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //--------------------------------------------------------------------------------//
        //regular code

        var m = MainActivity()
        m.setStatusBarColor(this, Color.parseColor("#F5F5F5"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        sharedPreferences = getSharedPreferences("User_Session", Context.MODE_PRIVATE)
        val uid = sharedPreferences.getInt("uid",101)

        var isFavourite = false

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

        val i = intent
        val pid = i.getIntExtra("pid",9999999)

        //---------------------------------------------------------------------------
        //set data to product view page

        val call: Call<Model> = apiInterface.getiddata(pid)
        call.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {

                if (response.isSuccessful && pid == response.body()?.pid)
                {
                    binding.productName.text = response.body()!!.name
                    binding.productPrice.text = response.body()!!.price
                    binding.productDesc.text = response.body()!!.desc
                    binding.productWeight.text = response.body()!!.weight

                    var img1 = response.body()!!.image
                    var img2 = response.body()!!.image2
                    var img3 = response.body()!!.image3
                    var img4 = response.body()!!.image4
                    var img5 = response.body()!!.image5

                    var images = ArrayList<String>()
                    if (img1 != "") images.add(img1)
                    if (img2 != "") images.add(img2)
                    if (img3 != "") images.add(img3)
                    if (img4 != "") images.add(img4)
                    if (img5 != "") images.add(img5)
                    sliderView = findViewById(R.id.product_view_slider)
                    val imageSlider = ImageSlider(images)
                    sliderView.setSliderAdapter(imageSlider)
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)

                    if (img2 == "" && img3 =="" && img4 =="" && img5=="")
                    {
                        sliderView.setInfiniteAdapterEnabled(false)
                    }
                }
            }

            override fun onFailure(call: Call<Model>, t: Throwable) {
                Toast.makeText(applicationContext,"Something went wrong", Toast.LENGTH_LONG).show()
            }

        })

        //---------------------------------------------------------------------------
        //check if product is favourite or not and if then set clickonevent


        var call2: Call<Model> = apiInterface.getcheckfav(pid,uid)
        call2.enqueue(object : Callback<Model> {
            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                binding.favourite.setImageResource(R.drawable.heartfilled)
                isFavourite = true
            }
            override fun onFailure(call: Call<Model>, t: Throwable) {
            }
        })

        binding.favouriteLayout.setOnClickListener {
            if (isFavourite) {
                var call: Call<Void> = apiInterface.getfavdelete(pid,uid)
                call!!.enqueue(object:Callback<Void?>{
                    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                        Toast.makeText(this@ProductViewActivity, "Removed from favourite", Toast.LENGTH_SHORT).show()
                        isFavourite = false
                        binding.favourite.setImageResource(R.drawable.heart)
                    }
                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(this@ProductViewActivity,"Error",Toast.LENGTH_LONG).show()
                    }
                })
            }
            else if(!isFavourite) {
                val call: Call<Void> = apiInterface.insertfav(pid, uid)
                call.enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        binding.favourite.setImageResource(R.drawable.heartfilled)
                        isFavourite = true
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@ProductViewActivity, "Fail", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        //------------------------------------------------------------------------------------------
        //set grid view item

        var manager : RecyclerView.LayoutManager = GridLayoutManager(this,10)
        binding!!.gridItem.layoutManager=manager

        var call3: Call<List<Model>> = apiInterface.getdata()
        call3.enqueue(object: Callback<List<Model>>
        {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {

                if (this != null) {
                    var list = response.body() as MutableList<Model>

                    val adapter3 = RecyclerGridAdapter(this@ProductViewActivity, list)
                    binding.gridItem.adapter = adapter3
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@ProductViewActivity,"No Internet", Toast.LENGTH_LONG).show()
            }
        })

        //-------------------------------------------------------------------------------------
        //insert product to cart

        var qty = binding.quantitytext.text.toString().toInt()

        binding.addToCart.setOnClickListener {
            var call4 : Call<Void> = apiInterface.cartinsert(pid,uid,qty)
            call4.enqueue(object : Callback<Void>{
                override fun onResponse(call: Call<Void>, response: Response<Void>) {

                    Toast.makeText(applicationContext, "inserted", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    //----------------------------------------------------------------------
    //on back press function

    override fun onBackPressed() {
        var i = intent
        var cpage = i.getStringExtra("cpage").toString()
        if (cpage == "favourite") {
            var k = Intent(this,MainActivity::class.java)
            k.putExtra("page","favourite")
            startActivity(k)
        }
        if (cpage == "home") {
            var k = Intent(this,MainActivity::class.java)
            k.putExtra("page","home")
            startActivity(k)
        }
        else {
            super.onBackPressed()
        }
    }

    //-------------------------------------------------------------------------------------
    //on back backpressed the list will be refreshed
    override fun onRestart() {
        super.onRestart()

        // Get the current position of the RecyclerView
        val currentPosition = (binding.gridItem.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()

        // Refetch the data and refresh the list
        val call = apiInterface.getdata()
        call.enqueue(object : Callback<List<Model>> {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {
                if (response.isSuccessful) {
                    var list = response.body() as MutableList<Model>
                    val adapter = RecyclerGridAdapter(this@ProductViewActivity, list)
                    binding.gridItem.adapter = adapter

                    // Scroll to the previously saved position
                    (binding.gridItem.layoutManager as GridLayoutManager).scrollToPosition(currentPosition)
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(this@ProductViewActivity, "No Internet", Toast.LENGTH_LONG).show()
            }
        })
    }
}