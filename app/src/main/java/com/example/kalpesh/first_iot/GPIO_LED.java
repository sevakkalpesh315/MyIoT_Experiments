package com.example.kalpesh.first_iot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kalpesh on 21/06/2017.
 *
 *
 *
 * Skeleton of the main Android Things activity. Implement your device's logic
 * in this class.
 *
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 *
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 */


public class GPIO_LED extends Activity {
    /**
     * https://github.com/pimoroni/rainbow-hat/blob/master/documentation/Technical-reference.md
     */
    private static final String GREEN_LED_PIN = "BCM19";
    private static final String RED_LED_PIN = "BCM6";
    private static final String BLUE_LED_PIN="BCM26";
    /**
     * 1 Get the details of the GPIO you want to communicate to
     * 2 There are two cowboys involved: PeripheralManagerService and the other is GPIO
     * 3 The PeripheralManager is used to initialize the GPIO object by opening the connection
     * 4 then the GPIO has to configure itself
     *
     */

    private Gpio mLedGpio;
    private Handler ledToggleHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManagerService service = new PeripheralManagerService();

        try {
            mLedGpio = service.openGpio(BLUE_LED_PIN);
        } catch (IOException e) {
            throw new IllegalStateException(RED_LED_PIN + " bus cannot be opened.", e);
        }

        try {
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);
            mLedGpio.setActiveType(Gpio.ACTIVE_HIGH);
        } catch (IOException e) {
            throw new IllegalStateException(RED_LED_PIN + " bus cannot be configured.", e);
        }

        ledToggleHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void onStart() {
        super.onStart();
        ledToggleHandler.post(toggleLed);
    }

    private final Runnable toggleLed = new Runnable() {
        @Override
        public void run() {
            boolean isOn;
            try {
                isOn = mLedGpio.getValue();
            } catch (IOException e) {
                throw new IllegalStateException(RED_LED_PIN + " cannot be read.", e);
            }
            try {
                if (isOn) {
                    mLedGpio.setValue(false);
                } else {
                    mLedGpio.setValue(true);
                }
            } catch (IOException e) {
                throw new IllegalStateException(RED_LED_PIN + " cannot be written.", e);
            }
            ledToggleHandler.postDelayed(this, TimeUnit.SECONDS.toMillis(1));
        }
    };

    @Override
    protected void onStop() {
        ledToggleHandler.removeCallbacks(toggleLed);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ledToggleHandler.removeCallbacks(toggleLed);

        if (mLedGpio != null) {
            try {
                mLedGpio.close();

            } catch (IOException e) {
                Log.e("TUT", RED_LED_PIN + " bus cannot be closed, you may experience errors on next launch.", e);
            }
        }
    }
}