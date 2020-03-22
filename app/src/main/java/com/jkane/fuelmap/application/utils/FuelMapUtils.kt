package com.jkane.fuelmap.application.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

// Default to hardcoded unavailable, but should set to a resource value for localization.
var unavailable: String = "Unavailable"

fun asStringOrUnavailable(input: String?) = input ?: unavailable

fun asStringOrUnavailable(input: Double?) =
    input?.let {
        DecimalFormat("0.00").format(input)
    } ?: unavailable

// TODO use localized string values for this
fun asPriceOrUnavailable(input: Double?) =
    input?.let {
        "$" + DecimalFormat("0.00").format(input)
    } ?: unavailable

fun asDateOrUnavailable(input: Date?) =
    input?.let {
        SimpleDateFormat("MMM dd, YYYY\nhh:mm a", Locale.getDefault()).format(input)
    } ?: unavailable
