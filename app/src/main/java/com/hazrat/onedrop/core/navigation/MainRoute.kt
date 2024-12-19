package com.hazrat.onedrop.core.navigation


import com.hazrat.onedrop.R
import kotlinx.serialization.Serializable


/**
 * @author Hazrat Ummar Shaikh
 */


@Serializable
sealed class MainRoute {

    @Serializable
    data object HomeRoute : MainRoute()

    @Serializable
    data object SearchRoute : MainRoute()

    @Serializable
    data object MoreRoute : MainRoute()

    @Serializable
    data object BloodDonorRoute : MainRoute()


    @Serializable
    data object CreateBloodDonorProfileRoute : MainRoute()


    @Serializable
    data object RequestBloodScreenRoute : MainRoute()

    @Serializable
    data object CreateBloodRequestRoute : MainRoute()

    @Serializable
    data class BloodDonorsProfileScreenRoute(val userId: String) : MainRoute()

    @Serializable
    data object SelfProfileScreenRoute : MainRoute()

    @Serializable
    data object EditProfileScreenRoute : MainRoute()

}


sealed class BottomNavigation<T>(val icon: Int, val fillIcon: Int, val name: String, val route: T) {
    data object HomeNav : BottomNavigation<MainRoute.HomeRoute>(
        icon = R.drawable.home,
        fillIcon = R.drawable.homefill,
        name = "Home",
        route = MainRoute.HomeRoute
    )

    data object RequestBloodNav : BottomNavigation<MainRoute.RequestBloodScreenRoute>(
        icon = R.drawable.request_blood_outline,
        fillIcon = R.drawable.request_blood,
        name = "Request Blood",
        route = MainRoute.RequestBloodScreenRoute
    )

    data object MoreNav : BottomNavigation<MainRoute.MoreRoute>(
        icon = R.drawable.more_outline,
        fillIcon = R.drawable.more_filled,
        name = "More",
        route = MainRoute.MoreRoute
    )
}