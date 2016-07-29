package com.example.erik.thermometer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//A custom comment
public class MainActivity extends ActionBarActivity implements SensorEventListener {

    public Temperature temperature;
    private SensorManager sensorManager;
    private TextView pressureValue;
    private TextView humidityValue;
    private RadioButton radioCelsius;
    private RadioButton radioFahrenheit;
    private RadioButton radioKelvin;
    public TemperatureView temperatureView;
    public ThermometerView thermometerView;

    private static final String tag = "sensors";
    private static final String tag2 = "picture";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.thermo_icon);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        temperature = getTemperature();

        pressureValue = (TextView)this.findViewById(R.id.pressureValue);
        humidityValue = (TextView)this.findViewById(R.id.humidityValue);

        radioCelsius = (RadioButton)this.findViewById(R.id.radCelsius);
        radioFahrenheit = (RadioButton)this.findViewById(R.id.radFahrenheit);
        radioKelvin = (RadioButton)this.findViewById(R.id.radKelvin);

        thermometerView = getThermometerView();
        temperatureView = getTemperatureView();

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        getSensorsToLog();
    }

    public Temperature getTemperature(){
        return new Temperature();
    }

    private void getSensorsToLog(){
        List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor:list){
            Log.i(tag, "sensor type = "+sensor.getType()+" sensor name "+sensor.getName());

        }
    }

    public TemperatureView getTemperatureView(){
        TemperatureView temperatureView = (TemperatureView) this.findViewById(R.id.Tempview);
        temperatureView.setTemperature(temperature);
        temperatureView.setRadioCelsius(radioCelsius);
        temperatureView.setRadioFahrenheit(radioFahrenheit);
        temperatureView.setRadioKelvin(radioKelvin);
       return temperatureView;
    }

    public ThermometerView getThermometerView(){
        ThermometerView thermometerView = (ThermometerView) this.findViewById(R.id.Thermoview);
        thermometerView.setRadioCelsius(radioCelsius);
        thermometerView.setRadioFahrenheit(radioFahrenheit);
        thermometerView.setRadioKelvin(radioKelvin);
        thermometerView.setTemperature(temperature);
        return thermometerView;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("celsius",temperature.getCelsius());
        outState.putDouble("fahrenheit",temperature.getFahrenheit());
        outState.putDouble("kelvin",temperature.getKelvin());
        outState.putCharSequence("pressure",pressureValue.getText());
        outState.putCharSequence("humidity",humidityValue.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        temperature.setCelsius(savedInstanceState.getDouble("celsius"));
        temperature.setFahrenheit(savedInstanceState.getDouble("fahrenheit"));
        temperature.setKelvin(savedInstanceState.getDouble("kelvin"));
        pressureValue.setText(savedInstanceState.getCharSequence("pressure"));
        humidityValue.setText(savedInstanceState.getCharSequence("humidity"));
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i = new Intent(MainActivity.this,MenuActivity.class);
        switch(id){
            case R.id.action_share_report:
                String report = getReportString();
                if (!TextUtils.isEmpty(report)){
                    File file = new File(getCacheDir(),"documents/report.txt");
                    createParentDirectories(file);
                    writeStringToFile(report, file);
                    Intent intent = shareFile(file,"This is my room's temperature","Share report with");
                    startActivity(intent);
                }
                return true;
            case R.id.action_share_image:
                File screenshot = getScreenshot();
                createParentDirectories(screenshot);
                Intent intent = shareFile(screenshot,"This is my room's thermometer!","Share image with");
                startActivity(intent);
                return true;
            case R.id.action_facts_temperature:
                //Intent to a website
                String website="http://en.wikipedia.org/wiki/Temperature";
                Intent webBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                if (webBrowser.resolveActivity(getPackageManager()) != null) {
                    startActivity(webBrowser);
                }
                return true;
            case R.id.action_instructions:
                //Intent to another activity
                i.putExtra("id",R.id.action_instructions);
                this.startActivity(i);
                return true;
            case R.id.action_info:
                //Intent to another activity
                i.putExtra("id",R.id.action_info);
                this.startActivity(i);
                return true;
            case R.id.action_exit:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Intent shareFile(File file, String subject, String title){
        Intent intent = null;
        if(file != null) {
            try{
                Uri uri = FileProvider.getUriForFile(this, "com.example.erik.thermometer", file);
                Log.i(tag2,"Uri = "+uri.toString());
                ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this);
                intentBuilder.setType("image/png");
                intentBuilder.setSubject(subject);
                intentBuilder.setStream(uri);//uri
                intentBuilder.setChooserTitle(title);
                intentBuilder.createChooserIntent();
                intent = intentBuilder.getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Log.i(tag2,"Shared successfully!");
                return intent;

            }catch(IllegalArgumentException e){
                Log.i(tag2,"Couldn't share file "+e.getMessage());
            }

        }else{
            Log.i(tag2,"File is null");
            return null;
        }
        return intent;
    }

    public String getReportString(){
        String currentDateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+" || ";
        String report = "";
        if(radioCelsius.isChecked()){
            float celsius = (float)temperature.getCelsius();
            report = currentDateAndTime+" My room's temperature is about "+celsius+"°C";
        }else if(radioFahrenheit.isChecked()){
            float fahrenheit = (float)temperature.getFahrenheit();
            report = currentDateAndTime+" My room's temperature is about "+fahrenheit+"°F";
        }else if(radioKelvin.isChecked()){
            float kelvin = (float)temperature.getCelsius();
            report = currentDateAndTime+" My room's temperature is about "+kelvin+"°K";
        }
        Log.i(tag2,"Report String = "+report);
        return report;
    }
    public static void createParentDirectories(final File inFile)
    {
        if (inFile != null){
            final File parentDir = inFile.getParentFile();
            if ((parentDir != null) && !parentDir.exists()){
                parentDir.mkdirs();
            }
        }
    }

    public boolean writeStringToFile(final String inData, final File toFile) {
        if (toFile == null) return false;

        BufferedWriter output;
        BufferedReader input;
        try {
            input = new BufferedReader(new StringReader(inData));
            output = new BufferedWriter(new FileWriter(toFile));
            int n = 0;
            while (n != -1) {
                n = input.read();
                output.write(n);
            }
            input.close();
            output.close();
            Log.i(tag2,"File written");
            return true;
        } catch (Exception e) {
            Log.i(tag2,"error = "+e.getMessage());
        }
        return false;
    }

    public File getScreenshot(){

        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        File file = new File(getCacheDir(),"documents/img.png");

        Log.i(tag2,"Dir = "+file.getPath());
        try{
            FileOutputStream out = new FileOutputStream(file);
            Bitmap picture = Bitmap.createBitmap(v1.getDrawingCache());
            picture.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
            Log.i(tag2,"File created");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.i(tag2,"File not created");
        }
        return file;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
               getTemperature(event);
                break;
            case Sensor.TYPE_PRESSURE:
                getPressure(event);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                getHumidity(event);
                break;
        }

    }
    private void getTemperature(SensorEvent event){
        double celsiusValue = event.values[0];
        if(radioCelsius.isChecked()){
            temperature.setCelsius(celsiusValue);

        }else if(radioFahrenheit.isChecked()){
            double fahrenheitValue = temperature.celsiusToFahrenheit(celsiusValue);
            temperature.setFahrenheit(fahrenheitValue);

        }
        else if(radioKelvin.isChecked()){
            double kelvinValue = temperature.celsiusToKelvin(celsiusValue);
            temperature.setKelvin(kelvinValue);

        }
    }
    private void getPressure(SensorEvent event){
        int prValue = (int)event.values[0];
        pressureValue.setText(prValue+" hPa");

    }
    private void getHumidity(SensorEvent event){
        int humValue = (int)event.values[0];
        humidityValue.setText(humValue+"%");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
