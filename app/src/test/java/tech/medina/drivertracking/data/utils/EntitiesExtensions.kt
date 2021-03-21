package tech.medina.drivertracking.data.utils

import tech.medina.drivertracking.data.datasource.local.db.entities.DeliveryLocal
import tech.medina.drivertracking.data.datasource.local.db.entities.TrackingLocal
import tech.medina.drivertracking.data.datasource.remote.api.entities.DeliveriesResponse
import tech.medina.drivertracking.data.datasource.remote.api.entities.request.TrackingRequest
import tech.medina.drivertracking.data.datasource.remote.api.entities.response.TrackingResponse
import tech.medina.drivertracking.domain.model.Delivery
import tech.medina.drivertracking.domain.model.DeliveryStatus
import tech.medina.drivertracking.domain.model.Tracking
import tech.medina.drivertracking.domain.model.TrackingStatus

fun DeliveriesResponse.Companion.mock(isFull: Boolean = false): DeliveriesResponse =
    DeliveriesResponse(arrayOf(
        DeliveriesResponse.Delivery(
            id = 123,
            address = "Mocked Address",
            latitude = 42.0,
            longitude = 2.0,
            customerName = "Mocked Customer Name",
            requiresSignature = if (isFull) true else null,
            specialInstructions = if (isFull) "Mocked Special Instructions" else null
        )
    ))

fun TrackingRequest.Companion.mock(): TrackingRequest =
    TrackingRequest(
        driverId = 123,
        list = listOf(
            TrackingRequest.TrackingData(42.0, 2.0, 123, 80, 123456)
        ))

fun DeliveryLocal.Companion.mock(status: Int = 0, isFull: Boolean = false): DeliveryLocal =
    DeliveryLocal(
        id = 123,
        address = "Mocked Address",
        latitude = 42.0,
        longitude = 2.0,
        customerName = "Mocked Customer Name",
        status = status,
        fetchTimestamp = 1234,
        requiresSignature= if (isFull) true else null,
        specialInstructions = if (isFull) "Mocked Special Instructions" else null
    )

fun TrackingLocal.Companion.mock(): TrackingLocal =
    TrackingLocal(
        id = 123,
        latitude = 42.0,
        longitude = 2.0,
        deliveryId = 123,
        batteryLevel = 80,
        timestamp = 123456,
        status = 0,
        syncTimestamp = 123456
    )

fun TrackingResponse.Companion.mock(): TrackingResponse =
    TrackingResponse("ok")

fun Delivery.Companion.mock(isFull: Boolean = false): Delivery =
    Delivery(
        id = 123,
        address = "Mocked Address",
        latitude = 42.0,
        longitude = 2.0,
        customerName = "Mocked Customer Name",
        status = DeliveryStatus.DEFAULT,
        fetchTimestamp = 123456,
        specialInstructions = if (isFull) "Mocked Special Instructions" else "",
        requiresSignature = isFull
    )

fun Tracking.Companion.mock(): Tracking =
    Tracking(
        latitude = 42.0,
        longitude = 2.0,
        deliveryId = 123,
        batteryLevel = 80,
        timestamp = 12345,
        status = TrackingStatus.DEFAULT
    )