package com.jkane.fuelmap.application.network.models

import java.util.*

data class FuelEntry(
	val date: Date? = null,
	val usGallons: String? = null,
	val id: Int,
	val fuelTypeName: String? = null,
	val images: List<FleetImage?>? = null,
	val vendorName: String? = null,
	val usGallonsPerHr: Double? = null,
	val usageInMi: Double? = null,
	val totalAmount: Double? = null,
	val vendorId: Int? = null,
	val vehicleName: String? = null,
	val createdAt: String? = null,
	val costPerMi: Double? = null,
	val updatedAt: String? = null,
	val vehicleId: Int? = null,
	val pricePerVolumeUnit: Double? = null,
	val fuelTypeId: Int? = null,
	val reference: String? = null,
	val geolocation: GeoLocation? = null
)
