package com.hazrat.onedrop.core.domain.model


import androidx.annotation.DrawableRes
import com.hazrat.onedrop.R

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

enum class BloodGroup {

    A_POSITIVE {
        override fun toString() = "A+"

        @DrawableRes
        override fun getIconResId() =
            R.drawable.a_plus
    },
    A_NEGATIVE {
        override fun toString() = "A-"

        @DrawableRes
        override fun getIconResId() = R.drawable.a_minus
    },
    B_POSITIVE {
        override fun toString() = "B+"

        @DrawableRes
        override fun getIconResId() = R.drawable.b_plus
    },
    B_NEGATIVE {
        override fun toString() = "B-"

        @DrawableRes
        override fun getIconResId() = R.drawable.b_minus
    },
    AB_POSITIVE {
        override fun toString() = "AB+"

        @DrawableRes
        override fun getIconResId() = R.drawable.ab_plus
    },
    AB_NEGATIVE {
        override fun toString() = "AB-"

        @DrawableRes
        override fun getIconResId() = R.drawable.ab_minus
    },
    O_POSITIVE {
        override fun toString() = "O+"

        @DrawableRes
        override fun getIconResId() = R.drawable.o_plus
    },
    O_NEGATIVE {
        override fun toString() = "O-"

        @DrawableRes
        override fun getIconResId() = R.drawable.o_minus
    };

    @DrawableRes
    abstract fun getIconResId(): Int
}