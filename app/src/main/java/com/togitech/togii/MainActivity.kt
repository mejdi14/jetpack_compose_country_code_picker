package com.togitech.togii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.TogiRoundedPicker
import com.togitech.ccp.data.utils.checkPhoneNumber
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.togii.ui.theme.TogiiTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TogiiTheme {
                val phoneNumber = rememberSaveable { mutableStateOf("") }
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(
                    color = MaterialTheme.colors.primary,
                    false
                )
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colors.primary,
                    false
                )
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text(text = "Togisoft") }) }) {
                    it.calculateTopPadding()
                    Surface(modifier = Modifier.fillMaxSize()) {
                        SelectCountryWithCountryCode {

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SelectCountryBody() {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {




        }
    }

    @Composable
    fun SelectCountryWithCountryCode(updateValue: (String) -> Unit) {
        val context = LocalContext.current
        val phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode(context)) }
        val defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        val isValidPhone by remember { mutableStateOf(true) }
        val verifyText by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = verifyText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
            TogiCountryCodePicker(
                pickedCountry = {
                    // phoneCode = it.countryPhoneCode
                    // defaultLang = it.countryCode
                },
                defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
                dialogAppBarTextColor = Color.Black,
                dialogAppBarColor = Color.White,
                error = isValidPhone,
                text = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                rowPadding = Modifier.padding(vertical = 0.dp),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                modifier = Modifier
                    .height(50.dp)
                    .padding(0.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    )

            )

            val fullPhoneNumber = "$phoneCode${phoneNumber.value}"
            val checkPhoneNumber = checkPhoneNumber(
                phone = phoneNumber.value,
                fullPhoneNumber = fullPhoneNumber,
                countryCode = defaultLang
            )

            updateValue(fullPhoneNumber)

        }
    }
}


@Composable
private fun RoundedPicker() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode(context)) }
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
        var isValidPhone by remember { mutableStateOf(true) }
        val fullPhoneNumber = "$phoneCode${phoneNumber.value}"

        val checkPhoneNumber = checkPhoneNumber(
            phone = phoneNumber.value,
            fullPhoneNumber = fullPhoneNumber,
            countryCode = defaultLang
        )

        TogiRoundedPicker(
            value = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
            pickedCountry = {
                phoneCode = it.countryPhoneCode
                defaultLang = it.countryCode.ifBlank { "tr" }
            },
            error = isValidPhone
        )

        OutlinedButton(
            onClick = { isValidPhone = checkPhoneNumber }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .height(
                    50.dp
                )
        ) {
            Text(text = "Verify Phone Number")
        }
    }
}
