package com.okuuyghur.fotografpaylasmafirebase

import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.okuuyghur.fotografpaylasmafirebase.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class Haber_RecyclerAdapte (val PostListesi : ArrayList<PostModel>):RecyclerView.Adapter<Haber_RecyclerAdapte.HaberHolder>() {
    inner class HaberHolder (val row_bindig : RecyclerRowBinding): RecyclerView.ViewHolder(row_bindig.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HaberHolder {
        val view = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context))
        return HaberHolder(view)
    }

    override fun onBindViewHolder(holder: HaberHolder, position: Int) {
        holder.row_bindig.EmailText.setText(PostListesi.get(position).Email)
        holder.row_bindig.yorumText.setText(PostListesi.get(position).yorum)
        Picasso.get().load(PostListesi.get(position).gorsel).into(holder.row_bindig.imageviewRecyclerview)
    }

    override fun getItemCount(): Int {
       return PostListesi.size
    }
}