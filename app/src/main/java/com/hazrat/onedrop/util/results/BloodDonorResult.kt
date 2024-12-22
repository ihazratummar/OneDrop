package com.hazrat.onedrop.util.results

/**
 * @author Hazrat Ummar Shaikh
 * Created on 07-12-2024
 */

enum class BloodDonorProfileSuccess{
    SUCCESS,
    PROFILE_EXISTS
}

enum class BloodDonorProfileError: Error {
    FAILED,
    UNKNOWN
}

enum class SelfProfileSuccess{
    UPDATED
}
enum class SelfProfileError : Error{
    UPDATE_FAILED
}