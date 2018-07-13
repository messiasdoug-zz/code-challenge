package com.arctouch.codechallenge.view.home

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val api: TmdbApi) : ViewModel() {

    val movies: MutableLiveData<List<Movie>> = MutableLiveData()
    val loader: MutableLiveData<Boolean> = MutableLiveData()

    fun upcomingMovies(page: Long) {
        loader.postValue(true)
        if (Cache.genres.isEmpty()) {
            api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                    .enqueue(object : Callback<GenreResponse> {
                        override fun onFailure(call: Call<GenreResponse>?, t: Throwable?) {

                        }

                        override fun onResponse(call: Call<GenreResponse>?, response: Response<GenreResponse>) {
                            Cache.cacheGenres(response.body()?.genres!!)
                        }
                    })
        }

        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
                .enqueue(object : Callback<UpcomingMoviesResponse> {
                    override fun onFailure(call: Call<UpcomingMoviesResponse>?, t: Throwable?) {
                        loader.postValue(false)
                    }

                    override fun onResponse(call: Call<UpcomingMoviesResponse>?, response: Response<UpcomingMoviesResponse>) {
                        val moviesWithGenres = response.body()?.results!!.map { movie ->
                            movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                        }
                        if (!moviesWithGenres.isEmpty()) {
                            movies.postValue(moviesWithGenres)
                        }
                        loader.postValue(false)
                    }
                })
    }
}
