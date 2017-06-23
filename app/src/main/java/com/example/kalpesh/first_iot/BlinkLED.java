package com.example.kalpesh.first_iot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kalpesh on 22/06/2017.
 */

/**
 * General Purpose Input/Output (GPIO) pins provide a programmable interface to read the state of a binary input device
 * (such as a pushbutton switch) or control the on/off state of a binary output device (such as an LED).

 You can configure GPIO pins as an input or output with either a high or low state.
 As an input, an external source determines the state, and your app can read the current value or react to changes in state. As an output, your app configures the state of the pin.
 */
public class BlinkLED extends AppCompatActivity {

    public static final String LED_GREEN="BCM19";
    public static final String LED_BLUE="BCM26";

    private Handler mHandler;
    private Gpio mLEDGPIO=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isOn;


        PeripheralManagerService peripheralManagerService = new PeripheralManagerService();
        try {
            mLEDGPIO = peripheralManagerService.openGpio(LED_BLUE);

            mLEDGPIO.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLEDGPIO.setActiveType(Gpio.ACTIVE_LOW);

            // Step 1: Create a handler and initilaize with mainLooper queue
            mHandler= new Handler(Looper.getMainLooper());


        } catch (IOException e) {
            throw(new IllegalStateException("connection is lossed" , e));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Step 3: Contract between handler and runnable. Assign the runnable object to the handler in post method
        mHandler.post(runnable);
    }


    //Step 2: Create a runnable object with run method
    private final Runnable runnable= new Runnable() {
        @Override
        public void run() {

            boolean isOn;
            try {
                isOn= mLEDGPIO.getValue();

                if(isOn){
                    mLEDGPIO.setValue(false);
                }
                else{
                    mLEDGPIO.setValue(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Step 5: Define the interval  ( handler tells the runnable after a delay of what time he should re run itself)
            mHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }

    };


    @Override
    protected void onDestroy() {
        //Step 4: Remove the assigned runnable in onStop() or in onDestroy() method
        mHandler.removeCallbacks(runnable);
        if(mLEDGPIO!=null)
            try {
                mLEDGPIO.close();
            }catch (IOException e){
            throw(new IllegalStateException());
            }

        super.onDestroy();
    }
}
