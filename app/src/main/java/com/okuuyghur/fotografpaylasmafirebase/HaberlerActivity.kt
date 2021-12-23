package com.okuuyghur.fotografpaylasmafirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.okuuyghur.fotografpaylasmafirebase.databinding.ActivityHaberlerBinding

class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private lateinit var binding : ActivityHaberlerBinding
    private lateinit var recyclerAdapte: Haber_RecyclerAdapte
    val PostListesi = ArrayList<PostModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHaberlerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        database = FirebaseFirestore.getInstance()
        verileriCekmek()
        val ln = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = ln
        recyclerAdapte = Haber_RecyclerAdapte(PostListesi)
        binding.recyclerView.adapter = recyclerAdapte

    }

    fun verileriCekmek(){
        database.collection("users").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

            if (error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{

                if ( value != null){
                    if (!value.isEmpty){
                        val documents = value.documents


                        PostListesi.clear()
                        for (document in documents ){
                            val Email = document.get("guncelkullaniciEmail") as String
                            val yorum = document.get("kullaniciYorumalari") as String
                            val gorsel = document.get("gorselUrl") as String

                            val Postlar = PostModel(Email,yorum,gorsel)
                            PostListesi.add(Postlar)

                        }
                        recyclerAdapte.notifyDataSetChanged()
                    }
                }

            }

        }

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