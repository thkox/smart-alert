package eu.tkacas.smartalert.ui.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import eu.tkacas.smartalert.R
import eu.tkacas.smartalert.app.SharedPrefManager
import eu.tkacas.smartalert.models.CriticalWeatherPhenomenon
import eu.tkacas.smartalert.ui.screen.Screen


@Preview
@Composable
fun CardComponentWithImage(
    row1: String = "Kifissia, Athens",
    row2: String = "Emergency level",
    row3: String = "",
    beDeletedEnabled: Boolean = false,
    onClick: () -> Unit = {}
) {
    val sharedPrefManager = SharedPrefManager(LocalContext.current)
    val weatherPhenomenon = sharedPrefManager.getCriticalWeatherPhenomenon()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
            onClick()
        },
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp,
        ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp)
        ){
            Column(
                modifier = Modifier.padding(start = 8.dp),
                verticalArrangement = Arrangement.Center
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(id = weatherPhenomenon.getImage()),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(horizontal = 5.dp)
                    )
                    Column(){
                        Text(text = row1)
                        Text(text = row2)
                        if (row3.isNotEmpty()) {
                            Text(text = row3)
                        }
                    }
                }
            }
            if(beDeletedEnabled){
                Column(
                    modifier = Modifier.padding(end = 8.dp),
                    verticalArrangement = Arrangement.Center
                ){
                    Icon(
                        painter = painterResource(id = R.drawable.delete),
                        contentDescription = "Delete Icon",
                        modifier = Modifier.clickable {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CriticalWeatherPhenomenonCardComponent(navController : NavController? = null, weatherPhenomenon: CriticalWeatherPhenomenon) {
    val imageResId = weatherPhenomenon.getImage()
    val sharedPrefManager = SharedPrefManager(LocalContext.current)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp
    ) {
        Button(
            onClick = {
                sharedPrefManager.setCriticalWeatherPhenomenon(weatherPhenomenon.name)
                navController?.navigate("GroupEventsByLocation")
            },
            modifier = Modifier.fillMaxSize(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = "Button Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 8.dp) // Make the image fill the button
                )
                Text(
                    text = weatherPhenomenon.name,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun SettingCard(screen: Screen.SettingsScreen, onClick: () -> Unit) {
    val color = if(screen.titleResId == R.string.logout) Color.Red else Color.Black

    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(6.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = screen.icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                    tint = color
                )
                Text(
                    text = stringResource(id = screen.titleResId),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 10.dp),
                    color = color
                )
            }
            if(screen.titleResId != R.string.logout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Icon"
                )
            }
        }
    }
}

@Composable
fun PermissionCard(
    iconResId: Int,
    permissionName: String,
    isExpanded: MutableState<Boolean>,
    switchState: MutableState<Boolean>,
    onToggleClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.padding(8.dp), elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row (
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            painter = painterResource(id = iconResId),
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(40.dp),
                            contentDescription = null)
                        Text(
                            text = permissionName,
                            modifier = Modifier.padding(start = 8.dp),
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Switch(
                        checked = switchState.value,
                        onCheckedChange = {
                            if (it) {
                                switchState.value = it
                                onToggleClick()
                            } else {
                                switchState.value = false
                            }
                        },
                        enabled = !switchState.value
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                IconButton(onClick = { isExpanded.value = !isExpanded.value }){
                    Icon(
                        painter = if (isExpanded.value) painterResource(id = R.drawable.arrow_up) else painterResource(id = R.drawable.arrow_down),
                        modifier = Modifier
                            .padding(horizontal = 5.dp),
                        contentDescription = null
                    )
                }
            }
            if (isExpanded.value) {
                Row {
                    Text(
                        text = "Allow SmartAlert to access this device's $permissionName?",
                        modifier = Modifier.padding(start = 8.dp),
                        style = TextStyle(fontSize = 14.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageCard(
    onClick: () -> Unit,
    @DrawableRes imageResId: Int,
    @StringRes textResId: Int
) {

    Card(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .padding(6.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp),
                )
                Text(
                    text = stringResource(id = textResId),
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 10.dp),
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun ClickableElevatedCardSample() {
    ElevatedCard(
        //onClick = { /* Do something */ },
        modifier = Modifier.size(width = 150.dp, height = 500.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            Text("Clickable", Modifier.align(Alignment.Center))
        }
    }
}
