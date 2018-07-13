import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.UpcomingMoviesResponse
import com.arctouch.codechallenge.view.home.HomeViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private lateinit var api: TmdbApi
    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var observable: Observable<UpcomingMoviesResponse>

    @Before
    fun setUp() {
        api = mock(TmdbApi::class.java)
        `when`(api.upcomingMovies("", "", 0, "")).thenReturn(observable)
        `when` (observable.subscribeOn(mock(Scheduler::class.java))).thenReturn(observable)
        `when` (observable.observeOn(mock(Scheduler::class.java))).thenReturn(observable)
        viewModel = HomeViewModel(api)
    }

    @Test
    fun verifyApiCall() {
        viewModel.upcomingMovies(0)
        verify(api).upcomingMovies("", "", 0, "")
    }
}