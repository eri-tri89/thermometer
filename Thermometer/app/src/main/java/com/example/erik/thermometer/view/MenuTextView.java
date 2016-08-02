package com.example.erik.thermometer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.erik.thermometer.R;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-04-04.
 */
public class MenuTextView extends View {

    public int id;
    private Paint paint = new Paint();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public MenuTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = this.getHeight();
        int width = this.getWidth();
        paint.setColor(Color.WHITE);
        switch(getId()){
            case R.id.action_instructions:

                ArrayList<String> instructions = getInstructions();
                drawText(instructions,canvas,width,height);
                super.onDraw(canvas);
                invalidate();
                break;

            case R.id.action_info:

                ArrayList<String> info = getInfo();
                drawText(info,canvas,width,height);
                super.onDraw(canvas);
                invalidate();
                break;
        }


    }

    private void drawText(ArrayList<String> text,Canvas canvas,int width,int height){
        int textSize = width/30;
        paint.setTextSize(textSize);

        for(int i = 0;i<text.size();i++){
            canvas.drawText(text.get(i),width/100,((height*(i))/15)+(textSize*2),paint);
        }
    }

    private ArrayList<String> getInstructions(){
        ArrayList<String> instructions = new ArrayList<>();
        instructions.add(0,"\t\t\t\t\t\tInstructions");
        instructions.add(1,"\n");
        instructions.add(2,"- The bar shows graphically the actual");
        instructions.add(3,"  temperature of the room you actually are.");
        instructions.add(4,"- The pressure section shows the ambient air");
        instructions.add(5,"  pressure in hPa.");
        instructions.add(6,"- The humidity section shows ambient air");
        instructions.add(7,"  humidity in percent.");
        instructions.add(8,"- You can share a report file by clicking the option");
        instructions.add(9,"  share report on the menu.");
        instructions.add(10,"- You can share a picture of the thermometer by");
        instructions.add(11,"  clicking the share icon on the action bar.");
        instructions.add(12,"* This app will work properly if the device");
        instructions.add(13,"  has the appropriate sensors.");


        return instructions;
    }

    private ArrayList<String> getInfo(){
        ArrayList<String> info = new ArrayList<>();
        info.add(0,"\t\t\t\t\t\tInformation");
        info.add(1,"\n");
        info.add(2,"This app is developed by Erik Perez");
        info.add(3,"In the year 2015.");

        return info;
    }




}
