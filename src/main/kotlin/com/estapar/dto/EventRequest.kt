package com.estapar.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event_type",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(value = EntryRequest::class, name = "ENTRY"),
    JsonSubTypes.Type(value = ParkingRequest::class, name = "PARKED"),
    JsonSubTypes.Type(value = ExitRequest::class, name = "EXIT")
)
interface EventRequest
