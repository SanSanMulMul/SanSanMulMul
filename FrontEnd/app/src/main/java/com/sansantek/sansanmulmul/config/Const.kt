package com.sansantek.sansanmulmul.config

class Const {
    companion object {
        // Hiking Recording
        const val BEFORE_HIKING = 0
        const val HIKING = 1
        const val AFTER_HIKING = 2

        // Shared Preference
        const val SHARED_PREFERENCES_NAME = "sansanmulmul_preference"
        const val SP_HIKING_RECORDING_STATE = "hiking_recording_state"
        const val SP_SPEND_TIME_KEY = "spend_time"
        const val SP_SPEND_TIME_IS_RUNNING_KEY = "spend_time_is_running"

        // Camera permission code
        const val REQUEST_CAMERA_PERMISSION = 100
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}