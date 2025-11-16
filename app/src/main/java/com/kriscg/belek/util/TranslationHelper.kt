package com.kriscg.belek.util

import android.content.Context
import com.kriscg.belek.domain.Lugar

object TranslationHelper {


    fun getCurrentLanguage(context: Context): String {
        val preferencesManager = com.kriscg.belek.data.userpreferences.PreferencesManager.getInstance(context)
        return preferencesManager.preferencesFlow.value.language
    }

    fun getLugarNombre(lugar: Lugar, language: String): String {
        return when (language) {
            "en" -> lugar.nombreEn ?: lugar.nombre
            else -> lugar.nombre
        }
    }

    fun getLugarDescripcion(lugar: Lugar, language: String): String {
        return when (language) {
            "en" -> lugar.descripcionEn ?: lugar.descripcion
            else -> lugar.descripcion
        }
    }

    fun translateTipo(tipo: String, toLanguage: String): String {
        val translations = mapOf(
            "Arqueológicos" to mapOf("en" to "Archaeological", "es" to "Arqueológicos"),
            "Naturales" to mapOf("en" to "Natural", "es" to "Naturales"),
            "Históricos" to mapOf("en" to "Historical", "es" to "Históricos"),
            "Culturales" to mapOf("en" to "Cultural", "es" to "Culturales"),
            "Ecoturísticos" to mapOf("en" to "Ecotourism", "es" to "Ecoturísticos"),
            "Gastronómicos" to mapOf("en" to "Gastronomic", "es" to "Gastronómicos"),
            "Recreativos" to mapOf("en" to "Recreational", "es" to "Recreativos"),
            "Comerciales" to mapOf("en" to "Commercial", "es" to "Comerciales"),

            "Archaeological" to mapOf("en" to "Archaeological", "es" to "Arqueológicos"),
            "Natural" to mapOf("en" to "Natural", "es" to "Naturales"),
            "Historical" to mapOf("en" to "Historical", "es" to "Históricos"),
            "Cultural" to mapOf("en" to "Cultural", "es" to "Culturales"),
            "Ecotourism" to mapOf("en" to "Ecotourism", "es" to "Ecoturísticos"),
            "Gastronomic" to mapOf("en" to "Gastronomic", "es" to "Gastronómicos"),
            "Recreational" to mapOf("en" to "Recreational", "es" to "Recreativos"),
            "Commercial" to mapOf("en" to "Commercial", "es" to "Comerciales")
        )

        return translations[tipo]?.get(toLanguage) ?: tipo
    }

    fun translateAmbiente(ambiente: String, toLanguage: String): String {
        val translations = mapOf(
            "Familiares" to mapOf("en" to "Family-friendly", "es" to "Familiares"),
            "Románticos" to mapOf("en" to "Romantic", "es" to "Románticos"),
            "Aventura" to mapOf("en" to "Adventure", "es" to "Aventura"),
            "Cultural" to mapOf("en" to "Cultural", "es" to "Cultural"),
            "Nocturnos" to mapOf("en" to "Nightlife", "es" to "Nocturnos"),
            "Espiritual" to mapOf("en" to "Spiritual", "es" to "Espiritual"),

            "Family-friendly" to mapOf("en" to "Family-friendly", "es" to "Familiares"),
            "Romantic" to mapOf("en" to "Romantic", "es" to "Románticos"),
            "Adventure" to mapOf("en" to "Adventure", "es" to "Aventura"),
            "Nightlife" to mapOf("en" to "Nightlife", "es" to "Nocturnos"),
            "Spiritual" to mapOf("en" to "Spiritual", "es" to "Espiritual")
        )

        return translations[ambiente]?.get(toLanguage) ?: ambiente
    }

    fun translatePresupuesto(presupuesto: String, toLanguage: String): String {
        val translations = mapOf(
            "Alto" to mapOf("en" to "High", "es" to "Alto"),
            "Mediano" to mapOf("en" to "Medium", "es" to "Mediano"),
            "Bajo" to mapOf("en" to "Low", "es" to "Bajo"),

            "High" to mapOf("en" to "High", "es" to "Alto"),
            "Medium" to mapOf("en" to "Medium", "es" to "Mediano"),
            "Low" to mapOf("en" to "Low", "es" to "Bajo")
        )

        return translations[presupuesto]?.get(toLanguage) ?: presupuesto
    }

