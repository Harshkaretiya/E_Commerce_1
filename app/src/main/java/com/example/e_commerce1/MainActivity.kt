package com.example.e_commerce1

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.e_commerce1.databinding.ActivityMainBinding

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//
//        replacefragment(HomeFragment(),"home")
//
//
//        setStatusBarColor(Color.parseColor("#F5F5F5"))
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//        changeicon(icon1 = R.drawable.homeselected)
//
//        binding.home.setOnClickListener {
//            if (checkfragment("home"))
//            {
//            }
//            else {
//                replacefragment(HomeFragment(), "home")
//                changeicon(icon1 = R.drawable.homeselected)
//            }
//        }
//        binding.favourite.setOnClickListener {
//            if (checkfragment("favourite"))
//            {
//            }
//            else {
//                replacefragment(FavouriteFragment(), "favourite")
//                changeicon(icon2 = R.drawable.heartselected)
//            }
//        }
//        binding.cart.setOnClickListener {
//            if (checkfragment("search"))
//            {
//            }
//            else {
//                replacefragment(CartFragment(), "search")
//                changeicon(icon3 = R.drawable.shoppingcartgreen)
//            }
//        }
//        binding.profile.setOnClickListener {
//            if (checkfragment("profile"))
//            {
//            }
//            else {
//                replacefragment(ProfileFragment(), "profile")
//                changeicon(icon4 = R.drawable.userselected)
//            }
//        }
//
//    }
//    fun replacefragment(fragment: Fragment,tag : String) {
//
//        var fragmentManager = supportFragmentManager
//        var fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.fragment,fragment,"$tag")
//        fragmentTransaction.commit()
//    }
//    fun checkfragment(tag: String): Boolean {
//        val myFragment=  supportFragmentManager.findFragmentByTag("$tag")
//        return (myFragment != null && myFragment.isVisible())
//    }
//    fun setStatusBarColor(color: Int) {
//        var window = this.window
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = color
//        }
//    }
//    fun changeicon(icon1: Int = R.drawable.home,icon2 : Int = R.drawable.heart,icon3 : Int = R.drawable.shoppingcart,icon4 : Int = R.drawable.user)
//    {
//        binding.homeimage.setImageResource(icon1)
//        binding.favouriteimage.setImageResource(icon2)
//        binding.cartimage.setImageResource(icon3)
//        binding.userimage.setImageResource(icon4)
//    }
//}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(this,Color.parseColor("#F5F5F5"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        var i = intent

        if (i.getStringExtra("page")==null) {
            replaceFragment(HomeFragment(), R.drawable.homeselected, "home")
        }
        else if (i.getStringExtra("page")=="profile")
        {
            replaceFragment(ProfileFragment(),R.drawable.userselected,"profile")
        }
        else if (i.getStringExtra("page")=="favourite")
        {
            replaceFragment(FavouriteFragment(),R.drawable.heartselected,"favourite")
        }
        else if (i.getStringExtra("page")=="setting")
        {
            replaceFragment(ProfileFragment(),R.drawable.userselected,"setting")
        }
        else if (i.getStringExtra("page")=="home")
        {
            replaceFragment(HomeFragment(),R.drawable.homeselected,"home")
        }



        binding.home.setOnClickListener {
            replaceFragment(
                HomeFragment(),
                R.drawable.homeselected,
                "home"
            )
        }
        binding.favourite.setOnClickListener {
            replaceFragment(
                FavouriteFragment(),
                R.drawable.heartselected,
                "favourite"
            )
        }
        binding.cart.setOnClickListener {
            replaceFragment(
                CartFragment(),
                R.drawable.shoppingcartgreen,
                "search"
            )
        }
        binding.profile.setOnClickListener {
            replaceFragment(
                ProfileFragment(),
                R.drawable.userselected,
                "profile"
            )
        }
    }

        fun replaceFragment(fragment: Fragment, icon: Int, tag: String) {
            if (checkFragment(tag)) return
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, tag)
                .commit()
            changeIcon(icon)
        }

        fun checkFragment(tag: String) = supportFragmentManager.findFragmentByTag(tag)?.isVisible ?: false

        fun setStatusBarColor(activity: Activity, color: Int) {
            val window = activity.window
            window.statusBarColor = color
        }

        fun changeIcon(icon: Int) {
            binding.homeimage.setImageResource(if (icon == R.drawable.homeselected) icon else R.drawable.home)
            binding.favouriteimage.setImageResource(if (icon == R.drawable.heartselected) icon else R.drawable.heart)
            binding.cartimage.setImageResource(if (icon == R.drawable.shoppingcartgreen) icon else R.drawable.shoppingcart)
            binding.userimage.setImageResource(if (icon == R.drawable.userselected) icon else R.drawable.user)
        }
        fun checkfragment(tag: String): Boolean {
            val myFragment=  supportFragmentManager.findFragmentByTag("$tag")
            return (myFragment != null && myFragment.isVisible())
        }

    override fun onBackPressed() {
            if (checkFragment("profile") || checkFragment("search") || checkFragment("favourite"))
            {
                replaceFragment(HomeFragment(),R.drawable.homeselected,"home")
            }
            else
            {
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
    }