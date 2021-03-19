package tech.medina.drivertracking.data.datasource.remote.api.entities

import com.google.gson.annotations.SerializedName

data class TrackingRemote(
    @SerializedName("driver_id")
    val driverId: Long,
    @SerializedName("tracking_data")
    val list: List<TrackingData>
) {

    //This declaration is necessary to provide mocks of this class by extensions in the tests
    companion object {  }

    data class TrackingData(
        val latitude: Double,
        val longitude: Double,
        @SerializedName("delivery_id")
        val deliveryId: Long,
        @SerializedName("battery_level")
        val batteryLevel: Int,
        val timestamp: Long
    )

}