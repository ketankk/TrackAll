package in.kuari.trackall.couriers;

import android.content.Context;
import android.webkit.WebView;

import in.kuari.trackall.interfaces.CourierDao;

/**
 * Created by root on 1/26/16.
 */
public class Default implements CourierDao{
    public Default(WebView view,Context context) {
    }

    @Override
    public WebView hideShowContent() {
        return null;
    }

    @Override
    public void load() {

    }

    @Override
    public void fillForm() {

    }
}
