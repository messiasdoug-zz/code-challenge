package com.arctouch.codechallenge.view.home

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.di.Injectable
import com.arctouch.codechallenge.view.common.NavigationController
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var controller: NavigationController

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HomeViewModel::class.java)

        val adapter = HomeAdapter { movie ->
            controller.toDetails(movie.id)
        }

        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(HomeAdapter.PaginationScrollListener(linearLayoutManager) { page ->
            upcomingMovies(page.toLong())
        })

        attachObserver(adapter)

        upcomingMovies(1)
    }

    private fun attachObserver(adapter: HomeAdapter) {
        viewModel.loader.observeForever { isLoad ->
            if (isLoad!!) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
        viewModel.movies.observeForever {
            adapter.add(it!!)
        }
    }

    private fun upcomingMovies(page: Long) {
        viewModel.upcomingMovies(page)
    }
}
