package com.aosp.freekite.patch.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.freekite.patch.aosppatch.PatchReadHookSource;
import com.android.freekite.patch.aosppatch.PatchReadHookSource.Yard;

public class MainActivity extends AppCompatActivity {

    private Yard yard = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.yard = PatchReadHookSource.doRead(this, "com.android.freekite.patch.aosppatch.MainActivity");


        this.yard.receive("test", new Object[]{"a", "b"});

        this.findViewById(R.id.test_id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("****************************");

            }
        });

        EditText editText = (EditText) this.findViewById(R.id.test_edit);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Thread.dumpStack();
                System.out.println("beforeTextChanged-------------------");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("onTextChanged------------------------");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                System.out.println("afterTextChanged------------------------");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Used to find calling chain
        // System.out.println("***************************");
        // Thread.dumpStack();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Used to find calling chain
        // System.out.println("***************************");
        // Thread.dumpStack();
        return super.dispatchKeyEvent(event);
    }
}
