package com.example.rickandmorty.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.model.CharactersModel
import com.example.rickandmorty.ui.detail.DetailActivity

class CharactersAdapter(private var data: List<CharactersModel>, private var context: Context) : RecyclerView.Adapter<CharactersAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val firstName: TextView = itemView.findViewById(R.id.firstName)
        val gender: TextView = itemView.findViewById(R.id.gender)
        val image: ImageView = itemView.findViewById(R.id.imageView)
        val id: TextView = itemView.findViewById(R.id.rickAndMortyId)
        val job: TextView = itemView.findViewById(R.id.job)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.firstName.text = item.name
        holder.gender.text = item.gender
        holder.id.text = "Id: ${item.id}"
        holder.job.text = item.species
        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("Id", item.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun filterData(filterList: List<CharactersModel>) {
        data = filterList
        notifyDataSetChanged()
    }

}