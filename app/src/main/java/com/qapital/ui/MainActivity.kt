package com.qapital.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qapital.R
import com.qapital.base.QapitalApplication

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as QapitalApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
