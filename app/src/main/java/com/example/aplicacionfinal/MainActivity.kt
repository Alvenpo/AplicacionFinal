package com.example.aplicacionfinal

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplicacionfinal.databinding.ActivityMainBinding
import com.example.aplicacionfinal.ui.theme.AplicacionFinalTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: SuperHeroAdapter
    private val heroesImage = mutableListOf<SuperHeroItemResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        //screenSplash.setKeepOnScreenCondition{false}
        setTheme(R.style.Theme_AplicacionFinal)
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //initUI()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = SuperHeroAdapter(heroesImage)
        binding.rvHero.layoutManager = LinearLayoutManager(this)
        binding.rvHero.adapter = adapter
    }

    /*private fun initUI(){
        binding.svHero.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean{
                searchByName(query.orEmpty())
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })
        adapter = SuperHeroAdapter()
        binding.rvHero.setHasFixedSize(true)
        binding.rvHero.layoutManager = LinearLayoutManager(this)
        binding.rvHero.adapter = adapter
    }*/

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        binding.pgBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getSuperheroes(query)
            val response = call.body()
            runOnUiThread {
                if (call.isSuccessful) {
                    binding.pgBar.isVisible = false
                    //raza = query
                    val images = response?.superheroes ?: emptyList()
                    heroesImage.clear()
                    heroesImage.addAll(images)
                    adapter.notifyDataSetChanged()
                } else {
                    showError()
                }
                hideKeyBoard()
            }
        }
    }

    /*private fun searchByName(query:String){
        binding.pgBar.isVisible = true
        CoroutineScope(Dispatchers.IO).launch {
            val call: Response<SuperHeroDataResponse> = getRetrofit().create(APIService::class.java).getSuperheroes(query)
            if (call.isSuccessful){
                val heroes: SuperHeroDataResponse? = call.body()
                if (heroes != null){
                    runOnUiThread{
                        adapter.updateList(heroes.superheroes)
                        binding.pgBar.isVisible = false
                    }
                }
            } else{
                //...
            }
        }
    }*/

    private fun hideKeyBoard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken, 0)
    }

    private fun showError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    /*private fun navigateToDetail(id:String){
        val intent = Intent(this, DetailDogActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        intent.putExtra(EXTRA_RAZA, raza)
        startActivityForResult(intent, 1)
    }*/
}