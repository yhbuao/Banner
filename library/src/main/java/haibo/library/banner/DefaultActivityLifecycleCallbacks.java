package haibo.library.banner;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @author: yuhaibo
 * @time: 2018/2/11 15:45.
 * projectName: Banner.
 * Description: 默认实现Activity生命周期里面的回调
 */

public class DefaultActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
