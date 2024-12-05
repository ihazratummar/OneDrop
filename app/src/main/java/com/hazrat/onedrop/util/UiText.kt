package com.hazrat.onedrop.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * @author Hazrat Ummar Shaikh
 * Created on 02-12-2024
 */

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    class  StringResource(
        @StringRes val id: Int,
        val args: Array<Any> = arrayOf()
    ): UiText()

    @Composable
    fun asString(): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> LocalContext.current.getString(id, *args)
        }
    }

    fun asString(context: Context): String{
        return when(this){
            is DynamicString -> value
            is StringResource -> context.getString(id, *args)
        }
    }
}