package example.com.CoursePackge;


import android.content.Context;
import android.util.Log;

public final class DensityUtil {

    private static float density = -1F;
    private static int widthPixels = -1;
    private static int heightPixels = -1;

    private DensityUtil() {
        Log.d("my","private DensityUtil(){");
    }

    public static float getDensity(Context context) {
        if (density <= 0F) {
            density = context.getResources().getDisplayMetrics().density;
            Log.d("my","getDensity"+density);
        }
        return density;
    }

    public static int dip2px(Context context, float dpValue) {
        Log.d("my","dip2px"+(int) (dpValue * getDensity(context) + 0.5F));
        return (int) (dpValue * getDensity(context) + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getDensity(context) + 0.5F);
    }

    public static int getScreenWidth(Context context) {
        if (widthPixels <= 0) {
            widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        }
        return widthPixels;
    }


    public static int getScreenHeight(Context context) {
        if (heightPixels <= 0) {
            heightPixels =context.getResources().getDisplayMetrics().heightPixels;
        }
        return heightPixels;
    }
}
