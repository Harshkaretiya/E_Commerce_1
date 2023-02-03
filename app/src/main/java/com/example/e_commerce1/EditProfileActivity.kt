package com.example.e_commerce1

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.e_commerce1.databinding.ActivityEditProfileBinding
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import net.gotev.uploadservice.MultipartUploadRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var sharedPreferences: SharedPreferences
    private var filepath: Uri? = null
    private lateinit var  bitmap: Bitmap

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change colour of input images
        binding.editNameImage.setColorFilter(Color.parseColor("#868889"))
        binding.editEmailImage.setColorFilter(Color.parseColor("#868889"))
        binding.editPhoneNumberImage.setColorFilter(Color.parseColor("#868889"))
        binding.profileImageEdit.setColorFilter(Color.parseColor("#FFFFFF"))

        //set status bar colour white
        val s = MainActivity()
        s.setStatusBarColor(this,Color.WHITE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        //using shared preference to get details
        sharedPreferences = getSharedPreferences("User_Session", Context.MODE_PRIVATE)

        var email = sharedPreferences.getString("email","")

        binding.editName.setText("${sharedPreferences.getString("name","")}")
        binding.editEmail.setText(email)
        binding.editPhoneNumber.setText("${sharedPreferences.getString("number","")}")

        val id = sharedPreferences.getInt("uid",101)

        //using apiInterference to get image using shared preference id and also using retro fit

        apiInterface = ApiClient.getapiclient().create(ApiInterface::class.java)
        val call: Call<ModelUser> = apiInterface.getiduser(id)
        call.enqueue(object : Callback<ModelUser> {
            override fun onResponse(call: Call<ModelUser>, response: Response<ModelUser>) {
                if (this!=null) {
                    if (response.isSuccessful && id == response.body()?.uid) {
                        Picasso.get().load(response.body()!!.image)
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .placeholder(R.drawable.defaultuser).into(binding.profileImage)
                    }
                }

            }
            override fun onFailure(call: Call<ModelUser>, t: Throwable) {
                Toast.makeText(applicationContext,"some error occurs",Toast.LENGTH_LONG).show()
            }
        })



        //using volley
        /*
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            "https://hk123234345.000webhostapp.com/ProjectTest/useridview.php",
            com.android.volley.Response.Listener { response ->
                if (response.trim().equals("0")) {
                    Toast.makeText(this, "Fail to get", Toast.LENGTH_LONG).show()
                } else {
                    val jsonObject = JSONObject(response)
                    if (this!=null) {
                        Picasso.get().load(jsonObject.getString("image").toString())
                            .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                            .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                            .placeholder(R.drawable.defaultuser).into(binding!!.profileImage)
                    }
                }
            },
            com.android.volley.Response.ErrorListener {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val map = HashMap<String, String>()
                map["id"] = id.toString()
                return map
            }
        }
        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)

         */

        //getting image from local storage or phone storage
        binding.profileImageCardview.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1) //calling onActivityResult function
        }

        //requesting permission of read storage
        requestpermission()

        //main code of saving/updating the data
        binding.saveButton.setOnClickListener {
            //getting the text from edittext
            val name = binding.editName.text.toString()
            val email = binding.editEmail.text.toString()
            val number = binding.editPhoneNumber.text.toString()

            //check if the image want to be updated or not
                //if yes the this code will run
            if(filepath !=null){
                val path = getPath(filepath)
                val multipartUploadRequest = MultipartUploadRequest(this, "https://hk123234345.000webhostapp.com/ProjectTest/upload.php")
                    .addParameter("uid",id.toString())
                    .addParameter("name", name)
                    .addParameter("email",email)
                    .addParameter("number",number)
                    .setMaxRetries(2)

                multipartUploadRequest.addFileToUpload(path, "img")
                multipartUploadRequest.startUpload()
            }
                //if no the this code will be run
            else{
                val multipartUploadRequest = MultipartUploadRequest(this, "https://hk123234345.000webhostapp.com/ProjectTest/upload.php")
                    .addParameter("uid",id.toString())
                    .addParameter("name", name)
                    .addParameter("email",email)
                    .addParameter("number",number)
                    .setMaxRetries(2)
                multipartUploadRequest.startUpload()
            }

            //updating data of sharedpreference as data is also updated to online database
            val edit1: SharedPreferences.Editor = sharedPreferences.edit()
            edit1.putString("name", name)
            edit1.putString("email", email)
            edit1.putString("number", number)
            edit1.apply()
            Toast.makeText(this@EditProfileActivity, "success", Toast.LENGTH_SHORT).show()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
    //read premission code
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestpermission()
    {
        if(checkSelfPermission(READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE),100)
        }
    }
    //get path of image from local storage function
    @SuppressLint("Range")
    fun getPath(uri: Uri?): String
    {
        var cursor = contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        var document_id = cursor.getString(0)
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1)
        cursor.close()
        cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, MediaStore.Images.Media._ID + " = ? ", arrayOf(document_id), null)
        cursor!!.moveToFirst()
        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        cursor.close()
        return path
    }

    //when image is selected from getting image code the image will be set to image view
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(requestCode==1 && resultCode == RESULT_OK && data != null) {
            filepath = data.data!!
            bitmap= MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            binding.profileImage.setImageBitmap(bitmap)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}