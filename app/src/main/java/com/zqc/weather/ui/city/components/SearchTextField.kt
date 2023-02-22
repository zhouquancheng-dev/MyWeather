package com.zqc.weather.ui.city.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zqc.mdoel.view.noRippleClickable
import com.zqc.weather.R

@Composable
fun SearchContent(
    searchState: SearchState,
    modifier: Modifier = Modifier
) {
    SearchTextField(
        modifier = modifier,
        query = searchState.query,
        onQueryChange = { searchState.query = it },
        onSearchFocusChange = { searchState.focused = it },
        onClearQuery = { searchState.query = "" }
    )
}

@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearchFocusChange: (Boolean) -> Unit,
    onClearQuery: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f),
        shape = RoundedCornerShape(60.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.width(10.dp))
            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (query.isEmpty()) {
                    SearchHint()
                }
                BasicTextField(
                    value = query,
                    onValueChange = { onQueryChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 35.dp)
                        .onFocusChanged {
                            onSearchFocusChange(it.isFocused)
                        },
                    cursorBrush = SolidColor(MaterialTheme.colors.primary),
                    singleLine = true
                )
                if (query.isNotEmpty()) {
                    ClearQuery(
                        onClearQuery = onClearQuery,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchHint() {
    Text(text = stringResource(id = R.string.search_hint_text), color = MaterialTheme.colors.onSurface)
}

@Composable
private fun ClearQuery(
    modifier: Modifier = Modifier,
    onClearQuery: () -> Unit
) {
    Surface(shape = CircleShape,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
        modifier = modifier.noRippleClickable { onClearQuery() }
    ) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "clear")
    }
}