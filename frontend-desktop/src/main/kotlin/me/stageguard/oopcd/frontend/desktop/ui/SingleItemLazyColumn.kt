package me.stageguard.oopcd.frontend.desktop.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
@Suppress("FunctionName")
fun SingleItemLazyColumn(block: @Composable ColumnScope.() -> Unit) {
    LazyColumn {
        item {
            Column {
                block(this)
            }
        }
    }
}