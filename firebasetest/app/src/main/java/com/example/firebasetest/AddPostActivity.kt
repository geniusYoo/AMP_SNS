package com.example.firebasetest

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddPostActivity : AppCompatActivity() {
    private lateinit var launcher : ActivityResultLauncher<Intent>
    var storage : FirebaseStorage? = null
    var photoUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)

        storage = FirebaseStorage.getInstance()
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        var result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == RESULT_OK) {
                photoUri = photoPickerIntent?.data
                findViewById<ImageView>(R.id.addphoto_image).setImageURI(photoUri)
            }
            else {
                finish()
            }
        }
        fun contentUpload() {
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            var imageFileName = "IMAGE_" + timestamp + "_.png"

            var storageRef = storage?.reference?.child("images")?.child(imageFileName)
            storageRef?.putFile(photoUri!!)?.addOnSuccessListener {
                Toast.makeText(this, "upload Success", Toast.LENGTH_LONG).show()
            }
        }

        findViewById<Button>(R.id.addphoto_btn_upload).setOnClickListener {
            contentUpload()
        }

    }

}