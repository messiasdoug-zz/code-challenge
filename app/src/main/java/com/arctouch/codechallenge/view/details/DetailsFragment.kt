package com.arctouch.codechallenge.view.details

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.di.Injectable
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

class DetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        fun newInstance(id: Int): DetailsFragment {
            val args = Bundle()
            args.putInt("id", id)
            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DetailsViewModel::class.java)

        val movieImageUrlBuilder = MovieImageUrlBuilder()

        viewModel.movie.observeForever { movie ->
            Glide.with(this)
                    .load(movie?.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(backDropImageView)

            titleTextView.text = movie?.title
            genresTextView.text = movie?.genres?.joinToString(separator = ", ") { it.name }
            overviewTextView.text = movie?.overview
            releaseDateTextView.text = movie?.releaseDate
        }
        val id = arguments!!.getInt("id")
        viewModel.movie(id.toLong())
    }
}
