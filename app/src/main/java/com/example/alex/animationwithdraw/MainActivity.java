package com.example.alex.animationwithdraw;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends Activity {

    private static final int FRAME_DELAY = 200; // ms

    private ArrayList<Bitmap> mBitmaps;
    private final AtomicInteger mBitmapIndex = new AtomicInteger();
    private View mView;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BitmapFactory.Options options = new BitmapFactory.Options(); //scaling down image
        options.inSampleSize = 2; //by factors of 2


        // load images
        mBitmaps = new ArrayList<Bitmap>();
        for(int resId : new int[]{
                R.drawable.human001, R.drawable.human003,
                R.drawable.human005, R.drawable.human007, R.drawable.human009, R.drawable.human011, R.drawable.human013,
                R.drawable.human015, R.drawable.human017, R.drawable.human019, R.drawable.human021, R.drawable.human023,
                R.drawable.human025, R.drawable.human027, R.drawable.human029, R.drawable.human031, R.drawable.human033,
                R.drawable.human035, R.drawable.human037, R.drawable.human039, R.drawable.human041, R.drawable.human043,
                R.drawable.human045, R.drawable.human047, R.drawable.human049, R.drawable.human051, R.drawable.human053,
                R.drawable.human055, R.drawable.human057, R.drawable.human059, R.drawable.human061, R.drawable.human063,
                R.drawable.human065, R.drawable.human067, R.drawable.human069, R.drawable.human071, R.drawable.human073,
                R.drawable.human075, R.drawable.human077, R.drawable.human079, R.drawable.human081, R.drawable.human083,
                R.drawable.human085, R.drawable.human087, R.drawable.human089, R.drawable.human091, R.drawable.human093,
                R.drawable.human095, R.drawable.human097, R.drawable.human099, R.drawable.human101, R.drawable.human103,
                R.drawable.human105, R.drawable.human107, R.drawable.human109, R.drawable.human111, R.drawable.human113,
                R.drawable.human115, R.drawable.human117, R.drawable.human119, R.drawable.human001, R.drawable.human001,
                R.drawable.human001, R.drawable.human001
        }){
            mBitmaps.add(BitmapFactory.decodeResource(getResources(), resId, options));
        }

        // create view and start draw
        ViewGroup root = (ViewGroup) findViewById(R.id.activity_main);
        root.addView(mView = new View(this){
            @Override
            public void draw(Canvas canvas) {
                canvas.drawBitmap(mBitmaps.get(Math.abs(mBitmapIndex.get() % mBitmaps.size())), 10, 10, null);
                super.draw(canvas);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mThread = new Thread(){
            @Override
            public void run() {
                // wait and invalidate view until interrupted
                while(true){
                    try {
                        Thread.sleep(FRAME_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break; // get out if interrupted
                    }
                    mBitmapIndex.incrementAndGet();
                    mView.postInvalidate();
                }
            }
        };

        mThread.start();
    }

    @Override
    protected void onStop() {
        mThread.interrupt();
        super.onStop();
    }
}
