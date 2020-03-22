package com.jkane.fuelmap.ui.fuel_entries.fuel_map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.jkane.fuelmap.R
import com.jkane.fuelmap.ui.fuel_entries.FuelEntriesViewModel
import kotlinx.android.synthetic.main.fragment_fuel_map.*

/**
 * Rather simplistic map that will display pins at fuel entries with lat and long values.
 *
 * Wanted to enhance this but couldn't find a way to enforce geolocation.lat/long being required
 * via network filters. Also didn't see a clean or clear way to geofence based on these for
 * displaying nearby entries only. Would've dug in more with more time.
 */
class FuelMapFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var viewModel: FuelEntriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_fuel_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(FuelEntriesViewModel::class.java)

        map.onCreate(savedInstanceState)
        map.onResume()
        observeFuelEntries()

        try {
            MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            Log.e(this.tag, "Maps failed to initialize: $e")
        }

        map.getMapAsync { googleMap ->
            mMap = googleMap
            val bounds = LatLngBounds.Builder()
            viewModel.fuelEntries.value?.forEach {
                if (it.geolocation?.latitude != null && it.geolocation.longitude != null) {
                    val pos = LatLng(it.geolocation.latitude, it.geolocation.longitude)
                    mMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title("${it.vehicleName} @ ${it.vendorName}")
                            .snippet(
                                "Cost : $${it.totalAmount} | " +
                                        "Gallons : ${it.usGallons} | " +
                                        "Price per gallon : $${it.pricePerVolumeUnit}"
                            )
                    )
                    bounds.include(pos)
                }
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10))
        }
    }

    private fun observeFuelEntries() {
        viewModel.fuelEntries.observe(viewLifecycleOwner, Observer {
            try {
                MapsInitializer.initialize(requireContext())
            } catch (e: Exception) {
                Log.e(this.tag, "Maps failed to initialize: $e")
            }
        })
    }
}