package com.gedanken.catstomers.second.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gedanken.catstomers.R
import com.gedanken.catstomers.second.model.remote.BreedsResponse
import com.gedanken.catstomers.second.viewmodel.BreedsViewModel
import kotlinx.android.synthetic.main.fragment_second.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class BreedsFragment : Fragment() {
    private lateinit var movieAdapter: BreedRecyclerViewAdapter

    private lateinit var breedsViewModel: BreedsViewModel

    var isLastPage: Boolean = false
    var isLoading: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            breedsViewModel = ViewModelProvider(it).get(BreedsViewModel::class.java)
        }


        movieAdapter = BreedRecyclerViewAdapter(ArrayList(), this)
        val linearLayoutManager = LinearLayoutManager(activity)
        cat_list_recycler.layoutManager = linearLayoutManager
        cat_list_recycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        //var loadTimes = 0
        cat_list_recycler.adapter = movieAdapter
        //cat_list_recycler.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager){
        //    override fun isLastPage(): Boolean {
        //        return isLastPage
        //    }
        //    override fun isLoading(): Boolean {
        //        return isLoading
        //    }
        //    override fun loadMoreItems() {
        //        isLoading = true
        //        //you have to call loadmore items to get more data
        //        loadTimes++
        //        breedsViewModel.getCatBreedsPageNo(loadTimes)
        //       Toast.makeText(activity,"load more activated",Toast.LENGTH_SHORT).show()
        //    }
        //
        //})
        val searchIcon = country_search.findViewById<ImageView>(R.id.search_mag_icon)
        searchIcon.setColorFilter(Color.BLACK)
        val cancelIcon = country_search.findViewById<ImageView>(R.id.search_close_btn)
        cancelIcon.setColorFilter(Color.BLACK)
        val textView = country_search.findViewById<TextView>(R.id.search_src_text)
        textView.setTextColor(Color.BLACK)

        country_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieAdapter.filter.filter(newText)
                return false
            }
        })


        progress.visibility = View.VISIBLE
        breedsViewModel.breedList.observe(viewLifecycleOwner, Observer { onResult(it) })
        breedsViewModel.errorMessage.observe(viewLifecycleOwner, Observer { onError(it) })
    }

    fun selectBreed(breedResponse: BreedsResponse){
        breedsViewModel.selectBreed(breedResponse)
        findNavController().navigate(R.id.action_SecondFragment_to_ThirdFragment)
    }
    /**
     * Method called when a change in MainViewModel.logInStatus MutableLiveData is observed
     */
    private fun onResult(breedList: List<BreedsResponse>) {
        if(breedList.isEmpty()){
            progress.visibility = View.INVISIBLE
        } else {
            progress.visibility = View.VISIBLE
        }
        isLoading = false
        movieAdapter.setNetCats(breedList)
        // Not doing anything yet with this list except a toast
        Toast.makeText(activity, "Got ${breedList.size} breeds", Toast.LENGTH_SHORT).show()
    }

    /**
     * Method triggered when we observe a change in BreedsViewModel.errorMessage MutableLiveData
     * @param error Error message describing what went wrong
     */
    private fun onError(error: String) {
        progress.visibility = View.INVISIBLE
        isLoading = false
        error.let {
            if (!it.isBlank()) {
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        progress.visibility = View.VISIBLE
    }
}