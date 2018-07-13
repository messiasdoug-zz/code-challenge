package com.arctouch.codechallenge.view.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Stubber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest {

    @get:Rule
    val mActivityRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: TmdbApi

    @InjectMocks
    private lateinit var viewModel: DetailsViewModel

    @Mock
    private lateinit var movieCall: Call<Movie>

    @Mock
    private lateinit var movieObserver: Observer<Movie>

    @Mock
    private lateinit var movie: Movie

    @Before
    fun setUp() {
        Mockito.`when`(api.movie(anyLong(), anyString(), anyString())).thenReturn(movieCall)
    }

    @Test
    fun call() {
        doAnswer(null, movieCall, Response.success(movie))
                .`when`(movieCall).enqueue(Mockito.any(Callback::class.java) as Callback<Movie>?)

        viewModel.movie(0)
        viewModel.movie.observeForever(movieObserver)
        verify(movieObserver).onChanged(movie)
    }

    private fun <T> doAnswer(throwable: Throwable?, call: Call<T>, response: Response<T>): Stubber {
        return Mockito.doAnswer { invocationOnMock ->
            val callback = invocationOnMock.getArgument<Callback<T>>(0)
            if (throwable == null) {
                callback.onResponse(call, response)
            } else {
                callback.onFailure(call, throwable)
            }
            null
        }
    }
}