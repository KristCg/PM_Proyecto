package com.kriscg.belek.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val calificacion: Float? = null,
    @SerialName("imagen_url")
    val imagenUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null
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