package eu.tkacas.smartalert.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.tkacas.smartalert.app.SharedPrefManager
import eu.tkacas.smartalert.database.cloud.FirebaseUtils
import eu.tkacas.smartalert.ui.screen.Screen
import eu.tkacas.smartalert.ui.screen.auth.ForgotPasswordScreen
import eu.tkacas.smartalert.ui.screen.auth.LoginScreen
import eu.tkacas.smartalert.ui.screen.auth.PrivacyPolicyScreen
import eu.tkacas.smartalert.ui.screen.auth.SignUpScreen
import eu.tkacas.smartalert.ui.screen.auth.TermsAndConditionsScreen
import eu.tkacas.smartalert.ui.screen.citizen.AlertFormScreen
import eu.tkacas.smartalert.ui.screen.citizen.CameraScreen
import eu.tkacas.smartalert.ui.screen.citizen.HomeCitizenScreen
import eu.tkacas.smartalert.ui.screen.employee.AlertCitizensFormScreen
import eu.tkacas.smartalert.ui.screen.employee.EventsByLocationScreen
import eu.tkacas.smartalert.ui.screen.employee.GroupEventsByLocationScreen
import eu.tkacas.smartalert.ui.screen.employee.HomeEmployeeScreen
import eu.tkacas.smartalert.ui.screen.employee.MapWithPinnedReportsScreen
import eu.tkacas.smartalert.ui.screen.intro.PermissionsScreen
import eu.tkacas.smartalert.ui.screen.intro.WelcomeScreen
import eu.tkacas.smartalert.ui.screen.screensInHomeCitizen
import eu.tkacas.smartalert.ui.screen.screensInHomeEmployee
import eu.tkacas.smartalert.ui.screen.screensInSettings
import eu.tkacas.smartalert.ui.screen.settings.AboutScreen
import eu.tkacas.smartalert.ui.screen.settings.AccountScreen
import eu.tkacas.smartalert.ui.screen.settings.AnalyticsScreen
import eu.tkacas.smartalert.ui.screen.settings.LanguageScreen
import eu.tkacas.smartalert.ui.screen.settings.MyReportsHistoryScreen
import eu.tkacas.smartalert.ui.screen.settings.SettingsScreen
import eu.tkacas.smartalert.viewmodel.navigation.NavigationViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Navigation(navController: NavController = rememberNavController()) {
    val sharedPrefManager = SharedPrefManager(LocalContext.current)
    val context = LocalContext.current
    val viewModel = NavigationViewModel(context)
    val firebase = FirebaseUtils()


    NavHost(
        navController = navController as NavHostController,
        startDestination = viewModel.findStartDestination()
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("permissions") {
            viewModel.setUserIdentity()
            if (viewModel.permissionsAreGranted()) navController.navigate("home")
            else PermissionsScreen(navController)
        }
        composable("privacyPolicy") { PrivacyPolicyScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("signUp") { SignUpScreen(navController) }
        composable("termsAndConditions") { TermsAndConditionsScreen(navController) }
        composable("forgotPassword") { ForgotPasswordScreen(navController) }
        composable("alertCitizensForm") { AlertCitizensFormScreen(navController) }
        composable("alertForm") { AlertFormScreen(navController) }
        composable("camera") { CameraScreen(navController) }
        composable("home") {
            when {
                !viewModel.permissionsAreGranted() -> navController.navigate("permissions")
                !firebase.userExists() -> navController.navigate("welcome")
                sharedPrefManager.isEmployee() -> {
                    sharedPrefManager.setLocationName("")
                    sharedPrefManager.setLocationID("")
                    sharedPrefManager.setBoundsNorthEastLat(0.0)
                    sharedPrefManager.setBoundsNorthEastLng(0.0)
                    sharedPrefManager.setBoundsSouthWestLat(0.0)
                    sharedPrefManager.setBoundsSouthWestLng(0.0)
                    HomeEmployeeScreen(navController = navController)
                }

                else -> HomeCitizenScreen(navController)
            }
        }
        composable("settings") { SettingsScreen(navController) }

        screensInHomeCitizen.forEach { screen ->
            composable(screen.route) {
                if (firebase.userExists()) {
                    when (screen) {
                        is Screen.HomeCitizen.AlertForm -> AlertFormScreen(navController)
                    }
                } else {
                    navController.navigate("welcome")
                }
            }
        }

        screensInHomeEmployee.forEach { screen ->
            composable(screen.route) {
                if (firebase.userExists() && sharedPrefManager.isEmployee()) {
                    when (screen) {
                        is Screen.HomeEmployee.AlertCitizenForm -> AlertCitizensFormScreen(
                            navController
                        )

                        is Screen.HomeEmployee.GroupEventsByLocation -> GroupEventsByLocationScreen(
                            navController
                        )

                        is Screen.HomeEmployee.EventsByLocation -> EventsByLocationScreen(
                            navController
                        )

                        is Screen.HomeEmployee.MapWithPinnedReports -> MapWithPinnedReportsScreen(
                            navController
                        )
                    }
                } else {
                    navController.navigate("welcome")
                }
            }
        }

        screensInSettings.forEach { screen ->
            composable(screen.route) {
                when (screen) {
                    is Screen.SettingsScreen.Account -> AccountScreen(navController)
                    is Screen.SettingsScreen.MyReportsHistory -> MyReportsHistoryScreen(
                        navController
                    )

                    is Screen.SettingsScreen.Language -> LanguageScreen(navController)
                    is Screen.SettingsScreen.Analytics -> AnalyticsScreen(navController)
                    is Screen.SettingsScreen.About -> AboutScreen(navController)
                    is Screen.SettingsScreen.Logout -> {
                        sharedPrefManager.setFirstName("")
                        firebase.signOutUser()
                        sharedPrefManager.removeIsEmployee()
                        navController.navigate("welcome")
                    }
                }
            }
        }
    }
}