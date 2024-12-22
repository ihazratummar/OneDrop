package com.hazrat.onedrop.util.encrytion

import java.io.InputStream
import java.io.OutputStream

/**
 * @author Hazrat Ummar Shaikh
 * Created on 08-12-2024
 */

interface EncryptionUtil {

    fun encrypt(bytes: ByteArray ): ByteArray

    fun decrypt(inputStream: InputStream): ByteArray

}