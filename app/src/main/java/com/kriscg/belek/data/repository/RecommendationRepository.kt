package com.kriscg.belek.data.repository

import com.kriscg.belek.domain.EventPlace
import com.kriscg.belek.domain.Lugar
import com.kriscg.belek.domain.PlaceRecommendation
import com.kriscg.belek.data.SupabaseConfig
import io.github.jan.supabase.postgrest.from

class RecommendationRepository {
    private val supabase get() = SupabaseConfig.client

    suspend fun getRecommendationsByEventId(eventId: String): Result<List<PlaceRecommendation>> {
        return try {
            val eventPlaces = supabase.from("event_places")
                .select {
                    filter {
                        eq("event_id", eventId)
                    }
                }
                .decodeList<EventPlace>()

            if (eventPlaces.isEmpty()) {
                return Result.success(emptyList())
            }

            val placeIds = eventPlaces.map { it.placeId }

            val lugares = supabase.from("lugares")
                .select {
                    filter {
                        isIn("id", placeIds)
                    }
                }
                .decodeList<Lugar>()

            val recommendations = eventPlaces.mapNotNull { eventPlace ->
                val lugar = lugares.find { it.id == eventPlace.placeId }
                lugar?.let { PlaceRecommendation(eventPlace, it) }
            }

            Result.success(recommendations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendationsByType(
        eventId: String,
        type: String
    ): Result<List<PlaceRecommendation>> {
        return try {
            val eventPlaces = supabase.from("event_places")
                .select {
                    filter {
                        eq("event_id", eventId)
                        eq("recommendation_type", type)
                    }
                }
                .decodeList<EventPlace>()

            if (eventPlaces.isEmpty()) {
                return Result.success(emptyList())
            }

            val placeIds = eventPlaces.map { it.placeId }

            val lugares = supabase.from("lugares")
                .select {
                    filter {
                        isIn("id", placeIds)
                    }
                }
                .decodeList<Lugar>()

            val recommendations = eventPlaces.mapNotNull { eventPlace ->
                val lugar = lugares.find { it.id == eventPlace.placeId }
                lugar?.let { PlaceRecommendation(eventPlace, it) }
            }

            Result.success(recommendations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRecommendationsGroupedByType(
        eventId: String
    ): Result<Map<String, List<PlaceRecommendation>>> = try {
        val allRecommendations = getRecommendationsByEventId(eventId)
            .getOrNull() ?: emptyList()

        val grouped = allRecommendations.groupBy { it.type }
        Result.success(grouped)
    } catch (e: Exception) {
        Result.failure(e)
    }
}