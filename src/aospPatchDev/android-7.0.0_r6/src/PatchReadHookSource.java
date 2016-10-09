package android.util;

import android.Manifest;
import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by yuer on 10/8/16.
 *
 * Read class bytes from hook place and load class
 */
public class PatchReadHookSource {
    private static final int REQUEST_READ_STORAGE = 945868432;
    private static final String HOOK_DIR = "aosp_hook";
    private static final String HOOK_FILE = "test.jar";
    private static final String CODE_CACHE_DIR = "aosp_hook_code_cache";
    private static final String HOOK_CLASS_NAME = "Test";

    /**
     * @hide
     */
    public static Class doRead(final Activity activity) {
        //ask for the permission in android M
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            int permission = 0;
            permission = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // at this moment wait for user's response
                makeRequest(activity);
                return null;
            }
        }

        File file = getHookFile();
        // code cache dir
        String cacheDir = activity.getFilesDir() + File.separator + CODE_CACHE_DIR;
        new File(cacheDir).mkdirs();

        DexClassLoader cl = new DexClassLoader(
                file.getAbsolutePath(),
                cacheDir,
                null,
                activity.getClassLoader());

        try {
            Class Test = cl.loadClass(HOOK_CLASS_NAME);
            System.out.println("----------------just load class-------------");
            System.out.println(Test);
            System.out.println("--------------------------------------------");
            return Test;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @hide
     */
    public static Class onRequestPermissionsResult(Activity activity,
                                                   int requestCode,
                                                   String permissions[],
                                                   int[] grantResults) {
        try {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //resume tasks needing this permission
                return doRead(activity);
            }
        } catch (Exception e) {
            System.out.println("[error]----------------------------");

            e.printStackTrace();
        }

        return null;
    }

    private static void makeRequest(Activity activity) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            activity.requestPermissions(
                    new String[]{permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        }
    }

    private static File getHookFile() {
        File dir = Environment.getExternalStoragePublicDirectory(HOOK_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("--------------------------------------------");
            System.out.println("Directory not created");
        }
        File file = new File(dir, HOOK_FILE);
        return file;
    }
}
