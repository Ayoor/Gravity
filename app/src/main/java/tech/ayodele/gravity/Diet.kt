package tech.ayodele.gravity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient

class Diet : AppCompatActivity() {
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Places SDK
        Places.initialize(applicationContext, "AIzaSyBES7uqmpTZa4g5R0K5mjGbU9ypj2PQobM")
        placesClient = Places.createClient(this)

        // Initialize Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Call the method to find restaurants
        restaurants()
    }

    private fun restaurants() {
        // Define the fields to return for each place data
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.TYPES)

        // Set up a request to find the current place
        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        // Use the Places API to find the current place
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
            return
        }

        // Get the current place
        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
            for (placeLikelihood in response.placeLikelihoods) {
                val place = placeLikelihood.place
                val name = place.name
                val address = place.address
                val latLng = place.latLng
                val types = place.types

                // Check if the place is a restaurant
                if (types != null && types.contains(Place.Type.BEAUTY_SALON)) {
                    // Handle the restaurant data
                    // For example: Display the name and address
                    Log.i("Restaurant", "Restaurant Name: $name, Address: $address")
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors
            Log.e("fetchError", "Error finding current place: ${exception.message}", exception)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }
}