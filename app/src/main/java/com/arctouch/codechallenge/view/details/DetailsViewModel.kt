package com.arctouch.codechallenge.view.details

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class DetailsViewModel @Inject constructor(private val api: TmdbApi) : ViewModel() {
    val movie: MutableLiveData<Movie> = MutableLiveData()

    fun movie(id: Long) {
        api.movie(id, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .enqueue(object : Callback<Movie> {
                    override fun onFailure(call: Call<Movie>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<Movie>?, response: Response<Movie>) {
                        movie.postValue(response.body())
                    }
                })
    }
}
