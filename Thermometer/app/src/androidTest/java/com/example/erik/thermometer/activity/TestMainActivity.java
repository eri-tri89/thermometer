package com.example.erik.thermometer.activity;

import android.test.ActivityInstrumentationTestCase2;

import com.example.erik.thermometer.activity.MainActivity;
import com.example.erik.thermometer.model.Temperature;
import com.example.erik.thermometer.view.TemperatureView;
import com.example.erik.thermometer.view.ThermometerView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Erik on 2015-04-07.
 */
public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {


    public TestMainActivity(){super(MainActivity.class);}

    public void testGetReportString()throws Exception{
        MainActivity mainActivity = getActivity();
        String report = mainActivity.getReportString();
        assertNotNull("Report",report);
    }
    public void testGetScreenShot()throws Exception{
        MainActivity mainActivity = getActivity();
        File screenshot = mainActivity.getScreenshot();
        assertNotNull("Screenshot",screenshot);
    }

    public void testGetTemperatureView()throws Exception{
        MainActivity mainActivity = getActivity();
        TemperatureView temperatureView = mainActivity.getTemperatureView();
        assertNotNull("TemperatureView",temperatureView);
    }

    public void testGetThermometerView()throws Exception{
        MainActivity mainActivity = getActivity();
        ThermometerView thermometerView = mainActivity.getThermometerView();
        assertNotNull("ThermometerView",thermometerView);
    }

    public void testCelsiusToFahrenheit()throws Exception{
        MainActivity mainActivity = getActivity();
        Temperature tmp = mainActivity.getTemperature();
        double celsius = tmp.getCelsius();
        double fahrenheit = celsius*9/5+32;
        assertEquals("celsius to fahrenheit",fahrenheit,tmp.celsiusToFahrenheit(celsius));
    }

    public void testCelsiusToKelvin()throws Exception{
        MainActivity mainActivity = getActivity();
        Temperature tmp = mainActivity.getTemperature();
        double celsius = tmp.getCelsius();
        double kelvin = celsius+273.15;
        assertEquals("celsius to kelvin",kelvin,tmp.celsiusToKelvin(celsius));
    }

    public void testCelsiusScale()throws Exception{
        MainActivity mainActivity = getActivity();
        ThermometerView tmp = mainActivity.getThermometerView();
        double height = tmp.getHeight();
        ArrayList<Double[]> celsiusScale = tmp.getCelsiusScale(height);
        assertNotNull("celsius Scale in thermometerView",celsiusScale);
    }

    public void testFahrenheitScale()throws Exception{
        MainActivity mainActivity = getActivity();
        ThermometerView tmp = mainActivity.getThermometerView();
        double height = tmp.getHeight();
        ArrayList<Double[]> fahrenheitScale = tmp.getCelsiusScale(height);
        assertNotNull("fahrenheit Scale in thermometerView",fahrenheitScale);
    }
    public void testKelvinScale()throws Exception{
        MainActivity mainActivity = getActivity();
        ThermometerView tmp = mainActivity.getThermometerView();
        double height = tmp.getHeight();
        ArrayList<Double[]> kelvinScale = tmp.getCelsiusScale(height);
        assertNotNull("kelvin Scale in thermometerView",kelvinScale);

    }
}
