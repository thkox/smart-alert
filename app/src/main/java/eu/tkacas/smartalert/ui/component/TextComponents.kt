package eu.tkacas.smartalert.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.tkacas.smartalert.R
import eu.tkacas.smartalert.ui.theme.BlueGreen
import eu.tkacas.smartalert.ui.theme.PrussianBlue

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = PrussianBlue,
        textAlign = TextAlign.Center
    )
}

@Composable
fun SimpleTextComponent(value: String) {
    Text(
        text = value,
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = PrussianBlue
    )
}


@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ), color = PrussianBlue,
        textAlign = TextAlign.Center
    )
}

@Composable
fun UnderLinedTextComponent(value: String, onClick: () -> Unit) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(onClick = onClick),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.colorGray),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun CenteredCreatorsText() {
    val context = LocalContext.current
    val creators = stringResource(id = R.string.creators, context)

    Text(
        text = creators,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        color = PrussianBlue,
    )
}

@Composable
fun CenteredAboutText() {
    val context = LocalContext.current
    val about = stringResource(id = R.string.about_us_text, context)

    Text(
        text = about,
        style = MaterialTheme.typography.body1,
        textAlign = TextAlign.Center,
        color = BlueGreen,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun REDUnderLinedTextComponent(value: String, onClick: () -> Unit) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clickable(onClick = onClick),
        style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = colorResource(id = R.color.red),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}