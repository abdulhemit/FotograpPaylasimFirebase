package com.okuuyghur.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.okuuyghur.fotografpaylasmafirebase.databinding.ActivityHaberlerBinding

class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityHaberlerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaberlerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuinflater = MenuInflater(this)
        menuinflater.inflate(R.menu.secenekler_menusu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cikis_id){
            auth.signOut()
            val intent = Intent(this,KullaniciActivity::class.java)
            startActivity(intent)
            finish()
        }else if (item.itemId == R.id.fotograf_paylas_id){
            // fotograf activitye gidis
            val intent = Intent(this,FotografPaylasmaActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}