package com.jkane.fuelmap.ui.fuel_entries

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jkane.fuelmap.R
import com.jkane.fuelmap.application.App
import com.jkane.fuelmap.application.network.repo.FleetRepo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fuel_list.*
import javax.inject.Inject

class FuelEntriesActivity : AppCompatActivity() {

    @Inject
    lateinit var fleetRepo: FleetRepo

    private lateinit var viewModel: FuelEntriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this.applicationContext as App).getAppComponent().inject(this)

        setContentView(R.layout.activity_main)
        setupBottomNav()
        viewModel = ViewModelProvider(this).get(FuelEntriesViewModel::class.java)
        observeLoading()
        observeError()
        viewModel.setInitialState(fleetRepo)
    }

    /**
     * Creates filter menu and upon filter selection updates the viewmodel filter map that is
     * passed to the network request.
     *
     * TODO break filters into enum file or something cleaner than hardcoded strings
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        require_price_data.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.addFilter("q[total_amount_gt]", "0")
                viewModel.addFilter("q[us_gallons_gt]", "0")
                viewModel.addFilter("q[price_per_volume_unit_gt]", "0")
            } else {
                viewModel.removeFilter("q[total_amount_gt]")
                viewModel.removeFilter("q[us_gallons_gt]")
                viewModel.removeFilter("q[price_per_volume_unit_gt]")
            }
            viewModel.clearAndRefresh()
        }

        sort_by_most_recent.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) viewModel.addFilter(
                "q[s]",
                "date+desc"
            ) else viewModel.removeFilter("q[s]")
            viewModel.clearAndRefresh()
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title.toString() == "filter") {
            filter_layout.visibility =
                if (filter_layout.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Observes isLoading on the ViewModel and renders a loading overlay accordingly.
     *
     * Loading state is currently being controlled by loadData().
     */
    private fun observeLoading() {
        viewModel.isLoading.observe(this, Observer {
            loading_overlay.visibility = if (it) View.VISIBLE else View.GONE
            swipeToRefresh.isRefreshing = false
        })
    }

    /**
     * Observes viewmodel error and displays a toast message upon value change.
     */
    private fun observeError() {
        viewModel.error.observe(this, Observer {
            Snackbar.make(
                findViewById(R.id.container),
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }

    /**
     * Boilerplate from AS when creating a bottom nav project.
     * Creates the bottom navigation appearance and logic via res files
     */
    private fun setupBottomNav() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fuel_list,
                R.id.fuel_map
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
