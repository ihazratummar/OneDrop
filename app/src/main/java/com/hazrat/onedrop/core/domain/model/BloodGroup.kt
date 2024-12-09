package com.hazrat.onedrop.core.domain.model

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

enum class BloodGroup {

    A_POSITIVE {
        override fun toString() = "A+"
    },
    A_NEGATIVE {
        override fun toString() = "A-"
    },
    B_POSITIVE {
        override fun toString() = "B+"
    },
    B_NEGATIVE {
        override fun toString() = "B-"
    },
    AB_POSITIVE {
        override fun toString() = "AB+"
    },
    AB_NEGATIVE {
        override fun toString() = "AB-"
    },
    O_POSITIVE {
        override fun toString() = "O+"
    },
    O_NEGATIVE {
        override fun toString() = "O-"
    };

}