package com.aosp.freekite.patch.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.freekite.patch.aosppatch.PatchReadHookSource;
import com.android.freekite.patch.aosppatch.PatchReadHookSource.Yard;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void getHookSource() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        Yard yard = PatchReadHookSource.doRead(appContext, "com.android.freekite.patch.aosppatch.MainActivity");

        assertNotEquals(yard, null);
    }
}
