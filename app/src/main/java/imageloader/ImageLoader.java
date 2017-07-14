package imageloader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import imageloader.config.GlobalConfig;
import imageloader.config.builder.ConfigBuilder;
import imageloader.config.builder.RequestBuilder;
import imageloader.i.ILoader;


/**
 * Created by Jungle on 2017/6/6.
 */

public class ImageLoader {
    public static Context context;

    static Handler getHandler() {
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        return handler;
    }

    static Handler handler;

    public static void init(final Context context, int cacheSizeInM, ILoader imageLoader) {
        ImageLoader.context = context;
        GlobalConfig.init(context, cacheSizeInM, imageLoader);
        handler = new Handler(Looper.getMainLooper());
        //imageLoader.init();
    }

    public static ILoader getActualLoader() {
        return GlobalConfig.getLoader();
    }

    /**
     * 加载普通图片
     *
     * @param context
     * @return
     */
    public static RequestBuilder with(Context context) {
        return new ConfigBuilder().with(context);
    }

    public static void onLowMemory() {
        GlobalConfig.getLoader().onLowMemory();
    }

    public static void trimMemory(int level) {
        GlobalConfig.getLoader().trimMemory(level);
    }

    public static void clearAllMemoryCaches() {
        GlobalConfig.getLoader().onLowMemory();
    }


}
