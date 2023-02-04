package com.example.e_commerce1

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce1.databinding.FragmentFavouriteBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavouriteFragment : Fragment() {

    private var _binding : FragmentFavouriteBinding? = null
    private val binding get() = _binding
    lateinit var list : MutableList<Model>
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val view = binding!!.root

        list = ArrayList()
        binding!!.emptyText.visibility = View.GONE
        binding!!.progressBar.visibility = View.VISIBLE

        val manager : RecyclerView.LayoutManager = LinearLayoutManager(requireActivity())
        binding!!.favouriteList.layoutManager=manager


        sharedPreferences = requireContext().getSharedPreferences("User_Session", Context.MODE_PRIVATE)
        
        val uid = sharedPreferences.getInt("uid",101)
        
        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        val call: Call<List<Model>> = apiInterface.getidfav(uid)
        call.enqueue(object: Callback<List<Model>>
        {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call<List<Model>>, response: Response<List<Model>>) {


                if (response.body() == null || response.body()!!.isEmpty())
                {
                    if (context!=null)
                    binding!!.emptyText.visibility = View.VISIBLE
                }
                if (context != null) {
                    list = response.body() as MutableList<Model>
                    binding!!.progressBar.visibility = View.GONE
                    val adapter = RecyclerListAdapter(requireActivity(), list,"favourite")
                    binding!!.favouriteList.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<Model>>, t: Throwable) {
                Toast.makeText(requireActivity(),"No Internet", Toast.LENGTH_LONG).show()
            }
        })




        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
