package ba.lukic.petar.eknjiznica.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissionUtil {
    /*
     * Check if version is marshmallow and above.
     * Used in deciding to ask runtime permission
     * */
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static void checkPermission(Context context, String permission, PermissionAskListener listener) {
        if (shouldAskPermission(context, permission)) {
            listener.onNeedPermission();
        } else {
            listener.onPermissionGranted();
        }
    }

    /*
     * Callback on various cases on checking permission
     *
     * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
     *     If permission is already granted, onPermissionGranted() would be called.
     *
     * 2.  Above M, if the permission is being asked first time onNeedPermission() would be called.
     *
     * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
     *     would be called.
     *
     * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
     *     check box on previous request permission, onPermissionDisabled() would be called.
     * */
    public interface PermissionAskListener {
        /*
         * Callback to ask permission
         * */
        void onNeedPermission();

        /*
         * Callback on permission denied
         * */
        void onPermissionPreviouslyDenied();

        /*
         * Callback on permission "Never show again" checked and denied
         * */
        void onPermissionDisabled();

        /*
         * Callback on permission granted
         * */
        void onPermissionGranted();
    }
}