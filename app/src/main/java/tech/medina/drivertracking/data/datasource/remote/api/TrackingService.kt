package tech.medina.drivertracking.data.datasource.remote.api

import retrofit2.http.POST
import tech.medina.drivertracking.data.datasource.remote.api.entities.TrackingRemote
import tech.medina.drivertracking.data.datasource.remote.api.entities.response.TrackingResponse

interface TrackingService {

    @POST
    fun postTracking(data: TrackingRemote): TrackingResponse

}