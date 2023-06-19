package com.example.campaigntask.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.campaigntask.R
import com.example.campaigntask.adapter.CategoryItemAdapter
import com.example.campaigntask.adapter.MainRecyclerViewAdapter
import com.example.campaigntask.databinding.ActivityDetailMovieBinding
import com.example.campaigntask.model.AllCategory
import com.example.campaigntask.model.CategoryItem
import com.example.campaigntask.repository.Repository
import com.example.campaigntask.utils.Contans.Companion.apiKey
import com.example.campaigntask.utils.Contans.Companion.language
import com.example.campaigntask.viewmodel.MainViewModel
import com.example.campaigntask.viewmodel.MainViewModelFactory
import com.google.android.material.chip.Chip
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class DetailMovieActivity : AppCompatActivity(), CategoryItemAdapter.OnItemClickListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityDetailMovieBinding
    private var allCategory: ArrayList<AllCategory> = ArrayList()
    private lateinit var repository: Repository
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        supportActionBar?.hide()

        val movieId = intent.getIntExtra("movie_id", 0)
        repository = Repository()
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        lifecycleScope.launch {
            getDetail(movieId)
            getDetailMovieCredits(movieId)
            getRecommendations(movieId)
            getTrailerVideo(movieId)
        }

    }


    private suspend fun getDetail(movieId: Int) {
        viewModel.getDetailMovie(movieId, apiKey, language)
        viewModel.responseDetailMovie.observe(this) { responseDetailMovie ->
            if (responseDetailMovie != null) {
                binding = ActivityDetailMovieBinding.inflate(layoutInflater)
                setContentView(binding.root)
                val genres = responseDetailMovie.genres
                val chipGroup = binding.chipGroup
                genres?.forEach { genre ->
                    val chip = Chip(this).apply {
                        text = genre?.name
                        isClickable = false
                        isCheckable = false
                        chipCornerRadius = 50f
                        chipStrokeWidth = 0f
                        setChipBackgroundColorResource(R.color.chip_color)
                        setTextColor(ContextCompat.getColor(this@DetailMovieActivity, R.color.dark))
                    }

                    chipGroup.addView(chip as View)

                    binding.tvTittleDetail.text = responseDetailMovie.originalTitle
                    val date = responseDetailMovie.releaseDate
                    val year = date?.substring(0, 4)
                    val month = date?.substring(5, 7)
                    val day = date?.substring(8, 10)
                    binding.tvReleaseDateDetail.text = "$day-$month-$year"
                    if (responseDetailMovie.overview == "") {
                        binding.tvSinopsisDetail.text = "Belum tersedia sinopsis mengenai film ini di database TMDB"
                    } else {
                        binding.tvSinopsisDetail.text = responseDetailMovie.overview
                    }
                    if (responseDetailMovie.backdropPath == null) {
                        binding.image.isGone = true
                    } else {
                        Picasso.get().load("https://image.tmdb.org/t/p/original/${responseDetailMovie.backdropPath}").into(binding.image)
                        Picasso.get().setIndicatorsEnabled(false)
                    }
                }
            }
        }
    }



    private suspend fun getRecommendations(movieId: Int){
        viewModel.getRecommendations(movieId, apiKey, language)
        viewModel.responseRecommendations.observe(this) { responseRecommendations ->
            if (responseRecommendations != null) {
                Log.d("Main", responseRecommendations.toString())
                val categoryItem1: ArrayList<CategoryItem> = ArrayList()
                responseRecommendations.results?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.title ?: "Belum terdapat judul", "https://image.tmdb.org/t/p/original/${result?.posterPath}")
                    categoryItem1.add(categoryItem)
                }
                allCategory.clear()
                allCategory.add(AllCategory("", categoryItem1))
                val recyclerView = findViewById<RecyclerView>(R.id.rv_recommendation)
                setupRecyclerView(allCategory, recyclerView)
            }
        }
    }

    private suspend fun getDetailMovieCredits(movieId: Int){
        viewModel.getDetailMovieCredits(movieId, apiKey, language)
        viewModel.responseDetailMovieCredits.observe(this) { responseDetailMovieCredits ->
            if (responseDetailMovieCredits != null) {
                Log.d("Main", responseDetailMovieCredits.toString())
                val categoryItem1: ArrayList<CategoryItem> = ArrayList()
                responseDetailMovieCredits.cast?.forEach { result ->
                    val categoryItem = CategoryItem(result?.id ?: 0, result?.name ?: "Belum terdapat nama", "https://image.tmdb.org/t/p/original/${result?.profilePath}")
                    categoryItem1.add(categoryItem)
                }
                allCategory.clear()
                allCategory.add(AllCategory("", categoryItem1))
                val recyclerView = findViewById<RecyclerView>(R.id.rv_cast)
                setupRecyclerView(allCategory, recyclerView)
            }
        }
    }

    private suspend fun getTrailerVideo(movieId: Int){
        viewModel.getTrailerVideo(movieId, apiKey, "en-US")
        viewModel.responseTrailerVideo.observe(this) { responseTrailerVideo ->
            if (responseTrailerVideo != null) {
                val results = responseTrailerVideo.results
                if (results == null || results.isEmpty()) {
                    binding.btnUrl.isGone = true
                } else {
                    val result = results[0]?.key
                    binding.btnUrl.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$result"))
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(allCategory: List<AllCategory>, vararg recyclerViews: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        for (recyclerView in recyclerViews) {
            recyclerView.layoutManager = layoutManager
            val mainRecyclerViewAdapter = MainRecyclerViewAdapter(this, allCategory, this)
            recyclerView.adapter = mainRecyclerViewAdapter
        }
    }

    override fun onItemClick(categoryItem: CategoryItem) {
        val responseDetailMovie = viewModel.responseDetailMovieCredits.value
        if (responseDetailMovie != null) {
            val castNames = responseDetailMovie.cast?.map { it?.name }
            if (castNames?.contains(categoryItem.categoryTittle) == true) {
                Toast.makeText(this, categoryItem.categoryTittle, Toast.LENGTH_SHORT).show()
            } else if (categoryItem.itemId != 0) {
                val intent = Intent(this, DetailMovieActivity::class.java)
                intent.putExtra("movie_id", categoryItem.itemId)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Belum terdapat detail movie", Toast.LENGTH_SHORT).show()
            }
        }
    }

}