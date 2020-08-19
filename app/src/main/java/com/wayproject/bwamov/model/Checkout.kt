package com.wayproject.bwamov.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize                          //membuat data dinamis
data class Checkout (
    var kursi:String ? = "",
    var harga:String ? = ""
) : Parcelable