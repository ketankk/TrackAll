/*
package in.kuari.trackall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import in.kuari.trackall.R;
import in.kuari.trackall.bean.UserProfile;
import in.kuari.trackall.utils.AppController;


public class LoginSignUp extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener
{

    private static final String DATABASE_NAME = "track_all";
    private static final String GOOGLE_DRIVE_FILE_NAME ="trackAll" ;
    private DriveFile mfile;
    private boolean mResolvingError = false;


    private Activity activity;
    private static final int DIALOG_ERROR_CODE =100;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;
    private static final String TAG = "LoginActivity";
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        // Inflate the layout for this fragment
        setContentView(R.layout.fragment_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SignInButton signInButton = (SignInButton) findViewById(R.id.googleSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
        analytics();
    }
    Tracker mTracker;
    private void analytics(){
        AppController appController= (AppController) getApplication();
        mTracker=appController.getDefaultTracker();
    }
    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();


    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Connecting...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.hide();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();

    }


    private void SignIn() {

        Log.d("signIn","Connected");
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
 }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DIALOG_ERROR_CODE) {
            mResolvingError = false;
            if(resultCode == RESULT_OK) { // Error was resolved, now connect to the client if not done so.
                if(!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("req1", result.getStatus() + ""+result.isSuccess());
        if (result.isSuccess()) {
         //   Log.d("resSuc",result.getSignInAccount().toString());
            //SignedIn Successfully
            account = result.getSignInAccount();
            if(account!=null)
                goBackToHomePage();            //Toast.makeText(activity,account.getDisplayName()+account.getId(),Toast.LENGTH_SHORT).show();
       } else {
            //Signed Out
            if(signOut())
            goBackToHomePage();
      }
    }

    private boolean signOut() {
        if (mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("SignOut", status.toString());

                }
            });
        return false;
    }

    private void revokeAccess() {
        if (mGoogleApiClient.isConnected())
            Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.d("Revoke", status.toString());
                }
            });
    }
    private void goBackToHomePage() {
         String urlDP="https://lh3.googleusercontent.com/-nW_UEE8QEN4/AAAAAAAAAAI/AAAAAAAAAAA/_AVZs4E6WjQ/photo.jpg";//Default image url ifuser has no profile picture

        Intent intent = new Intent(this, MainActivity.class);
        if (account != null) {
            intent.putExtra("comingFrom", "login");
            UserProfile.setDisplayName(account.getDisplayName());
            if(account.getPhotoUrl()!=null)
            UserProfile.setImageURL(account.getPhotoUrl().toString());
            else
                UserProfile.setImageURL(urlDP);//Default url

            UserProfile.setTokenId(account.getIdToken());
            //Log.d("img", account.getPhotoUrl() + "dd");

        }else {
            intent.putExtra("comingFrom", "signout");

        }
        startActivity(intent);

    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.v(TAG, "Connection failed");
        if(mResolvingError) { // If already in resolution state, just return.
            return;
        } else if(result.hasResolution()) { // Error can be resolved by starting an intent with user interaction
            mResolvingError = true;
            try {
                result.startResolutionForResult(this, DIALOG_ERROR_CODE);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else { // Error cannot be resolved. Display Error Dialog stating the reason if possible.
            ErrorDialogFragment fragment = new ErrorDialogFragment();
            Bundle args = new Bundle();
            args.putInt("error", result.getErrorCode());
            fragment.setArguments(args);
            fragment.show(getFragmentManager(), "errordialog");
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.v(TAG, "Connected successfully");

        */
/* Connection to Google Drive established. Now request for Contents instance, which can be used to provide file contents.
           The callback is registered for the same. *//*

        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(contentsCallback);
    }

    final private ResultCallback<DriveApi.DriveContentsResult> contentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create new file contents");
                return;
            }
Log.d(TAG,"contentsCallback"+result.getDriveContents().toString());
            String mimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType("db");
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setTitle(GOOGLE_DRIVE_FILE_NAME) // Google Drive File name
                    .setMimeType(mimeType)
                    .setStarred(true).build();
            // create a file on root folder
            Drive.DriveApi.getRootFolder(mGoogleApiClient)
                    .createFile(mGoogleApiClient, changeSet, result.getDriveContents())
                    .setResultCallback(fileCallback);
        }

    };


    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {


        @Override
        public void onResult(DriveFolder.DriveFileResult result) {
            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error while trying to create the file");
                return;
            }
            Log.d(TAG,"fileCallback");
            mfile = result.getDriveFile();

            mfile.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(contentsOpenedCallback);
        }
    };
void writeContent(final DriveFolder pFldr, final String titl,
                  final String mime, DriveApi.DriveContentsResult content, final File file){
    DriveContents cont=content.getDriveContents();
    // write file to content, chunk by chunk
    if (cont != null) try {
        OutputStream oos = cont.getOutputStream();
        if (oos != null) try {
            InputStream is = new FileInputStream(file);
            byte[] buf = new byte[4096];
            int c;
            while ((c = is.read(buf, 0, buf.length)) > 0) {
                oos.write(buf, 0, c);
                oos.flush();
            }
        }
        finally { oos.close();}

        // content's COOL, create metadata
        MetadataChangeSet meta = new MetadataChangeSet.Builder().setTitle(titl).setMimeType(mime).build();

        // now create file on GooDrive
        pFldr.createFile(mGoogleApiClient, meta, cont).setResultCallback(new ResultCallback<DriveFolder.DriveFileResult>() {
            @Override
            public void onResult(DriveFolder.DriveFileResult driveFileResult) {
                if (driveFileResult != null && driveFileResult.getStatus().isSuccess()) {
                    // BINGO
                } else {
                    // report error
                }
            }
        });
    } catch (Exception e) { e.printStackTrace(); }
}
    final private ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback = new ResultCallback<DriveApi.DriveContentsResult>() {

        @Override
        public void onResult(DriveApi.DriveContentsResult result) {

            if (!result.getStatus().isSuccess()) {
                Log.v(TAG, "Error opening file");
                return;
            }Log.d(TAG,"contentsOpenedCallback");

            try {
                FileInputStream is = new FileInputStream(getDbPath());
                BufferedInputStream in = new BufferedInputStream(is);

                byte[] buffer = new byte[8 * 1024];
//writeContent();
                DriveContents content = result.getDriveContents();
                BufferedOutputStream out = new BufferedOutputStream(content.getOutputStream());
                int n = 0;
                while( ( n = in.read(buffer) ) > 0 ) {
                    out.write(buffer, 0, n);
                  //  Log.d(TAG,is.toString()+"-"+in.toString()+"-"+content.toString());
                }
out.flush();
                in.close();
                */
/*mfile.(api, content).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status result) {
                        // Handle the response status
                    }
                });*//*

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    private File getDbPath() {
        File file=this.getDatabasePath(DATABASE_NAME);

       */
/* try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            while((line = in.readLine()) != null)
            {
               Log.d(TAG,"data"+line);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*//*

        return file;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub
        Log.v(TAG, "Connection suspended");

    }
}
*/
