package com.kriscg.belek.data.repository

import com.kriscg.belek.domain.Event
import com.kriscg.belek.data.SupabaseConfig
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import java.time.LocalDate

class EventRepository {
    private val supabase get() = SupabaseConfig.client
    suspend fun getAllEvents(): Result<List<Event>> = try {
        val events = supabase.from("events")
            .select()
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEventsByMonth(year: Int, month: Int): Result<List<Event>> = try {
        val startDate = LocalDate.of(year, month, 1)
        val endDate = startDate.plusMonths(1).minusDays(1)

        val events = supabase.from("events")
            .select {
                filter {
                    gte("event_date", startDate.toString())
                    lte("event_date", endDate.toString())
                }
                order("event_date", Order.ASCENDING)
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEventsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Result<List<Event>> = try {
        val events = supabase.from("events")
            .select {
                filter {
                    gte("event_date", startDate.toString())
                    lte("event_date", endDate.toString())
                }
                order("event_date", Order.ASCENDING)
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEventById(id: String): Result<Event> = try {
        val event = supabase.from("events")
            .select {
                filter {
                    eq("id", id)
                }
            }
            .decodeSingle<Event>()
        Result.success(event)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEventsByCity(city: String): Result<List<Event>> = try {
        val events = supabase.from("events")
            .select {
                filter {
                    eq("city", city)
                }
                order("event_date", Order.ASCENDING)
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getEventsByCategory(category: String): Result<List<Event>> = try {
        val events = supabase.from("events")
            .select {
                filter {
                    eq("category", category)
                }
                order("event_date", Order.ASCENDING)
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun searchEvents(query: String): Result<List<Event>> = try {
        val events = supabase.from("events")
            .select {
                filter {
                    or {
                        ilike("title", "%$query%")
                        ilike("description", "%$query%")
                        ilike("city", "%$query%")
                    }
                }
                order("event_date", Order.ASCENDING)
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUpcomingEvents(limit: Int = 10): Result<List<Event>> = try {
        val today = LocalDate.now().toString()

        val events = supabase.from("events")
            .select {
                filter {
                    gte("event_date", today)
                }
                order("event_date", Order.ASCENDING)
                limit(limit.toLong())
            }
            .decodeList<Event>()
        Result.success(events)
    } catch (e: Exception) {
        Result.failure(e)
    }
}