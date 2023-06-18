package com.example.campaigntask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.campaigntask.model.*
import com.example.campaigntask.repository.Repository
import retrofit2.Response

class MainViewModel(private val repository: Repository) : ViewModel() {

    val reponseNowPlaying: MutableLiveData<NowPlaying> = MutableLiveData()
    val reponseUpcoming: MutableLiveData<NowPlaying> = MutableLiveData()
    val reponsePopuler: MutableLiveData<NowPlaying> = MutableLiveData()
    val reponseTopRated: MutableLiveData<NowPlaying> = MutableLiveData()
    private val _responseDetailMovie = MutableLiveData<DetailMovieModel>()
    val responseDetailMovie: LiveData<DetailMovieModel> = _responseDetailMovie
    val responseDetailMovieCredits: MutableLiveData<DetailCreditsMovie> = MutableLiveData()
    val responseRecommendations: MutableLiveData<NowPlaying> = MutableLiveData()
    val responseTrailerVideo: MutableLiveData<TrailerVideoResponse> = MutableLiveData()

    suspend fun getNowPlaying(apiKey: String, language: String, page: Int) {
        val response = repository.getNowPlaying(apiKey, language, page)
        reponseNowPlaying.value = response
    }

    suspend fun getUpcoming(apiKey: String, language: String, page: Int) {
        val response = repository.getUpcoming(apiKey, language, page)
        reponseUpcoming.value = response
    }

    suspend fun getPopuler(apiKey: String, language: String, page: Int){
        val response = repository.getPopuler(apiKey, language, page)
        reponsePopuler.value = response
    }

    suspend fun getTopRated(apiKey: String, language: String, page: Int){
        val response = repository.getTopRated(apiKey, language, page)
        reponseTopRated.value = response
    }

    suspend fun getDetailMovie(movieId: Int, apiKey: String, language: String) {
        val response = repository.getDetailMovie(movieId, apiKey, language)
        _responseDetailMovie.value = response
    }

    suspend fun getDetailMovieCredits(movieId: Int, apiKey: String, language: String){
        val response = repository.getDetailMovieCredits(movieId, apiKey, language)
        responseDetailMovieCredits.value = response
    }

    suspend fun getRecommendations(movieId: Int, apiKey: String, language: String){
        val response = repository.getRecommendations(movieId, apiKey, language)
        responseRecommendations.value = response
    }

    suspend fun getTrailerVideo(movieId: Int, apiKey: String, language: String){
        val response = repository.getTrailerVideo(movieId, apiKey, language)
        responseTrailerVideo.value = response
    }


}
