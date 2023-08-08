package com.example.rickandmorty.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.model.CharactersModel
import com.example.rickandmorty.ui.adapter.CharactersAdapter
import com.example.rickandmorty.utils.showLoadingDialog
import com.example.rickandmorty.R
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private var loadingDialog: Dialog? = null
    private val viewModel: MainViewModel by inject()
    private var listRickyAndMorty: ArrayList<CharactersModel> = arrayListOf()
    private var listRickyAndMortyFiltered: ArrayList<CharactersModel> = arrayListOf()
    private var speciesList: ArrayList<String> = arrayListOf()
    private var genderList: ArrayList<String> = arrayListOf()
    private lateinit var recyclerView: RecyclerView
    private val adapter = CharactersAdapter(listRickyAndMorty,this)
    private lateinit var textInputLayoutGender: TextInputLayout
    private lateinit var textInputLayoutSpecies: TextInputLayout
    private lateinit var resetButton: Button
    private lateinit var autoCompleteGender: AutoCompleteTextView
    private lateinit var autoCompleteSpecies: AutoCompleteTextView
    private lateinit var errorImageView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDialog()
        relateViewWithVariables()
        setView()
        setObservers()
        setData()
    }

    private fun relateViewWithVariables() {
        recyclerView = findViewById(R.id.recyclerView)
        autoCompleteGender = findViewById(R.id.auto_complete_gender)
        autoCompleteSpecies = findViewById(R.id.auto_complete_species)
        textInputLayoutGender = findViewById(R.id.dropDownGender)
        textInputLayoutSpecies = findViewById(R.id.dropDownSpecies)
        resetButton = findViewById(R.id.resetFilter)
        errorImageView = findViewById(R.id.errorImage)
    }

    private fun setData() {
        if (viewModel.resultDao.value.isNotEmpty()) {
            setRecyclerView()
            listRickyAndMorty.clear()
            listRickyAndMorty.addAll(viewModel.resultDao.value)
            addItemsSpeciesAndGenderList()
            adapter.notifyDataSetChanged()
            Handler(Looper.getMainLooper()).postDelayed({
                hideLoading()
            }, 1000)
        } else {
            viewModel.getCharacters()
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.result.collect {
                viewModel.getCharacterDao()
            }
        }
        lifecycleScope.launch {
            viewModel.resultError.collect {
                setErrorView()
                setData()
            }
        }
        lifecycleScope.launch {
            viewModel.resultDao.collect {
                setData()
            }
        }
    }

    private fun addItemsSpeciesAndGenderList() {
        listRickyAndMorty.map {
            if (!speciesList.contains(it.species)) {
                it.species?.let { it1 -> speciesList.add(it1) }
            }
            if (!genderList.contains(it.gender)) {
                it.gender?.let { it1 -> genderList.add(it1) }
            }
        }
        configDorpDownsMenu()
    }

    private fun configDorpDownsMenu() {
        val adapterGender = ArrayAdapter(this, R.layout.list_items_dorpdownmenu,genderList)
        val adapterJob = ArrayAdapter(this,R.layout.list_items_dorpdownmenu,speciesList)

        autoCompleteGender.setAdapter(adapterGender)
        autoCompleteSpecies.setAdapter(adapterJob)

        autoCompleteGender.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position).toString()
            listRickyAndMortyFiltered = getFilteredList(itemSelected, true)
            adapter.filterData(listRickyAndMortyFiltered)
        }

        autoCompleteSpecies.onItemClickListener = AdapterView.OnItemClickListener {
                parent, view, position, id ->
            val itemSelected = parent.getItemAtPosition(position).toString()
            listRickyAndMortyFiltered = getFilteredList(itemSelected, false)
            adapter.filterData(listRickyAndMortyFiltered)
        }

        resetButton.setOnClickListener {
            resetFilter()
        }
    }

    private fun getFilteredList(itemSelected: String, isByGender: Boolean): ArrayList<CharactersModel> {
        val list = if (isByGender) {
            if (listRickyAndMortyFiltered.isNotEmpty()) {
                listRickyAndMortyFiltered.filter {
                    it.gender?.lowercase()!! == itemSelected.lowercase()
                }
            } else {
                listRickyAndMorty.filter {
                    it.gender?.lowercase()!! == itemSelected.lowercase()
                }
            }
        } else {
             if (listRickyAndMortyFiltered.isNotEmpty()) {
                listRickyAndMortyFiltered.filter {
                    it.species?.lowercase()!! == itemSelected.lowercase()
                }
            } else {
                listRickyAndMorty.filter {
                    it.species?.lowercase()!! == itemSelected.lowercase()
                }
            }
        }
        return list as ArrayList<CharactersModel>
    }

    private fun resetFilter() {
        adapter.filterData(listRickyAndMorty)
        textInputLayoutGender.clearFocus()
        textInputLayoutSpecies.clearFocus()
        autoCompleteSpecies.setText("")
        autoCompleteGender.setText("")
        listRickyAndMortyFiltered.clear()
    }

    private fun setRecyclerView() {
        recyclerView.visibility = View.VISIBLE
        errorImageView.visibility = View.GONE
    }

    private fun setErrorView() {
        recyclerView.visibility = View.GONE
        errorImageView.visibility = View.VISIBLE
    }

    private fun setView() {
        recyclerView.visibility = View.GONE
        errorImageView.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.cancel() }
    }

    private fun showDialog() {
        hideLoading()
        loadingDialog = this.showLoadingDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItemSearch = menu!!.findItem(R.id.action_search)
        val searchView = menuItemSearch.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val newList = listRickyAndMorty.filter {
                    it.name?.lowercase()!!.contains(query!!.lowercase())
                }
                adapter.filterData(newList)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val newList = listRickyAndMorty.filter {
                    it.name?.lowercase()!!.contains(newText!!.lowercase())
                }
                adapter.filterData(newList)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filters -> {
                if (textInputLayoutGender.visibility == View.GONE) {
                    textInputLayoutGender.visibility = View.VISIBLE
                    textInputLayoutSpecies.visibility = View.VISIBLE
                    resetButton.visibility = View.VISIBLE
                } else {
                    textInputLayoutGender.visibility = View.GONE
                    textInputLayoutSpecies.visibility = View.GONE
                    resetButton.visibility = View.GONE
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}