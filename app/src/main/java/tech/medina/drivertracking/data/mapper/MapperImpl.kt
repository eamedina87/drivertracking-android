package tech.medina.drivertracking.data.mapper

import tech.medina.drivertracking.data.datasource.local.db.entities.DeliveryLocal
import tech.medina.drivertracking.data.datasource.local.db.entities.TrackingLocal
import tech.medina.drivertracking.data.datasource.remote.api.entities.DeliveryRemote
import tech.medina.drivertracking.data.datasource.remote.api.entities.request.TrackingData
import tech.medina.drivertracking.data.datasource.remote.api.entities.request.TrackingRequest
import tech.medina.drivertracking.domain.model.*
import javax.inject.Inject

class MapperImpl @Inject constructor(): Mapper {

    override fun toLocal(entity: DeliveryRemote, timestamp: Long): DeliveryLocal =
        DeliveryLocal(
            id = entity.id,
            address = entity.address,
            latitude = entity.latitude,
            longitude = entity.longitude,
            customerName = entity.customerName,
            status = DeliveryStatus.DEFAULT.ordinal,
            fetchTimestamp = timestamp,
            requiresSignature = entity.requiresSignature,
            specialInstructions = entity.specialInstructions
        )

    override fun toRemote(model: Tracking): TrackingData =
        TrackingData(
            latitude = model.latitude,
            longitude = model.longitude,
            deliveryId = model.deliveryId,
            batteryLevel = model.batteryLevel,
            timestamp = model.timestamp
        )

    override fun toRemote(tracking: List<Tracking>, driverId: Long): TrackingRequest {
        val data: List<TrackingData> = tracking.map {
            toRemote(it)
        }
        return TrackingRequest(driverId, data)
    }

    override fun toModel(entity: DeliveryLocal): Delivery =
        Delivery(
            id = entity.id,
            address = entity.address,
            latitude = entity.latitude,
            longitude = entity.longitude,
            customerName = entity.customerName,
            status = entity.status.toDeliveryStatus(),
            fetchTimestamp = entity.fetchTimestamp,
            specialInstructions = entity.specialInstructions ?: "",
            requiresSignature = entity.requiresSignature ?: false
        )

    override fun toLocal(model: Delivery): DeliveryLocal =
        DeliveryLocal(
            id = model.id,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            customerName = model.customerName,
            status = model.status.ordinal,
            fetchTimestamp = model.fetchTimestamp,
            specialInstructions = model.specialInstructions,
            requiresSignature = model.requiresSignature
        )

    override fun toLocal(model: Tracking, timestamp: Long?): TrackingLocal =
        TrackingLocal(
            id = model.id,
            latitude = model.latitude,
            longitude = model.longitude,
            deliveryId = model.deliveryId,
            batteryLevel = model.batteryLevel,
            timestamp = model.timestamp,
            status = model.status.ordinal,
            syncTimestamp = timestamp
        )

    override fun toModel(entity: TrackingLocal): Tracking =
        Tracking(
            id = entity.id,
            latitude = entity.latitude,
            longitude = entity.longitude,
            deliveryId = entity.deliveryId,
            batteryLevel = entity.batteryLevel,
            timestamp = entity.timestamp,
            status = entity.status.toTrackingStatus()
        )

    override fun toLocation(frameworkLocation: android.location.Location): Location =
        Location(
            latitude = frameworkLocation.latitude,
            longitude = frameworkLocation.longitude,
            timestamp = frameworkLocation.time
        )

}