package com.lightscout.lightlabs.util

import androidx.compose.ui.graphics.Color

class Util {

    fun convertHexListToColorList(hexList: List<String>): List<Color> {
        return hexList.map {
            Color(android.graphics.Color.parseColor("#${it}"))
        }
    }
}