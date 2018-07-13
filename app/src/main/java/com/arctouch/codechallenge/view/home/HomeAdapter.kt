package com.arctouch.codechallenge.view.home

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(private val callback: (Movie) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    private val movies: ArrayList<Movie> = ArrayList()

    companion object {
        private const val PAGE_SIZE: Int = 5
        private const val ITEM: Int = 0
        private const val LOADING: Int = 1
        private const val HERO: Int = 2
    }

    class ViewHolder(itemView: View, private val callback: (Movie) -> Unit) : RecyclerView.ViewHolder(itemView) {

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie) {
            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate

            Glide.with(itemView)
                    .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(itemView.posterImageView)

            itemView.setOnClickListener {
                callback.invoke(movie)
            }
        }
    }

    class PaginationScrollListener(private val layoutManager: LinearLayoutManager, private val callback: (Int) -> Unit) : RecyclerView.OnScrollListener() {

        private var currentPage = 1

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                loadMoreItems()
            }
        }

        private fun loadMoreItems() {
            currentPage += 1
            callback.invoke(currentPage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view, callback)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HERO
        } else {
            if (position == movies.size) LOADING else ITEM
        }
    }

    fun add(moviesResult: List<Movie>) {
        for (movie in moviesResult) {
            add(movie)
        }
    }

    private fun add(movie: Movie) {
        this.movies.add(movie)
        notifyItemInserted(movies.size - 1)
    }
}
