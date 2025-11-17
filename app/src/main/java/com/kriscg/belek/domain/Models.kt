package com.kriscg.belek.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class Usuario(
    val id: String? = null,
    val username: String,
    val email: String,
    val descripcion: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Lugar(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val horario: String? = null,
    val tipo: String,
    val ambiente: String? = null,
    val servicios: String? = null,
    val precio: String? = null,
    @SerialName("precio_en")
    val precioEn: String? = null,
    val calificacion: Float? = null,
    @SerialName("imagen_url")
    val imagenUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("momento_dia")
    val momentoDia: String? = null,
    @SerialName("rango_precios")
    val rangoPrecio: String? = null,
    val coordenadas: String? = null,
    val departamento: String? = null,
    val latitud: Double? = null,
    val longitud: Double? = null,
    @SerialName("nombre_en")
    val nombreEn: String? = null,
    @SerialName("descripcion_en")
    val descripcionEn: String? = null
)

@Serializable
data class Resena(
    val id: Int? = null,
    @SerialName("lugar_id")
    val lugarId: Int,
    @SerialName("usuario_id")
    val usuarioId: String,
    val calificacion: Int,
    val comentario: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Viaje(
    val id: Int? = null,
    @SerialName("usuario_id")
    val usuarioId: String,
    val destino: String,
    val tipo: String,
    val presupuesto: String,
    val intereses: String,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Favorito(
    val id: Int? = null,
    @SerialName("usuario_id")
    val usuarioId: String,
    @SerialName("lugar_id")
    val lugarId: Int,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Guardado(
    val id: Int? = null,
    @SerialName("usuario_id")
    val usuarioId: String,
    @SerialName("lugar_id")
    val lugarId: Int,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class Event(
    val id: String = "",
    val title: String,
    val description: String? = null,
    @SerialName("event_date")
    val eventDate: String, // "2025-05-15"
    @SerialName("start_time")
    val startTime: String? = null,
    @SerialName("end_time")
    val endTime: String? = null,
    val city: String,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val category: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("is_recurring")
    val isRecurring: Boolean = false,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
) {
    fun getLocalDate(): LocalDate = LocalDate.parse(eventDate)

    fun getFormattedDate(): String {
        val date = getLocalDate()
        val months = listOf(
            "enero", "febrero", "marzo", "abril", "mayo", "junio",
            "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
        )
        return "${date.dayOfMonth} de ${months[date.monthValue - 1]} de ${date.year}"
    }

    fun getFormattedStartTime(): String? {
        return startTime?.let {
            val parts = it.split(":")
            val hour = parts[0].toInt()
            val minute = parts[1]
            val amPm = if (hour >= 12) "PM" else "AM"
            val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
            "$displayHour:$minute $amPm"
        }
    }
}

@Serializable
data class EventPlace(
    val id: String = "",
    @SerialName("event_id")
    val eventId: String,
    @SerialName("place_id")
    val placeId: Int,
    @SerialName("recommendation_type")
    val recommendationType: String,
    @SerialName("display_order")
    val displayOrder: Int = 0,
    @SerialName("created_at")
    val createdAt: String? = null
)

@Serializable
data class PlaceRecommendation(
    val eventPlace: EventPlace,
    val lugar: Lugar
) {
    val name: String get() = lugar.nombre
    val nameEn: String? get() = lugar.nombreEn
    val description: String? get() = lugar.descripcion
    val descriptionEn: String? get() = lugar.descripcionEn
    val imageUrl: String? get() = lugar.imagenUrl
    val type: String get() = eventPlace.recommendationType
    val rating: Float? get() = lugar.calificacion
    val price: String? get() = lugar.precio
    val priceEn: String? get() = lugar.precioEn
}

enum class RecommendationType(val value: String, val displayNameEs: String, val displayNameEn: String) {
    HOTEL("hotel", "Hoteles", "Hotels"),
    RESTAURANT("restaurant", "Restaurantes", "Restaurants"),
    ATTRACTION("attraction", "Atracciones", "Attractions"),
    GENERAL("general", "General", "General");

    companion object {
        fun fromValue(value: String): RecommendationType? {
            return values().find { it.value == value }
        }
    }
}