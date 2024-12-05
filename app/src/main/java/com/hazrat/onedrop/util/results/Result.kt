package com.hazrat.onedrop.util.results


/**
 * @author Hazrat Ummar Shaikh
 * Created on 01-12-2024
 */


typealias RootError =  Error

sealed interface Result <out D, out E: RootError> {

    data class Success<out D, out E: RootError>(val data: D) : Result<D, E>
    data class Error<out D, out E: RootError>(val error: E) : Result<D, E>

}