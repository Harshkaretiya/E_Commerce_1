package com.example.e_commerce1

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce1.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val s = MainActivity()
        s.setStatusBarColor(this,Color.parseColor("#F5F5F5"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        binding.registerButton.setOnClickListener {
            val name = binding.registerName.text.toString()
            val email = binding.registerEmail.text.toString()
            val password = binding.registerPassword.text.toString()

            if (password != binding.registerConfirmPassword.text.toString())
            {
                binding.registerConfirmPassword.error = "Password doesn't match"
            }
            else
            {

                apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)

                val call: Call<Void> = apiInterface.insertuser(name,email,password,"","")
                call.enqueue(object : Callback<Void>{
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {

                            Toast.makeText(applicationContext, "registered", Toast.LENGTH_LONG)
                                .show()
//                            startActivity(Intent(applicationContext, LoginActivity::class.java))

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(applicationContext,"Fail",Toast.LENGTH_LONG).show()
                    }

                })

            }
        }

        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }


    }


}