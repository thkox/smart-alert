package eu.tkacas.smartalert.viewmodel.employee

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import eu.tkacas.smartalert.app.SharedPrefManager
import eu.tkacas.smartalert.database.cloud.CloudFunctionsUtils
import eu.tkacas.smartalert.database.cloud.FirebaseUtils
import eu.tkacas.smartalert.interfacesAPI.PlacesAPI
import eu.tkacas.smartalert.models.Alert
import eu.tkacas.smartalert.models.Bounds
import eu.tkacas.smartalert.models.CriticalLevel
import eu.tkacas.smartalert.models.CriticalWeatherPhenomenon
import eu.tkacas.smartalert.models.LatLng
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class AlertCitizensFormViewModel(context: Context) : ViewModel() {
    val firebase = FirebaseUtils()


    val sharedPrefManager = SharedPrefManager(context)

    private val _selectedArea = mutableStateOf("")

    private val _selectedWeatherPhenomenon = mutableStateOf(CriticalWeatherPhenomenon.EARTHQUAKE)
    val selectedDangerLevelButton = mutableStateOf(CriticalLevel.LOW)

    private val _cloudFunctionsUtils = CloudFunctionsUtils()

    fun setSelectedArea(area: String) {
        _selectedArea.value = area
    }

    fun setSelectedWeatherPhenomenon(phenomenon: CriticalWeatherPhenomenon) {
        _selectedWeatherPhenomenon.value = phenomenon
    }

    fun setSelectedDangerLevelButton(level: CriticalLevel) {
        selectedDangerLevelButton.value = level
    }

    private val _retrofit: Retrofit
        get() = setupRetrofit()

    private fun setupRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/") // Base URL for Google Places API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createPlacesAPI(): PlacesAPI {
        return _retrofit.create(PlacesAPI::class.java)
    }

    private fun getBounds(): Bounds {
        return Bounds(
            LatLng(
                sharedPrefManager.getBoundsNorthEastLat(),
                sharedPrefManager.getBoundsNorthEastLng()
            ), LatLng(
                sharedPrefManager.getBoundsSouthWestLat(),
                sharedPrefManager.getBoundsSouthWestLng()
            )
        )

    }

    suspend fun sendAlertToCitizens() {
        val selectedLocation = if (_selectedArea.value.isEmpty()) {
            sharedPrefManager.getLocationName()
        } else {
            _selectedArea.value
        }

        val selectedLocationID = sharedPrefManager.getLocationID()

        val selectedWeatherPhenomenon = _selectedWeatherPhenomenon.value
        val selectedDangerLevelButton = selectedDangerLevelButton.value

        val database = firebase.storageRef()

        // Create a reference to the "notificationsToCitizens" node
        val myRef = database.getReference("notificationsToCitizens")

        // Create a unique key for the new alert
        val key = myRef.push().key

        // Get the bounds from shared preferences
        var bounds = getBounds()

        if (bounds.northeast == (LatLng(0.0, 0.0)) && bounds.southwest == (LatLng(0.0, 0.0))) {
            val placeId = getPlaceIdFromLocationName(selectedLocation)
            bounds = placeId?.let { getPlaceDetails(it) }!!
        }

        // Create am instance of alert
        val alert = Alert(
            locationName = selectedLocation,
            locationBounds = bounds,
            criticalWeatherPhenomenon = selectedWeatherPhenomenon,
            criticalLevel = selectedDangerLevelButton
        )

        // Send the data to the server
        if (key != null) {
            myRef.child(key).setValue(alert)
        }

        // Delete the alerts from the database (last 6 hours)
        _cloudFunctionsUtils.deleteAlertsByPhenomenonAndLocation(
            selectedWeatherPhenomenon.toString(),
            selectedLocationID
        )
    }

    private suspend fun getPlaceIdFromLocationName(locationName: String): String? {
        val placesAPI = createPlacesAPI()
        try {
            val placesResponse = placesAPI.getPlacesAutocomplete(
                locationName,
                "API_KEY_GC"
            ).await()

            // Check if any places were found
            if (placesResponse.predictions.isNotEmpty()) {
                // Return the place_id of the first place in the list
                return placesResponse.predictions[0].place_id
            }
        } catch (e: Exception) {
            Log.e("PlaceIdLookup", "Failed to get place id", e)
        }
        return null
    }

    private suspend fun getPlaceDetails(placeId: String): Bounds? {
        val placesAPI = createPlacesAPI()
        val deferredResponse =
            placesAPI.getPlaceDetails(placeId, "API_KEY_GC")

        try {
            val response = deferredResponse.await()
            val placeDetails = response.result
            val viewport = placeDetails.geometry.viewport
            return Bounds(
                LatLng(viewport.northeast.lat, viewport.northeast.lng),
                LatLng(viewport.southwest.lat, viewport.southwest.lng)
            )
        } catch (e: Exception) {
            Log.e("PlaceDetails", "Failed to get place details", e)
        }
        return null
    }


}