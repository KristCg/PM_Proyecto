package com.kriscg.belek.ui.navigation.calendar

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.kriscg.belek.ui.screens.calendar.CalendarScreen
import com.kriscg.belek.ui.screens.calendar.EventDetailScreen
import kotlinx.serialization.Serializable

@Serializable
object Calendar

@Serializable
data class EventDetail(val eventId: String)

fun NavGraphBuilder.calendarNavigation(
    onNavigateToEventDetail: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Calendar> {
        CalendarScreen(
            onEventClick = { event ->
                onNavigateToEventDetail(event.id)
            },
            onBackClick = onNavigateBack
        )
    }
    composable<EventDetail> { backStackEntry ->
        val route = backStackEntry.arguments?.getString("eventId") ?: ""

        EventDetailScreen(
            eventId = route,
            onBackClick = onNavigateBack
        )
    }
}