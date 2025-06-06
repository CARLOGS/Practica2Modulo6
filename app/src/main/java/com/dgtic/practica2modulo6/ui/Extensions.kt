package com.dgtic.practica2modulo6.ui

import android.app.Activity
import android.widget.Toast

fun Activity.message(message: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(
        this,
        message,
        duration
    ).show()
}