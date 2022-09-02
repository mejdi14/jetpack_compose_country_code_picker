package com.togitech.ccp.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.togitech.ccp.R
import com.togitech.ccp.data.CountryData
import com.togitech.ccp.data.utils.getCountryName
import com.togitech.ccp.data.utils.getFlags
import com.togitech.ccp.data.utils.getLibCountries
import com.togitech.ccp.utils.searchCountry

class TogiCodePicker {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun TogiCodeDialog(
        modifier: Modifier = Modifier,
        padding: Dp = 15.dp,
        defaultSelectedCountry: CountryData = getLibCountries().first(),
        showCountryCode: Boolean = true,
        pickedCountry: (CountryData) -> Unit,
        dialogAppBarColor: Color = MaterialTheme.colors.primary,
        dialogAppBarTextColor: Color = Color.White,
    ) {
        val countryList: List<CountryData> = getLibCountries()
        var isPickCountry by remember { mutableStateOf(defaultSelectedCountry) }
        var isOpenDialog by remember { mutableStateOf(false) }
        var searchValue by remember { mutableStateOf("") }
        var isSearch by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val interactionSource = remember { MutableInteractionSource() }

        Column(
            modifier = Modifier
                .fillMaxHeight()

                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { isOpenDialog = true },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Color(0xFFE4E4EA)),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(14.dp))
                    Image(
                        modifier = modifier.width(34.dp),
                        painter = painterResource(
                            id = getFlags(
                                isPickCountry.countryCode
                            )
                        ), contentDescription = null
                    )
                    Box(modifier = Modifier.width(22.dp))
                    Icon(painterResource(R.drawable.arrow_down), contentDescription = null)
                    Box(modifier = Modifier.width(12.dp))
                }

                if (showCountryCode) {
                    Text(
                        text = isPickCountry.countryPhoneCode,
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp,
                        color = Color(0x99000000),
                        modifier = Modifier.padding(start = 6.dp, end = 2.dp)
                    )

                }
                Box(modifier = Modifier.width(1.dp))
                Divider(

                    color = Color(0xFF616163),
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )
                Box(modifier = Modifier.width(12.dp))
            }
        }

        //Select Country Dialog
        if (isOpenDialog) {
            Dialog(
                onDismissRequest = { isOpenDialog = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
            ) {
                Scaffold(
                    topBar = {
                        Column() {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(modifier = Modifier.weight(1.0f), onClick = {
                                    isOpenDialog = false
                                    searchValue = ""
                                    isSearch = false
                                }) {
                                    Icon(
                                        painterResource(id = R.drawable.back_arrow),
                                        contentDescription = "Back",
                                    )
                                }

                                Text(
                                    text = stringResource(id = R.string.choose_country),
                                    textAlign = TextAlign.Center,
                                    modifier = modifier
                                        .weight(5.0f)
                                        .fillMaxWidth(),
                                    fontWeight = FontWeight.W400,
                                    fontSize = 17.sp,
                                    color = Color.Black

                                )
                                Box(modifier = Modifier.weight(1.0f))

                            }
                            TextField(
                                value = searchValue,
                                onValueChange = {
                                    searchValue = it
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(22.dp),

                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        contentDescription = "Search",
                                        tint = Color(0xffA2A3B0),
                                        // tint = if (isSelected) activeTextColor else inactiveTextColor,
                                        modifier = Modifier.size(20.dp)
                                    )

                                },
                                placeholder = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "Search",
                                        color = Color(0xffA2A3B0),
                                        textAlign = TextAlign.Left,
                                        fontWeight = FontWeight.W400,
                                        fontSize = 17.sp
                                    )
                                },

                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFFF2F2F7),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )

                        }
                    }
                ) {
                    it.calculateTopPadding()
                    Surface(modifier = modifier.fillMaxSize()) {
                        var value by remember { mutableStateOf(TextFieldValue("")) }
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            elevation = 4.dp,
                        ) {
                            Column {

                                LazyColumn {
                                    itemsIndexed(
                                        (if (searchValue.isEmpty()) {
                                            countryList
                                        } else {
                                            countryList.searchCountry(
                                                searchValue,
                                                context = context
                                            )
                                        })
                                    ) { index, countryItem ->
                                        Row(
                                            Modifier
                                                .padding(
                                                    horizontal = 18.dp,
                                                    vertical = 18.dp
                                                )
                                                .fillMaxWidth()
                                                .clickable {
                                                    pickedCountry(countryItem)
                                                    isPickCountry = countryItem
                                                    isOpenDialog = false
                                                    searchValue = ""
                                                    isSearch = false
                                                },
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Image(
                                                modifier = modifier.width(30.dp).weight(1.0f),
                                                painter = painterResource(
                                                    id = getFlags(
                                                        countryItem.countryCode
                                                    )
                                                ), contentDescription = null
                                            )
                                            Text(
                                                stringResource(id = getCountryName(countryItem.countryCode.lowercase())),
                                                Modifier.padding(horizontal = 18.dp).weight(8.0f),
                                                color = Color.Black
                                            )
                                            Text(countryItem.countryPhoneCode, modifier = Modifier.weight(1.0f),
                                            textAlign = TextAlign.End)
                                        }
                                        if (index < countryList.lastIndex)
                                            Divider(
                                                color = Color(0x33000000),
                                                thickness = 1.dp,
                                                modifier = Modifier.padding(horizontal = 16.dp)
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun SearchTextField(
        modifier: Modifier = Modifier,
        leadingIcon: (@Composable () -> Unit)? = null,
        trailingIcon: (@Composable () -> Unit)? = null,
        value: String,
        textColor: Color = Color.Black,
        onValueChange: (String) -> Unit,
        hint: String = stringResource(id = R.string.search),
        fontSize: TextUnit = MaterialTheme.typography.body2.fontSize
    ) {
        BasicTextField(modifier = modifier
            .background(
                MaterialTheme.colors.surface,
                MaterialTheme.shapes.small,
            )
            .fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colors.primary),
            textStyle = LocalTextStyle.current.copy(
                color = textColor,
                fontSize = fontSize
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (leadingIcon != null) leadingIcon()
                    Box(Modifier.weight(1f)) {
                        if (value.isEmpty()) Text(
                            hint,
                            style = LocalTextStyle.current.copy(
                                color = textColor,
                                fontSize = fontSize
                            )
                        )
                        innerTextField()
                    }
                    if (trailingIcon != null) trailingIcon()
                }
            }
        )
    }
}