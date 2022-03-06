package com.example.profileimage

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var storage: StorageReference
    private lateinit var ImageView: ImageView
    private lateinit var SelectImageBtn:Button
    private  lateinit var UploadImageBtn:Button
    private lateinit var imageUri : Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creating an instance of firebase storage
       var storage = Firebase.storage

        ImageView = findViewById(R.id.firebaseImage)
        SelectImageBtn = findViewById(R.id.select_image_btn)
        UploadImageBtn = findViewById(R.id.upload_image_btn)

        SelectImageBtn.setOnClickListener {
            selectImage()


        }

        UploadImageBtn.setOnClickListener {
            uploadImageToFirebase()

        }






        }

    private fun uploadImageToFirebase() {
        if (imageUri != null){
            var progress = ProgressDialog(this)
            progress.setTitle("Uploading,please wait")
            progress.show()

            var imageRef = FirebaseStorage.getInstance().reference.child("images/pic.jpg")
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    progress.dismiss()
                    Toast.makeText(this,"Uploaded successful",Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener{
                    progress.dismiss()
                    Toast.makeText(this,"Failed to upload",Toast.LENGTH_SHORT).show()

                }
                .addOnProgressListener {p0->
                    var rate :Double = (100.0 * p0.bytesTransferred)/p0.totalByteCount
                    progress.setMessage("Uploaded ${rate.toInt()}")

                }
        }
    }

    private fun selectImage() {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent,9)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 9 && resultCode == RESULT_OK && data != null){
            imageUri = data.data!!
            var bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
            ImageView.setImageBitmap(bitmap)

        }
    }


}





