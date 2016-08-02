package com.example.erik.thermometer.model;

/**
 * Created by Erik on 2015-03-30.
 */
public class Temperature {

    private double celsius;
    private double fahrenheit;
    private double kelvin;

    public Temperature() {
        celsius = 0;
        fahrenheit = 32;
        kelvin = 273.15;
    }

    public double getCelsius() {
        return celsius;
    }

    public void setCelsius(double celsius) {
        this.celsius = celsius;
    }

    public double getFahrenheit() {
        return fahrenheit;
    }

    public void setFahrenheit(double fahrenheit) {
        this.fahrenheit = fahrenheit;
    }

    public double getKelvin() {
        return kelvin;
    }

    public void setKelvin(double kelvin) {
        this.kelvin = kelvin;
    }

    public double celsiusToFahrenheit(double celsius){
        return celsius*9/5+32;
    }

    public double celsiusToKelvin(double celsius){
        return celsius+273.15;
    }


}
