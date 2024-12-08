package com.hazrat.onedrop.util

import android.util.Base64

/**
 * @author Hazrat Ummar Shaikh
 * Created on 08-12-2024
 */

// Function to convert ByteArray to Base64 String
fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

// Function to convert Base64 String back to ByteArray
fun String.fromBase64(): ByteArray {
    return Base64.decode(this, Base64.NO_WRAP)
}