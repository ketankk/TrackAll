/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.kuari.trackall.gcm;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import in.kuari.trackall.R;

public class GCM {

    private Context context;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean isReceiverRegistered;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public GCM(Context context){
        this.context=context;

    }
    public void start(){

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(context, RegistrationIntentService.class);
            context.startService(intent);
            registerReceiver();
        }
    }



    private void registerReceiver(){
        Log.d("GCM","registerReceiver");

        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(context).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
            Log.d("GCM","registerReceiver");

        }

       /* new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                InstanceID instanceID = InstanceID.getInstance(context);
                try {
                    String token = instanceID.getToken(context.getString(R.string.google_app_id),
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.d("tokensss",token);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
*/
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(((Activity)(context)), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
               // Log.i(TAG, "This device is not supported.");
                ((Activity)(context)).finish();
            }
            return false;
        }
        return true;
    }

}
