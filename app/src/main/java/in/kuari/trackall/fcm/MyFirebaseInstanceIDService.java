package in.kuari.trackall.fcm;

/**
 * Created by root on 9/27/16.
 */
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.kuari.trackall.databases.MYSQLHandler;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        SharedPreferences.Editor editor =getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
        editor.putBoolean("FCMreg", true);
        editor.commit();//commit because we want it to be done before calling function
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        analytics("sendRegistrationToServer");
        if (ifFCMReg()) {
            MYSQLHandler handler = new MYSQLHandler(this);
            handler.sendfcmtokentoserver(token);
        }

    }
    private boolean ifFCMReg() {

        SharedPreferences pref = getSharedPreferences("TRACKALL", MODE_PRIVATE);
        return pref.getBoolean("FCMreg", false);

    }
    private void analytics(String from){

        mFirebaseAnalytics= FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(TAG,from);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}