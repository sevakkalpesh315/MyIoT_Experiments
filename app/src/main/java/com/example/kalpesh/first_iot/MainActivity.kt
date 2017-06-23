package com.example.kalpesh.first_iot

import android.graphics.Path
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.android.things.pio.Gpio
import com.google.android.things.pio.PeripheralManagerService

class MainActivity : AppCompatActivity() {
    /**
     * We know that , this is how we create a global class variable
     */
    var mLedGPIO: Gpio?=null
    var mHandler: Handler?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
         * Gets the list of all connected GPIO pins from Raspberry Pi3
         */
        val peripheral =  PeripheralManagerService();
        Log.i("GPIO LIST",""+ peripheral.gpioList)

        mLedGPIO=peripheral.openGpio(GREEN_LIGHT)


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * We know that this is how we Declare static final string values
     */
    companion object{
        private val GREEN_LIGHT="BCM26"
    }
}
