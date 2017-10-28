package ilya.vitaly.alcotestdroid.Services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by vital on 28/10/2017.
 */

public class PermissionsHandler extends Activity {
    private static final int MY_PERMISSIONS_REQUESTS = 88;
    private static Context context;

    public PermissionsHandler(Context context){
        this.context = context;
    }

    public static boolean requestPermissions(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                ActivityCompat.requestPermissions((Activity) context, new String[] {
                    android.Manifest.permission.CAMERA, android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUESTS);
            return false;
        }
        return true;
    }
}
