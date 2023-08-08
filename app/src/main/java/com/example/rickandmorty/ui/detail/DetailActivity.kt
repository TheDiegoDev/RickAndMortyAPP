package com.example.rickandmorty.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.rickandmorty.R
import com.example.rickandmorty.model.CharactersModel
import kotlinx.coroutines.launch

class DetailActivity: AppCompatActivity() {

    private val viewModel by inject<DetailActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        val id = intent.getIntExtra("Id", 1)
        viewModel.getCharacterDataBase(id.toString())
        setObserver()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.result.collect {
                setView(it)
            }
        }
    }

    private fun setView(item: CharactersModel) {
        val name: TextView = findViewById(R.id.detailName)
        val image: ImageView = findViewById(R.id.detailImage)
        val status: TextView = findViewById(R.id.status)
        val species: TextView = findViewById(R.id.species)
        val created: TextView = findViewById(R.id.created)
        val gender: TextView = findViewById(R.id.detailGender)


        Glide.with(this)
            .load(item.image)
            .into(image)

        name.text = item.name
        status.text =  item.status
        species.text = "Species: ${item.species}"
        created.text = "Created: ${item.created}"
        gender.text = "Gender: ${item.gender}"
    }
}