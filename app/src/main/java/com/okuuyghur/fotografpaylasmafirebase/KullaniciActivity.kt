package com.okuuyghur.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.okuuyghur.fotografpaylasmafirebase.databinding.ActivityMainBinding

class KullaniciActivity : AppCompatActivity() {
  private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null){
            val intent = Intent(this,HaberlerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun girisYap(view: View){
        auth.signInWithEmailAndPassword(binding.editEmail.text.toString(),binding.editPaword.text.toString()).addOnCompleteListener { taskId ->
            if (taskId.isSuccessful){
                val guncelkullanici = auth.currentUser?.email.toString()

                Toast.makeText(this,"hos geldin ${guncelkullanici}",Toast.LENGTH_LONG).show()
                val intent = Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener(this){
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }

    }
    fun kaytOl(view:View){

        val email = binding.editEmail.text.toString()
        val sifre = binding.editPaword.text.toString()
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener (this){ taskId ->
           // asenkron
            if (taskId.isSuccessful){

                val intent = Intent(this,HaberlerActivity::class.java)
                startActivity(intent)
                finish()

            }
        }.addOnFailureListener(this){
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()

        }

    }
}