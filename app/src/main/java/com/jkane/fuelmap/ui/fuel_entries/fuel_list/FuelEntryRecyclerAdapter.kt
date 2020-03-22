package com.jkane.fuelmap.ui.fuel_entries.fuel_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jkane.fuelmap.R
import com.jkane.fuelmap.application.network.models.FuelEntry
import com.jkane.fuelmap.application.utils.asDateOrUnavailable
import com.jkane.fuelmap.application.utils.asPriceOrUnavailable
import com.jkane.fuelmap.application.utils.asStringOrUnavailable
import kotlinx.android.synthetic.main.component_fuel_entry_row.view.*

/**
 * Recyclerview with a viewholder to display fuel entry data.
 */
class FuelEntryRecyclerAdapter(
    private var fuelEntries: List<FuelEntry>,
    var onBottomReachedListener: (() -> Unit)
) :
    RecyclerView.Adapter<FuelEntryViewHolder>() {

    override fun getItemCount() = fuelEntries.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FuelEntryViewHolder {
        return FuelEntryViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: FuelEntryViewHolder, position: Int) {
        holder.bind(fuelEntries[position])
        if (position == fuelEntries.size - 1) onBottomReachedListener.invoke()
    }

    fun addEntries(entries: List<FuelEntry>) {
        fuelEntries = entries
    }
}

class FuelEntryViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(
        inflater.inflate(R.layout.component_fuel_entry_row, parent, false)
    ) {

    // Consider these fields into single text fields that concat resource with value for cleaner
    // localization.
    fun bind(fuelEntry: FuelEntry) {
        fuelEntry.run {
            Glide
                .with(itemView)
                .load(images?.getOrNull(0)?.fullUrl)
                .override(64, 64)
                .placeholder(R.drawable.ic_local_gas_station_24px)
                .into(itemView.imgView)

            itemView.vehicle_name.text = asStringOrUnavailable(vehicleName)
            itemView.date.text = asDateOrUnavailable(date)
            itemView.cost_value.text = asPriceOrUnavailable(totalAmount)
            itemView.cost_per_mile_value.text = asPriceOrUnavailable(costPerMi)
            itemView.gallons_value.text = asStringOrUnavailable(usGallons)
            itemView.fuel_type_value.text = asStringOrUnavailable(fuelTypeName)
            itemView.price_per_gallon_value.text = asPriceOrUnavailable(pricePerVolumeUnit)
            itemView.vendor_value.text = asStringOrUnavailable(vendorName)
            itemView.reference_number_value.text = asStringOrUnavailable(reference)
        }
    }
}
