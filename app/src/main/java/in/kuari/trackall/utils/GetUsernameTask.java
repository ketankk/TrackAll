package in.kuari.trackall.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

/**
 * Created by sultan mirza on 3/3/16.
 */
public class GetUsernameTask extends AsyncTask<String,Void,Void> {
        Activity mActivity;
        String mScope;
        String mEmail;

        GetUsernameTask(Activity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
        }




    @Override
    protected Void doInBackground(String... params) {
        String token=fetchToken();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
    private String fetchToken(){
        try {
            return GoogleAuthUtil.getToken(mActivity,mEmail,mScope);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GoogleAuthException e) {
            e.printStackTrace();
        }
        return null;
    }
}
