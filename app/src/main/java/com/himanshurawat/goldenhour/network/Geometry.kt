package com.himanshurawat.goldenhour.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Geometry(@SerializedName("location")val location: PlaceLocation): Serializable