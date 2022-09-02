package com.togitech.ccp.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getNumberHint
import com.togitech.ccp.transformation.PhoneNumberTransformation

@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    dialogAppBarColor: Color = MaterialTheme.colors.primary,
    dialogAppBarTextColor: Color = Color.White,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = text)) }
    val textFieldValue = textFieldValueState.copy(text = text)
    val keyboardController = LocalTextInputService.current
    Surface(
        color = color
    ) {
        Column(
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                OutlinedTextField(
                    modifier = Modifier.then(Modifier.height(55.dp).clip(shape = RoundedCornerShape(10.dp)).background(color = Color(0xFFF2F2F7)))
                        .fillMaxSize().padding(0.dp),
                    shape = RoundedCornerShape(10.dp),
                    value = textFieldValue,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = if (!error) Color.Red else focusedBorderColor,
                        unfocusedBorderColor = if (!error) Color.Red else unfocusedBorderColor,
                        cursorColor = cursorColor
                    ),
                    onValueChange = {
                        textFieldValueState = it
                        if (text != it.text) {
                            onValueChange(it.text)
                        }
                    },
                    singleLine = true,
                    visualTransformation = PhoneNumberTransformation(defaultCountry.countryCode.uppercase()),
                    placeholder = { Text(text = stringResource(id = R.string.phone_number, modifier.fillMaxHeight().align(Alignment.CenterVertically))) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.NumberPassword,
                        autoCorrect = true,
                    ),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hideSoftwareKeyboard() }),
                    leadingIcon = {
                        Row(modifier = Modifier.fillMaxHeight()) {
                            Column(modifier = Modifier.fillMaxHeight()) {
                                val dialog = TogiCodePicker()
                                dialog.TogiCodeDialog(
                                    pickedCountry = pickedCountry,
                                    defaultSelectedCountry = defaultCountry,
                                    dialogAppBarColor = dialogAppBarColor,
                                    showCountryCode = showCountryCode,
                                    dialogAppBarTextColor = dialogAppBarTextColor
                                )
                            }

                        }
                    },
                    trailingIcon = {
                        if (!error)
                            Icon(
                                imageVector = Icons.Filled.Warning, contentDescription = "Error",
                                tint = MaterialTheme.colors.error
                            )
                    }
                )
            }
            if (!error)
                Text(
                    text = stringResource(id = R.string.invalid_number),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 0.8.dp)
                )
        }
    }
}