import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.GenreResponse
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.view.home.HomeViewModel
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
class HomeViewModelTest {

    @get:Rule
    val mActivityRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var api: TmdbApi

    @InjectMocks
    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var progressObserver: Observer<Boolean>

    @Mock
    private lateinit var upcomingMoviesResponseCall: Call<UpcomingMoviesResponse>

    @Mock
    private lateinit var genreResponseCall: Call<GenreResponse>

    @Mock
    private lateinit var upcomingMoviesResponse: UpcomingMoviesResponse

    @Mock
    private lateinit var genreResponse: GenreResponse

    @Before
    fun setUp() {
        `when`(api.genres(anyString(), anyString())).thenReturn(genreResponseCall)
        `when`(api.upcomingMovies(anyString(), anyString(), anyLong(), anyString())).thenReturn(upcomingMoviesResponseCall)
    }

    @Test
    fun callLoaderAsTrue() {
        viewModel.upcomingMovies(0)
        viewModel.loader.observeForever(progressObserver)
        verify(progressObserver).onChanged(true)
    }

    @Test
    fun callApiCallGenres() {
        doAnswer(null, genreResponseCall, Response.success(genreResponse))
                .`when`(genreResponseCall).enqueue(any(Callback::class.java) as Callback<GenreResponse>?)

        viewModel.upcomingMovies(0)
        verify(api).genres(anyString(), anyString())
    }

    @Test
    fun callApiCallUpcomingMovies() {
        doAnswer(null, genreResponseCall, Response.success(genreResponse))
                .`when`(genreResponseCall).enqueue(any(Callback::class.java) as Callback<GenreResponse>?)

        doAnswer(null, upcomingMoviesResponseCall, Response.success(upcomingMoviesResponse))
                .`when`(upcomingMoviesResponseCall).enqueue(any(Callback::class.java) as Callback<UpcomingMoviesResponse>?)

        viewModel.upcomingMovies(0)
        verify(api).upcomingMovies(anyString(), anyString(), anyLong(), anyString())
    }

    @Test
    fun callLoaderAsFalse() {
        doAnswer(null, genreResponseCall, Response.success(genreResponse))
                .`when`(genreResponseCall).enqueue(any(Callback::class.java) as Callback<GenreResponse>?)

        doAnswer(null, upcomingMoviesResponseCall, Response.success(upcomingMoviesResponse))
                .`when`(upcomingMoviesResponseCall).enqueue(any(Callback::class.java) as Callback<UpcomingMoviesResponse>?)

        viewModel.upcomingMovies(0)
        viewModel.loader.observeForever(progressObserver)
        verify(progressObserver).onChanged(false)
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