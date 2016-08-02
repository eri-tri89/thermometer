package com.example.erik.thermometer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

import com.example.erik.thermometer.model.Temperature;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-03-30.
 */
public class ThermometerView  extends View {
    private Paint paint = new Paint();
    private Paint paint2 = new Paint();
    private Temperature temperature;
    private RadioButton radioCelsius;
    private RadioButton radioFahrenheit;
    private RadioButton radioKelvin;

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

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public ThermometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        temperature = getTemperature();
        radioCelsius = getRadioCelsius();
        radioFahrenheit = getRadioFahrenheit();
        radioKelvin = getRadioKelvin();
        this.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        double height = this.getHeight();
        double width = this.getWidth();

        super.onDraw(canvas);
        drawTemperatureBar(canvas, height, width);
        drawThermometer(canvas, height, width);
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        double height = this.getHeight();
        double width = this.getWidth();
        super.draw(canvas);

        paint.setColor(Color.GRAY);
        canvas.drawRect(0, 0, (float) width / 50, (float) height, paint);
        canvas.drawRect(0,0,(float)width,(float)height/50,paint);
        canvas.drawRect((float)(49*width)/50,0,(float)width,(float)height,paint);
        canvas.drawRect(0,(float)(49*height)/50,(float)width,(float)height,paint);
    }

    private void drawThermometerLines(Canvas canvas, int[] value, double height, double width){

        double top = 0;
        int pos = 0;

        String tmp[] = new String[value.length];
        for(int j = 0;j<tmp.length;j++){
            tmp[j]=value[j]+"°";
        }
        for(double i = top; i < height;i++){

            paint.setColor(Color.WHITE);

            if((i%10)==0){
                if(pos>=0 && pos<tmp.length){
                    paint.setTextSize((float)height/100);
                    canvas.drawText(tmp[pos],(float)(3*width)/50,(float)((height*i)/100),paint);
                }
                pos++;
                canvas.drawLine((float)width/6,(float)((height*i)/100),(float)width,(float)((height*i)/100),paint);
            }else{
                canvas.drawLine(0,(float)((height*i)/100),(float)width,(float)((height*i)/100),paint);
            }

        }

    }

    public ArrayList<Double[]> getCelsiusScale(double height){
        int limit = (int)height;

        ArrayList<Double> topPositions = new ArrayList<>();
        for(double i = 0;i<limit;i++){
            topPositions.add((i*height)/100);
        }
        ArrayList<Double>celsiusValues = new ArrayList<>();
        for(double i = 60;i>(limit)*(-1);i--){
            celsiusValues.add(i);
        }
        ArrayList<Double[]> celsiusPositions = new ArrayList<>();
        for(int i = 0;i<limit;i++){
            celsiusPositions.add(new Double[]{celsiusValues.get(i),topPositions.get(i)});
        }

        return celsiusPositions;
    }

    public ArrayList<Double[]> getFahrenheitScale(double height){
        ArrayList<Double[]> fahrenheitPositions = new ArrayList<>();
        int limit = (int)height;
        ArrayList<Double> topPositions = new ArrayList<>();
        for(double i = 0;i<limit;i++){
            topPositions.add((i*height)/100);
        }
        ArrayList<Double>fahrenheitValues = new ArrayList<>();
        for(double i = 60;i>(limit)*(-1);i--){
            fahrenheitValues.add(temperature.celsiusToFahrenheit(i));
        }

        for(int i = 0;i<limit;i++){
            fahrenheitPositions.add(new Double[]{fahrenheitValues.get(i), topPositions.get(i)});
        }

        return fahrenheitPositions;
    }

    public ArrayList<Double[]> getKelvinScale(double height){
        ArrayList<Double[]> kelvinPositions = new ArrayList<>();

        int limit = (int)height;
        ArrayList<Double> topPositions = new ArrayList<>();
        for(double i = 0;i<limit;i++){
            topPositions.add((i*height)/100);
        }
        ArrayList<Double>kelvinValues = new ArrayList<>();
        for(double i = 60;i>(limit)*(-1);i--){
            kelvinValues.add(temperature.celsiusToKelvin(i));
        }
        for(int i = 0;i<limit;i++){
            kelvinPositions.add(new Double[]{kelvinValues.get(i), topPositions.get(i)});
        }

        return kelvinPositions;
    }

    private void drawTemperatureBar(Canvas canvas, double height, double width){
        if(temperature != null){

            ArrayList<Double[]> celsiusPositions = getCelsiusScale(height);
            ArrayList<Double[]> fahrenheitPositions =getFahrenheitScale(height);
            ArrayList<Double[]> kelvinPositions = getKelvinScale(height);

            int [] celsiusLimits = new int[]{50,40,30,20,10,0,-10,-20,-30};
            int [] fahrenheitLimits;
            fahrenheitLimits = new int[celsiusLimits.length];
            int [] kelvinLimits;
            kelvinLimits = new int[celsiusLimits.length];

            for(int i = 0;i<celsiusLimits.length;i++){
                fahrenheitLimits[i]=(int)temperature.celsiusToFahrenheit(celsiusLimits[i]);
                kelvinLimits[i]=(int)temperature.celsiusToKelvin(celsiusLimits[i]);
            }

            if(radioCelsius !=null || radioFahrenheit !=null || radioKelvin !=null ){
                assert radioCelsius != null;
                if(radioCelsius.isChecked()){
                    double currentTemperature = temperature.getCelsius();
                    setColorToTemperature(Color.RED,Color.BLUE,currentTemperature,0);
                    drawBar(canvas,width,height,celsiusPositions,currentTemperature);
                }
                else if(radioFahrenheit.isChecked()){
                    double currentTemperature = temperature.getFahrenheit();
                    setColorToTemperature(Color.YELLOW,Color.MAGENTA,currentTemperature,32);
                    drawBar(canvas,width,height,fahrenheitPositions,currentTemperature);
                }
                else if(radioKelvin.isChecked()){
                    double currentTemperature = temperature.getKelvin();
                    setColorToTemperature(Color.GRAY,Color.WHITE,currentTemperature,273);
                    drawBar(canvas,width,height,kelvinPositions,currentTemperature);
                }
            }
        }
    }

    private void drawBar(Canvas canvas,double width,double height,ArrayList<Double[]> list,double currentTemperature){
        for(int i=0;i<list.size();i++){
            if(list.get(i)[0] <= currentTemperature){
                double pos = list.get(i)[1];
                canvas.drawRect((float) width / 6, (float) pos, (float) width - 50, (float) height, paint2);
            }
        }
    }

    private void setColorToTemperature(int hot,int cold,double currentTemperature, int limit){
        if(currentTemperature >= limit){
            paint2.setColor(hot);
        }else{
            paint2.setColor(cold);
        }
    }

    private void drawThermometer(Canvas canvas,double height, double width){
        if(temperature != null){
            int celsiusValues[]={60,50,40,30,20,10,0,-10,-20,-30};
            int fahrenheitValues []= new int[celsiusValues.length];
            int kelvinValues []= new int[celsiusValues.length];
            for(int i = 0; i<celsiusValues.length;i++){
                fahrenheitValues [i]=(int)temperature.celsiusToFahrenheit(celsiusValues[i]);
                kelvinValues [i]=(int)temperature.celsiusToKelvin(celsiusValues[i]);
            }

            if(radioCelsius != null || radioFahrenheit != null || radioKelvin != null){
                assert radioCelsius != null;
                if(radioCelsius.isChecked()){
                    drawThermometerLines(canvas,celsiusValues,height,width);
                }else if(radioFahrenheit.isChecked()){
                    drawThermometerLines(canvas,fahrenheitValues,height,width);
                }else if(radioKelvin.isChecked()){
                    drawThermometerLines(canvas,kelvinValues,height,width);
                }
            }
        }
    }
}
