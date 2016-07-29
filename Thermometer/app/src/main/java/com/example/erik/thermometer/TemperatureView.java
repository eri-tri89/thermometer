package com.example.erik.thermometer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Erik on 2015-03-31.
 */
public class TemperatureView extends View{
    private Paint paint = new Paint();

    private RadioButton radioCelsius;
    private RadioButton radioFahrenheit;
    private RadioButton radioKelvin;
    private Temperature temperature;

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public RadioButton getRadioCelsius() {
        return radioCelsius;
    }

    public void setRadioCelsius(RadioButton radioCelsius) {
        this.radioCelsius = radioCelsius;
    }

    public RadioButton getRadioFahrenheit() {
        return radioFahrenheit;
    }

    public void setRadioFahrenheit(RadioButton radioFahrenheit) {
        this.radioFahrenheit = radioFahrenheit;
    }

    public RadioButton getRadioKelvin() {
        return radioKelvin;
    }

    public void setRadioKelvin(RadioButton radioKelvin) {
        this.radioKelvin = radioKelvin;
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackgroundColor(Color.DKGRAY);

        radioCelsius = getRadioCelsius();
        radioFahrenheit = getRadioFahrenheit();
        radioKelvin = getRadioKelvin();
        temperature = getTemperature();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        double height = this.getHeight();
        double width = this.getWidth();
        paint.setColor(Color.BLACK);
        canvas.drawRect((float)width/20,(float)height/20,(float)(19*width)/20,(float)(19*height)/20,paint);
        if(radioCelsius !=null || radioFahrenheit !=null || radioKelvin !=null || temperature!= null){
            assert radioCelsius != null;
            if(radioCelsius.isChecked()){
                super.onDraw(canvas);
                int celsiusTemp = (int)temperature.getCelsius();
                setColorToText(Color.RED,Color.BLUE,celsiusTemp,0);
                drawTemperatureText(canvas,celsiusTemp+"°C",height,width);

            }else if(radioFahrenheit.isChecked()){
                super.onDraw(canvas);
                int fahrenheitTemp = (int)temperature.getFahrenheit();
                setColorToText(Color.YELLOW,Color.MAGENTA,fahrenheitTemp,32);
                drawTemperatureText(canvas,fahrenheitTemp+"°F",height,width);

            }else if(radioKelvin.isChecked()){
                super.onDraw(canvas);
                int kelvinTemp = (int)temperature.getKelvin();
                setColorToText(Color.GRAY,Color.WHITE,kelvinTemp,273);
                drawTemperatureText(canvas,kelvinTemp+"°K",height,width);
            }
        }
    }

    private void setColorToText(int hot,int cold,double currentTemperature, int limit) {
        if (currentTemperature >= limit) {
            paint.setColor(hot);
        } else {
            paint.setColor(cold);
        }
    }

    private void drawTemperatureText(Canvas canvas, String temperature, double height, double width){

        paint.setTextSize((float)height/2);
        canvas.drawText(temperature,
        (float)width/6,
        (float)(2*height)/3,
        paint);
        invalidate();
    }


}
