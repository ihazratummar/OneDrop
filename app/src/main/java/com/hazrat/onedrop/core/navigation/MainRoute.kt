package com.hazrat.onedrop.core.navigation


import com.hazrat.onedrop.R
import kotlinx.serialization.Serializable


/**
 * @author Hazrat Ummar Shaikh
 */


@Serializable
sealed class Route {

    @Serializable
    data object HomeRoute : Route()

    @Serializable
    data object SearchRoute : Route()

    @Serializable
    data object MoreRoute : Route()

    @Serializable
    data object BloodDonorRoute : Route()


    @Serializable
    data object RequestBloodRoute : Route()


}


sealed class BottomNavigation<T>(val icon: Int, val fillIcon: Int, val name: String, val route: T) {
    data object HomeNav : BottomNavigation<Route.HomeRoute>(
        icon = R.drawable.home,
        fillIcon = R.drawable.homefill,
        name = "Home",
        route = Route.HomeRoute
    )

    data object SearchNav : BottomNavigation<Route.SearchRoute>(
        icon = R.drawable.outline_search,
        fillIcon = R.drawable.search_filled,
        name = "Search",
        route = Route.SearchRoute
    )

    data object MoreNav : BottomNavigation<Route.MoreRoute>(
        icon = R.drawable.more_outline,
        fillIcon = R.drawable.more_filled,
        name = "More",
        route = Route.MoreRoute
    )
}