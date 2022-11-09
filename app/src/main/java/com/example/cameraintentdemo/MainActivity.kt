package com.example.cameraintentdemo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cameraintentdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    // create companion object to hold camera permission code and camera request code constant variable
    companion object {
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.cameraBtn?.setOnClickListener {
            //Check the the permission is granted or not
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                // prepare a intent to start the camera
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //Start Activity for Result with the intent and Request Code
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else { //Else if permission is not granted
            //ActivityCompat requestPermission with activity, array of permission name and camera permission code
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            }
        }
    }

    //Create a override fun onRequestPermissionResult to show the result when user grant or denied the permission
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //if the request code is same as camera intent request code
        if (requestCode == CAMERA_PERMISSION_CODE) {
            //then if grantResult is not empty and grantResult[0] is prmission granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                //Start Activity for Result with the intent and Request Code
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                Toast.makeText(this, "You just denied the permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST_CODE){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                binding?.imageIV?.setImageBitmap(thumbnail)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}