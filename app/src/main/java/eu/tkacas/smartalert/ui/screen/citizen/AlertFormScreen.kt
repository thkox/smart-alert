package eu.tkacas.smartalert.ui.screen.citizen

import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import eu.tkacas.smartalert.R
import eu.tkacas.smartalert.models.CitizenMessage
import eu.tkacas.smartalert.models.CriticalWeatherPhenomenon
import eu.tkacas.smartalert.permissions.openAppSettings
import eu.tkacas.smartalert.ui.component.CameraButton
import eu.tkacas.smartalert.ui.component.CameraPermissionTextProvider
import eu.tkacas.smartalert.ui.component.CriticalLevelButtonsRowComponent
import eu.tkacas.smartalert.ui.component.EnumDropdownComponent
import eu.tkacas.smartalert.ui.component.MultilineTextFieldComponent
import eu.tkacas.smartalert.ui.component.NormalTextComponent
import eu.tkacas.smartalert.ui.component.PermissionDialog
import eu.tkacas.smartalert.ui.component.UploadButtonComponent
import eu.tkacas.smartalert.ui.navigation.AppBarBackView
import eu.tkacas.smartalert.viewmodel.citizen.AlertFormViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun AlertFormScreen(navController: NavHostController? = null) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    val permissionsToRequest = arrayOf(
        Manifest.permission.CAMERA
    )
    val viewModel = AlertFormViewModel(context)

    val scope = rememberCoroutineScope()


    val dialogQueue = viewModel.visiblePermissionDialogQueue
    val locationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.CAMERA,
                isGranted = isGranted
            )
        }
    )
    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { perms ->
            permissionsToRequest.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = perms[permission] == true
                )
            }
        }
    )
    val citizenMessagePref = viewModel.getCitizenMessageFromPrefs(context)
    if (citizenMessagePref != null) {
        viewModel.setAlertDescription(citizenMessagePref.message)
        viewModel.setSelectedWeatherPhenomenon(citizenMessagePref.criticalWeatherPhenomenon)
        viewModel.setPhotoURL(citizenMessagePref.imageURL)
        viewModel.setSelectedDangerLevelButton(citizenMessagePref.criticalLevel)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarBackView(
                title = stringResource(id = R.string.create_a_new_alert),
                navController = navController,
                enableSettingsButton = false
            )
        }
    ) { it ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.size(80.dp))
                        NormalTextComponent(value = stringResource(id = R.string.emergency_level))
                        CriticalLevelButtonsRowComponent(
                            initialValue = viewModel.selectedDangerLevelButton.value,
                            onButtonClicked = {
                                viewModel.setSelectedDangerLevelButton(it)
                            }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        NormalTextComponent(value = stringResource(id = R.string.weather_phenomenon_selection))
                        EnumDropdownComponent(
                            enumClass = CriticalWeatherPhenomenon::class.java,
                            initialSelection = viewModel.selectedWeatherPhenomenon.value,
                            onSelected = {
                                viewModel.setSelectedWeatherPhenomenon(it)
                            }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        NormalTextComponent(value = stringResource(id = R.string.writeADescription))
                        MultilineTextFieldComponent(
                            value = viewModel.alertDescription.value,
                            onTextChanged = {
                                viewModel.setAlertDescription(it)
                            }
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                        NormalTextComponent(value = stringResource(id = R.string.takeAPicture))
                        Spacer(modifier = Modifier.size(8.dp))
                        CameraButton(
                            onButtonClicked = {
                                locationPermissionResultLauncher.launch(
                                    Manifest.permission.CAMERA
                                )
                                val citizenMessage = CitizenMessage(
                                    message = viewModel.alertDescription.value,
                                    criticalWeatherPhenomenon = viewModel.selectedWeatherPhenomenon.value,
                                    location = viewModel.locationData,
                                    criticalLevel = viewModel.selectedDangerLevelButton.value,
                                    imageURL = viewModel.photoURL.value
                                )
                                viewModel.saveCitizenMessageToPrefs(context, citizenMessage)
                                navController?.navigate("camera")
                            }
                        )
                        Spacer(modifier = Modifier.size(25.dp))
                        UploadButtonComponent(
                            value = stringResource(id = R.string.submit),
                            onButtonClicked = {
                                scope.launch {
                                    viewModel.sentAlert()
                                    viewModel.clearCitizenMessageFromPrefs(context)
                                    navController?.navigate("home")
                                    val currentLanguage = Locale.getDefault().language
                                    val toastSentMessage = when (currentLanguage) {
                                        "en" -> "Alert sent successfully"
                                        "el" -> "Το συμβάν στάλθηκε επιτυχώς"
                                        else -> "Alert sent successfully"
                                    }
                                    Toast.makeText(context, toastSentMessage, Toast.LENGTH_SHORT)
                                        .show()
                                }

                            }
                        )
                    }

                    dialogQueue
                        .reversed()
                        .forEach { permission ->
                            PermissionDialog(
                                permissionTextProvider = when (permission) {
                                    Manifest.permission.CAMERA -> {
                                        CameraPermissionTextProvider()
                                    }

                                    else -> return@forEach
                                },
                                isPermanentlyDeclined = !ActivityCompat.shouldShowRequestPermissionRationale(
                                    context as Activity,
                                    permission
                                ),
                                onDismiss = {
                                    viewModel.dismissDialog()
                                },
                                onOkClick = {
                                    viewModel.dismissDialog()
                                    multiplePermissionResultLauncher.launch(
                                        arrayOf(permission)
                                    )
                                },
                                onGoToAppSettingsClick = { openAppSettings(context) }
                            )
                        }
                }
            }
        }
    }
}


@Preview
@Composable
fun AlertFormScreenPreview() {
    AlertFormScreen()
}