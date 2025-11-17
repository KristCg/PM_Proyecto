package com.kriscg.belek.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kriscg.belek.domain.Lugar

@Entity(tableName = "lugares")
data class LugarEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val ubicacion: String,
    val horario: String?,
    val tipo: String,
    val ambiente: String?,
    val servicios: String?,
    val precio: String?,
    val precioEn: String?,
    val calificacion: Float?,
    val imagenUrl: String?,
    val createdAt: String?,
    val momentoDia: String?,
    val rangoPrecio: String?,
    val coordenadas: String?,
    val departamento: String?,
    val latitud: Double?,
    val longitud: Double?,
    val nombreEn: String?,
    val descripcionEn: String?
)

fun Lugar.toEntity(): LugarEntity =
    LugarEntity(
        id = this.id ?: 0,
        nombre = nombre,
        descripcion = descripcion,
        ubicacion = ubicacion,
        horario = horario,
        tipo = tipo,
        ambiente = ambiente,
        servicios = servicios,
        precio = precio,
        precioEn = precioEn,
        calificacion = calificacion,
        imagenUrl = imagenUrl,
        createdAt = createdAt,
        momentoDia = momentoDia,
        rangoPrecio = rangoPrecio,
        coordenadas = coordenadas,
        departamento = departamento,
        latitud = latitud,
        longitud = longitud,
        nombreEn = nombreEn,
        descripcionEn = descripcionEn
    )

fun LugarEntity.toDomain(): Lugar =
    Lugar(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        ubicacion = ubicacion,
        horario = horario,
        tipo = tipo,
        ambiente = ambiente,
        servicios = servicios,
        precio = precio,
        precioEn = precioEn,
        calificacion = calificacion,
        imagenUrl = imagenUrl,
        createdAt = createdAt,
        momentoDia = momentoDia,
        rangoPrecio = rangoPrecio,
        coordenadas = coordenadas,
        departamento = departamento,
        latitud = latitud,
        longitud = longitud,
        nombreEn = nombreEn,
        descripcionEn = descripcionEn
    )
