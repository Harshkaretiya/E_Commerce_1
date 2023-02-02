package com.example.e_commerce1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce1.databinding.FragmentHomeBinding
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding
    lateinit var list : MutableList<Model>
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPreferences: SharedPreferences
//    var images = listOf("https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg","https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg","https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg")
    lateinit var sliderView: SliderView


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding!!.root

        var manager : RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding!!.listItem.layoutManager=manager


        sharedPreferences = requireContext().getSharedPreferences("User_Session",Context.MODE_PRIVATE)

        list = ArrayList()


        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        var call: Call<List<Model>> = apiInterface.getdata()
        call.enqueue(object: Callback<List<Model>>
        {
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {

                if (context != null) {
                    list = response.body() as MutableList<Model>

                    var adapter = RecyclerListAdapter(requireActivity(), list.take(5) as MutableList<Model>)
                    binding!!.listItem.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(requireActivity(),"No Internet",Toast.LENGTH_LONG).show()
            }
        })



        var name = sharedPreferences.getString("name","")

        binding!!.nameText.setText(name)

        var images = ArrayList<String>()
        images.add("https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg")
        images.add("https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg")
        images.add("https://hk123234345.000webhostapp.com/ProjectTest/data/burger.jpg")

        sliderView = view.findViewById(R.id.image_slider)
        val imageSlider = ImageSlider(images)
        sliderView.setSliderAdapter(imageSlider)
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        sliderView.scrollTimeInSec = 5
        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        sliderView.startAutoCycle()

        binding!!.allLayout.setOnClickListener {
            startActivity(Intent(requireActivity(),ProductActivity::class.java))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
