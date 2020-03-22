package com.jkane.fuelmap.ui.fuel_entries.fuel_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jkane.fuelmap.R
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesViewModel
import kotlinx.android.synthetic.main.fragment_fuel_list.*

/**
 * Simple fragment containing a recyclerview that observes on the #[FuelEntriesActivity]'s
 * #[FuelEntriesViewModel] and updates the recyclerview adapter when fuel entries list changes.
 *
 * Also establishes a listener for swipe down to refresh and when the recyclerview hits the bottom
 * of the list to trigger pagination.
 */
class FuelListFragment : Fragment() {

    private lateinit var viewModel: FuelEntriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_fuel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FuelEntriesViewModel::class.java)
        swipeToRefresh.setOnRefreshListener { viewModel.clearAndRefresh() }
        createListAdapter()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.fuelEntries.observe(viewLifecycleOwner, Observer {
            (fuel_entry_recycler.adapter as FuelEntryRecyclerAdapter).addEntries(it)
            fuel_entry_recycler.adapter?.notifyDataSetChanged()
        })
    }

    private fun createListAdapter() {
        fuel_entry_recycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = FuelEntryRecyclerAdapter(
                fuelEntries = listOf(),
                onBottomReachedListener = { viewModel.loadNextPage() }
            )
        }
    }
}
