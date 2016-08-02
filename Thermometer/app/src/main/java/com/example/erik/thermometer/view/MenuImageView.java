package com.example.erik.thermometer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.erik.thermometer.R;

import java.util.ArrayList;

/**
 * Created by Erik on 2015-04-04.
 */
public class MenuImageView extends View {
    public int id;
    Paint paint = new Paint();

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public MenuImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        ArrayList<Bitmap> images = getThermometerImages();
        for(int i = 0; i<images.size();i++){
            if(images.get(i)!= null){
                switch(getId()){
                    case R.id.action_instructions:
                        drawBackground(canvas,images.get(0));
                        super.onDraw(canvas);
                        invalidate();
                        break;
                    case R.id.action_info:
                        drawBackground(canvas,images.get(1));
                        super.onDraw(canvas);
                        invalidate();
                        break;
                }
            }

        }

    }

    private ArrayList<Bitmap> getThermometerImages(){
        Bitmap therm1 = BitmapFactory.decodeResource(getResources(), R.drawable.thermometer_1);
        Bitmap therm2 = BitmapFactory.decodeResource(getResources(), R.drawable.thermometer_2);

        ArrayList<Bitmap> images = new ArrayList<>();
        images.add(therm1);
        images.add(therm2);

        return images;
    }

    private void drawBackground(Canvas canvas, Bitmap image){
        canvas.drawBitmap(image,0,0,paint);
    }
}
