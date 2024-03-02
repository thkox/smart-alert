package eu.tkacas.smartalert.models

import java.util.HashMap

data class LocationCriticalWeatherPhenomenonData(
    val locationID: String,
    val locationName : String,
    val numOfReports: Int
)

data class ListOfLocationCriticalWeatherPhenomenonData(
    val list: MutableList<LocationCriticalWeatherPhenomenonData> = mutableListOf()
)