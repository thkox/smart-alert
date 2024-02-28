package eu.tkacas.smartalert.ui.screen.employee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import eu.tkacas.smartalert.R
import eu.tkacas.smartalert.models.CriticalWeatherPhenomenon
import eu.tkacas.smartalert.ui.component.AlertLevelButtonsRowComponent
import eu.tkacas.smartalert.ui.component.CityTextFieldComponent
import eu.tkacas.smartalert.ui.component.EnumDropdownComponent
import eu.tkacas.smartalert.ui.component.GeneralButtonComponent
import eu.tkacas.smartalert.ui.component.NormalTextComponent
import eu.tkacas.smartalert.ui.component.UploadButtonComponent
import eu.tkacas.smartalert.ui.navigation.AppBarBackView

@Composable
fun AlertCitizensFormScreen(navController: NavHostController? = null){
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBarBackView(title = stringResource(id = R.string.alert_form), navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(modifier = Modifier.size(80.dp))
                NormalTextComponent(value = stringResource(id = R.string.city_of_emergency))
                CityTextFieldComponent(labelValue = stringResource(id = R.string.city))
                Spacer(modifier = Modifier.size(16.dp))
                NormalTextComponent(value = stringResource(id = R.string.weather_phenomenon_selection))
                EnumDropdownComponent(
                    CriticalWeatherPhenomenon::class.java,
                    initialSelection = CriticalWeatherPhenomenon.EARTHQUAKE,
                    onSelected = {

                    })
                Spacer(modifier = Modifier.size(16.dp))
                NormalTextComponent(value = stringResource(id = R.string.emergency_level))
                AlertLevelButtonsRowComponent(
                    onButtonClicked = {

                    }
                )
                Spacer(modifier = Modifier.size(80.dp))
                UploadButtonComponent(
                    value = stringResource(id = R.string.submit),
                    onButtonClicked = {

                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun AlertCitizensFormScreenPreview(){
    AlertCitizensFormScreen()
}