    fun translateRangoPrecio(rango: String, toLanguage: String): String {
        val translations = mapOf(
            "Económico" to mapOf("en" to "Budget", "es" to "Económico"),
            "Gama Media" to mapOf("en" to "Mid-range", "es" to "Gama Media"),
            "Lujo" to mapOf("en" to "Luxury", "es" to "Lujo"),

            "Budget" to mapOf("en" to "Budget", "es" to "Económico"),
            "Mid-range" to mapOf("en" to "Mid-range", "es" to "Gama Media"),
            "Luxury" to mapOf("en" to "Luxury", "es" to "Lujo")
        )

        return translations[rango]?.get(toLanguage) ?: rango
    }

    fun translateServicio(servicio: String, toLanguage: String): String {
        val translations = mapOf(
            "Estacionamiento" to mapOf("en" to "Parking", "es" to "Estacionamiento"),
            "Wifi" to mapOf("en" to "Wifi", "es" to "Wifi"),
            "Petfriendly" to mapOf("en" to "Pet-friendly", "es" to "Petfriendly"),
            "Accesible" to mapOf("en" to "Accessible", "es" to "Accesible"),
            "Transporte Incluido" to mapOf("en" to "Transport Included", "es" to "Transporte Incluido"),
            "Tours Incluidos" to mapOf("en" to "Tours Included", "es" to "Tours Incluidos"),

            "Parking" to mapOf("en" to "Parking", "es" to "Estacionamiento"),
            "Pet-friendly" to mapOf("en" to "Pet-friendly", "es" to "Petfriendly"),
            "Accessible" to mapOf("en" to "Accessible", "es" to "Accesible"),
            "Transport Included" to mapOf("en" to "Transport Included", "es" to "Transporte Incluido"),
            "Tours Included" to mapOf("en" to "Tours Included", "es" to "Tours Incluidos")
        )

        return translations[servicio]?.get(toLanguage) ?: servicio
    }

    fun translateMomento(momento: String, toLanguage: String): String {
        val translations = mapOf(
            "Amanecer" to mapOf("en" to "Sunrise", "es" to "Amanecer"),
            "Matutino" to mapOf("en" to "Morning", "es" to "Matutino"),
            "Mediodía" to mapOf("en" to "Noon", "es" to "Mediodía"),
            "Vespertino" to mapOf("en" to "Afternoon", "es" to "Vespertino"),
            "Atardecer" to mapOf("en" to "Sunset", "es" to "Atardecer"),
            "Nocturno" to mapOf("en" to "Night", "es" to "Nocturno"),

            "Sunrise" to mapOf("en" to "Sunrise", "es" to "Amanecer"),
            "Morning" to mapOf("en" to "Morning", "es" to "Matutino"),
            "Noon" to mapOf("en" to "Noon", "es" to "Mediodía"),
            "Afternoon" to mapOf("en" to "Afternoon", "es" to "Vespertino"),
            "Sunset" to mapOf("en" to "Sunset", "es" to "Atardecer"),
            "Night" to mapOf("en" to "Night", "es" to "Nocturno")
        )

        return translations[momento]?.get(toLanguage) ?: momento
    }

    fun normalizeToSpanish(term: String): String {
        // Intentar traducir con cada función
        val tipoTranslated = translateTipo(term, "es")
        if (tipoTranslated != term) return tipoTranslated

        val ambienteTranslated = translateAmbiente(term, "es")
        if (ambienteTranslated != term) return ambienteTranslated

        val presupuestoTranslated = translatePresupuesto(term, "es")
        if (presupuestoTranslated != term) return presupuestoTranslated

        val precioTranslated = translateRangoPrecio(term, "es")
        if (precioTranslated != term) return precioTranslated

        val servicioTranslated = translateServicio(term, "es")
        if (servicioTranslated != term) return servicioTranslated

        val momentoTranslated = translateMomento(term, "es")
        if (momentoTranslated != term) return momentoTranslated

        return term
    }
}