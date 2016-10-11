package com.android.freekite.patch.aosppatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.android.freekite.patch.aosppatch.PatchReadHookSource.Yard;

public class MainActivity extends AppCompatActivity {

    private Yard yard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.yard = PatchReadHookSource.doRead(this, "com.freekite.android.patch.aosppatch.MainActivity");
        this.yard.receive("test", new Object[]{"a", "b"});
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Used to find calling chain
        System.out.println("***************************");
        Thread.dumpStack();
        return super.dispatchTouchEvent(ev);
    }
}
