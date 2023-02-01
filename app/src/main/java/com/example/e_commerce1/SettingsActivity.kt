package com.example.e_commerce1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.e_commerce1.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.editProfile.setOnClickListener {
            startActivity(Intent(this,EditProfileActivity::class.java))
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

    }

    override fun onBackPressed() {
        var i = Intent(this,MainActivity::class.java)
        i.putExtra("page","setting")
        startActivity(i)
    }

}