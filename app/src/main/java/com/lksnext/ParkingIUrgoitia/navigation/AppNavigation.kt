package com.lksnext.ParkingIUrgoitia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.lksnext.ParkingIUrgoitia.screens.HomeScreen
import com.lksnext.ParkingIUrgoitia.screens.ReservationScreen
import com.lksnext.ParkingIUrgoitia.screens.AddCarScreen
import com.lksnext.ParkingIUrgoitia.screens.EditReservationScreen
import com.lksnext.ParkingIUrgoitia.screens.ViewReservationsScreen
import com.lksnext.ParkingIUrgoitia.screens.DeleteReservationScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {


        composable("home") {
            HomeScreen(
                onMakeReservationClick = { navController.navigate("make_reservation") },
                onAddVehicleClick = { navController.navigate("add_vehicle") },
                onViewReservationsClick = { navController.navigate("view_reservations") },
                onEditReservationClick = { navController.navigate("edit_reservations") },
                onDeleteReservationClick = { navController.navigate("delete_reservations") }
            )
        }

        composable("make_reservation") {
            ReservationScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable("view_reservations") {

            ViewReservationsScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
        composable("add_vehicle") {
            AddCarScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable("edit_reservations") {
            EditReservationScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }

        composable("delete_reservations") {
            DeleteReservationScreen(
                onNavigateHome = {
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }
    }
}