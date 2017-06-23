package com.example.kalpesh.first_iot;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

/**
 * Created by kalpesh on 23/06/2017.
 */

public class GPIO_Input extends AppCompatActivity{

    public static final String GPIO_PIN="BCM21";
    private Gpio gpio_ButtonPress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);


        PeripheralManagerService peripheralManagerService= new PeripheralManagerService();

        try {
            gpio_ButtonPress= peripheralManagerService.openGpio(GPIO_PIN);

        } catch (IOException e) {
            throw new IllegalStateException(GPIO_PIN + " bus cannot be opened.", e);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            gpio_ButtonPress.setDirection(Gpio.DIRECTION_IN);
            gpio_ButtonPress.setActiveType(Gpio.ACTIVE_LOW);
            gpio_ButtonPress.setEdgeTriggerType(Gpio.EDGE_BOTH);
            gpio_ButtonPress.registerGpioCallback(gpioCallback);
        } catch (IOException e) {
            throw new IllegalStateException(GPIO_PIN + " bus cannot be monitored.", e);
        }
        catch (Exception e) {
            throw new IllegalStateException(GPIO_PIN + " bus cannot be configured.", e);
        }
    }


    /**
     * Call back to listen for input; like a button click
     */
    private final GpioCallback gpioCallback= new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                if (gpio.getValue()) {
                    Log.i("TUT", "ON PRESSED DOWN");
                } else {
                    Log.i("TUT", "ON PRESSED UP");
                }
            } catch (IOException e) {
                throw new IllegalStateException(GPIO_PIN + " cannot be read.", e);
            }
            return true;
        }

        @Override
        public void onGpioError(Gpio gpio, int error) {
            super.onGpioError(gpio, error);
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        gpio_ButtonPress.unregisterGpioCallback(gpioCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            gpio_ButtonPress.close();
        } catch (IOException e) {
            Log.e("TUT", GPIO_PIN + " bus cannot be closed, you may experience errors on next launch.", e);
        }
    }
}
