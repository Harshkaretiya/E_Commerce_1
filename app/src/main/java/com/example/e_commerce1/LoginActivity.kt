package com.example.e_commerce1

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce1.databinding.ActivityLoginBinding
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val s = MainActivity()
        s.setStatusBarColor(this,Color.parseColor("#F5F5F5"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        sharedPreferences = getSharedPreferences("User_Session", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("User_Session", false) )
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {

            var email = binding.loginEmail.text.toString()
            var password = binding.loginPassword.text.toString()

            apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

            var call: Call<ModelUser> = apiInterface.getuser(email,password)

            call.enqueue(object : Callback<ModelUser>{
                override fun onResponse(call: Call<ModelUser>, response: Response<ModelUser>) {

                    if (response.isSuccessful && email == response.body()?.email && password == response.body()?.password)
                    {
//                        Toast.makeText(applicationContext,"done",Toast.LENGTH_LONG).show()

                        var edit1: SharedPreferences.Editor = sharedPreferences.edit()

                        edit1.putBoolean("User_Session", true)

                        edit1.putInt("uid", response.body()!!.uid)
                        edit1.putString("name", response.body()!!.name)
                        edit1.putString("email", response.body()!!.email)
                        edit1.putString("image", response.body()!!.image)
                        edit1.putString("number", response.body()!!.number)
                        edit1.putString("password", response.body()!!.password)
                        edit1.apply()

                        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    }
                }

                override fun onFailure(call: Call<ModelUser>, t: Throwable) {
                    Toast.makeText(applicationContext,"Incorrect email or password",Toast.LENGTH_LONG).show()
                }

            })
        }

        binding.dontHaveAccount.setOnClickListener {
            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Are you sure you want to exit?")

        builder.setPositiveButton("Yes"){ _, _ ->
            finishAffinity()
        }

        builder.setNegativeButton("No"){ dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }
}
