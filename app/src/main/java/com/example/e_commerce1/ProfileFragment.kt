package com.example.e_commerce1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.e_commerce1.databinding.FragmentProfileBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding
    lateinit var apiInterface : ApiInterface
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding!!.root

        //getting data from shared preference
        
        sharedPreferences = requireContext().getSharedPreferences("User_Session",Context.MODE_PRIVATE)

        val uid = sharedPreferences.getInt("uid",101)
        
        //placing image from dataabase
        //using retrofit

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        val call: Call<ModelUser> = apiInterface.getiduser(uid)
        call.enqueue(object : Callback<ModelUser> {
            override fun onResponse(call: Call<ModelUser>, response: Response<ModelUser>) {
                if (context!=null) {
                    if (response.isSuccessful && uid == response.body()?.uid) {
                        Picasso.get().load(response.body()!!.image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .placeholder(R.drawable.defaultuser).into(binding!!.profileImage)
                    }
                }
            }
            override fun onFailure(call: Call<ModelUser>, t: Throwable) {
                Toast.makeText(requireContext(),"some error occurs",Toast.LENGTH_LONG).show()
            }
        })


        //using volley
        /*
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://hk123234345.000webhostapp.com/ProjectTest/useridview.php",
            com.android.volley.Response.Listener { response ->
                if (response.trim().equals("0")) {
                    Toast.makeText(requireContext(), "Fail to get", Toast.LENGTH_LONG).show()
                } else {
                    val jsonObject = JSONObject(response)
                    if (context!=null) {
                            Picasso.get().load(jsonObject.getString("image").toString())
                                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                                .placeholder(R.drawable.defaultuser).into(binding!!.profileImage)
                    }
                }
            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["id"] = id.toString()
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
        queue.add(stringRequest)
         */
        
        //transition animation of setting text
        binding!!.settingsButton.setOnClickListener  {
            val i = Intent(requireActivity(),SettingsActivity::class.java)
            startActivity(i)

        }

        //shared preference logout
        binding!!.logout.setOnClickListener {

            val edit1: SharedPreferences.Editor = sharedPreferences.edit()
            edit1.putBoolean("User_Session",false)
            edit1.apply()
            val i = Intent(requireContext(),LoginActivity::class.java)
            startActivity(i)

        }

        //set name to text
        binding!!.profileName.text = sharedPreferences.getString("name","")
        binding!!.profileEmail.text = sharedPreferences.getString("email","")

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}