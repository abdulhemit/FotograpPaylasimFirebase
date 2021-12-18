package com.okuuyghur.fotografpaylasmafirebase

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.okuuyghur.fotografpaylasmafirebase.databinding.ActivityFotografPaylasmaBinding
import java.util.*
import kotlin.collections.HashMap

class FotografPaylasmaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFotografPaylasmaBinding
    var secilenGorsel : Uri? = null
    var secilenBitmap : Bitmap? = null
    private lateinit var storage : FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var dataBase : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotografPaylasmaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        dataBase = FirebaseFirestore.getInstance()
    }

    fun gorselYap(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else {
            val GaleriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(GaleriIntent,2)
        }
    }
    fun Paylas(view:View){

        // universal id olusturmak
        val uuid = UUID.randomUUID()
        val gorselIsmi = "${uuid}.jpg"

        // gorseli storage kayt etmek
        val reference = storage.reference
        val gorselReference = reference.child("images").child(gorselIsmi)
        if (secilenGorsel != null){
            gorselReference.putFile(secilenGorsel!!).addOnSuccessListener {
                //  gorselin url ni almak
                val yuklenengorselreference = reference.child("images").child(gorselIsmi)
                yuklenengorselreference.downloadUrl.addOnSuccessListener{
                    val downloadUrl = it
                    val guncelKullanici = auth.currentUser!!.email.toString()
                    val kullaniciYorumlari = binding.yorumId.text.toString()
                    val tarih = Timestamp.now()

                    // veri tabani islemleri
                    val userHashmap = HashMap<String,Any>()
                    userHashmap.put("gorselUrl",downloadUrl)
                    userHashmap.put("guncelkullaniciEmail",guncelKullanici)
                    userHashmap.put("kullaniciYorumalari",kullaniciYorumlari)
                    userHashmap.put("tarih",tarih)

                    dataBase.collection("Post").add(userHashmap).addOnCompleteListener { taskId ->
                        if (taskId.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener{
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()

                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED  ){
                val GaleriIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(GaleriIntent,2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            secilenGorsel = data.data
            if (secilenGorsel != null){

                if (Build.VERSION.SDK_INT >= 28){
                    val kaynak = ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap = ImageDecoder.decodeBitmap(kaynak)
                    binding.imageView.setImageBitmap(secilenBitmap)
                }else{
                    secilenBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    binding.imageView.setImageBitmap(secilenBitmap)


                }
            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}