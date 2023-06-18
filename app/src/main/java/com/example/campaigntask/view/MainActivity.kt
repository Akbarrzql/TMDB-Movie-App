package com.example.campaigntask.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campaigntask.R
import com.example.campaigntask.adapter.CategoryItemAdapter
import com.example.campaigntask.adapter.MainRecyclerViewAdapter
import com.example.campaigntask.model.AllCategory
import com.example.campaigntask.model.CategoryItem
import com.example.campaigntask.repository.Repository
import com.example.campaigntask.utils.Contans.Companion.apiKey
import com.example.campaigntask.utils.Contans.Companion.language
import com.example.campaigntask.utils.Contans.Companion.page
import com.example.campaigntask.viewmodel.MainViewModel
import com.example.campaigntask.viewmodel.MainViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CategoryItemAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private var MainCategoryRecyleView: RecyclerView? = null
    private var mainRecyclerViewAdapter: MainRecyclerViewAdapter? = null
    private var allCategory: ArrayList<AllCategory> = ArrayList()
    private lateinit var repository: Repository
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        lifecycleScope.launch {
            getNowPlaying()
            getUpComing()
            getPopuler()
            getTopRated()
        }
    }

    private suspend fun getNowPlaying() {
        repository = Repository()
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getNowPlaying(apiKey, language, page)
        viewModel.reponseNowPlaying.observe(this) { responseNowPlaying ->
            if (responseNowPlaying != null) {
                Log.d("Main", responseNowPlaying.toString())
                val categoryItem1: ArrayList<CategoryItem> = ArrayList()
                responseNowPlaying.results?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.originalTitle ?: "Tidak ada judul", "https://image.tmdb.org/t/p/original/${result?.posterPath}")
                    categoryItem1.add(categoryItem)
                }
                allCategory.clear()
                allCategory.add(AllCategory("Sedang Tayang", categoryItem1))
                setupMainCategoryRecyleView(allCategory)
            }
        }
    }

    private suspend fun getUpComing() {
        repository = Repository()
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getUpcoming(apiKey, language, page)
        viewModel.reponseUpcoming.observe(this) { responseUpcoming ->
            if (responseUpcoming != null) {
                Log.d("Main", responseUpcoming.toString())
                val categoryItem2: ArrayList<CategoryItem> = ArrayList()
                responseUpcoming.results?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.originalTitle ?: "Tidak ada judul", "https://image.tmdb.org/t/p/original/${result?.posterPath}")
                    categoryItem2.add(categoryItem)
                }
                allCategory.add(AllCategory("Akan Tayang", categoryItem2))
                setupMainCategoryRecyleView(allCategory)
            }
        }
    }

    private suspend fun getPopuler() {
        repository = Repository()
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getPopuler(apiKey, language, page)
        viewModel.reponsePopuler.observe(this) { responsePopuler ->
            if (responsePopuler != null) {
                Log.d("Main", responsePopuler.toString())
                val categoryItem3: ArrayList<CategoryItem> = ArrayList()
                responsePopuler.results?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.originalTitle ?: "Tidak ada judul", "https://image.tmdb.org/t/p/original/${result?.posterPath}")
                    categoryItem3.add(categoryItem)
                }
                allCategory.add(AllCategory("Populer Saat Ini", categoryItem3))
                setupMainCategoryRecyleView(allCategory)
            }
        }
    }

    private suspend fun getTopRated() {
        repository = Repository()
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getTopRated(apiKey, language, page)
        viewModel.reponseTopRated.observe(this) { responseTopRated ->
            if (responseTopRated != null) {
                Log.d("Main", responseTopRated.toString())
                val categoryItem4: ArrayList<CategoryItem> = ArrayList()
                responseTopRated.results?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.originalTitle ?: "Tidak ada judul", "https://image.tmdb.org/t/p/original/${result?.posterPath}")
                    categoryItem4.add(categoryItem)
                }
                allCategory.add(AllCategory("Top Rated", categoryItem4))
                setupMainCategoryRecyleView(allCategory)
            }
        }
    }

    private fun setupMainCategoryRecyleView(allCategory: List<AllCategory>) {
        MainCategoryRecyleView = findViewById(R.id.main_rv)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        MainCategoryRecyleView!!.layoutManager = layoutManager
        mainRecyclerViewAdapter = MainRecyclerViewAdapter(this, allCategory, this)
        MainCategoryRecyleView!!.adapter = mainRecyclerViewAdapter
    }

    override fun onItemClick(categoryItem: CategoryItem) {
        val intent = Intent(this, DetailMovieActivity::class.java)
        intent.putExtra("movie_id", categoryItem.itemId)
        startActivity(intent)
    }
}
