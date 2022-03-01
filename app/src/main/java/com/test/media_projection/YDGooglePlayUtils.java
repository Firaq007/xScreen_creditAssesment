package com.test.media_projection;


import android.content.Context;
import android.content.pm.InstallSourceInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

public class YDGooglePlayUtils {

    public static String GOOGLE_PLAY = "com.android.vending";
    public static String AMAZON = "com.amazon.venezia";

    public static boolean isInstalledViaGooglePlay(Context ctx) {
        return isInstalledVia(ctx, GOOGLE_PLAY);
    }

    public static boolean isInstalledViaAmazon(Context ctx) {
        return isInstalledVia(ctx, AMAZON);
    }

    public static boolean isSideloaded(Context ctx) {
        String installer = getInstallerPackageName(ctx);
        return TextUtils.isEmpty(installer);
    }

    public static boolean isInstalledVia(Context ctx, String required) {
        String installer = getInstallerPackageName(ctx);
        return required.equals(installer);
    }

    public static String getInstallerPackageName(Context ctx) {
        try {
            String packageName = "com.google.android.youtube";
            PackageManager pm = ctx.getPackageManager();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                InstallSourceInfo info = pm.getInstallSourceInfo(packageName);
                if (info != null) {
                    return info.getInstallingPackageName();
                }
            }
            else{
                return pm.getInstallerPackageName(packageName);
            }

        } catch (PackageManager.NameNotFoundException e) {
        }
        return "null";
    }

}
