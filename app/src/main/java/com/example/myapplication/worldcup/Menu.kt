// Menu.kt
package com.example.myapplication.worldcup

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(val name: String, val imageResourceId: Int) : Parcelable
