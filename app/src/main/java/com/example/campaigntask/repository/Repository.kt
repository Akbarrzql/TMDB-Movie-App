package com.example.campaigntask.repository

import com.example.campaigntask.api.RetrofitInstance
import com.example.campaigntask.model.*

class Repository {

    suspend fun getNowPlaying(apiKey: String, language: String, page: Int): NowPlaying {
        return RetrofitInstance.api.getNowPlaying(apiKey, language, page)
    }

    suspend fun getUpcoming(apiKey: String, language: String, page: Int): NowPlaying {
        return RetrofitInstance.api.getUpcoming(apiKey, language, page)
    }

    suspend fun getPopuler(apiKey: String, language: String, page: Int): NowPlaying {
        return RetrofitInstance.api.getPopuler(apiKey, language, page)
    }

    suspend fun getTopRated(apiKey: String, language: String, page: Int): NowPlaying {
        return RetrofitInstance.api.getTopRated(apiKey, language, page)
    }

    suspend fun getDetailMovie(movieId: Int, apiKey: String, language: String): DetailMovieModel {
        return RetrofitInstance.api.getDetailMovie(movieId, apiKey, language)
    }

    suspend fun getDetailMovieCredits(movieId: Int, apiKey: String, language: String): DetailCreditsMovie {
        return RetrofitInstance.api.getDetailMovieCredits(movieId, apiKey, language)
    }

    suspend fun getRecommendations(movieId: Int, apiKey: String, language: String): NowPlaying {
        return RetrofitInstance.api.getRecommendations(movieId, apiKey, language)
    }

    suspend fun getTrailerVideo(movieId: Int, apiKey: String, language: String): TrailerVideoResponse {
        return RetrofitInstance.api.getTrailer(movieId, apiKey, language)
    }
}