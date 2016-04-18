package in.kuari.trackall.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import in.kuari.trackall.R;
import in.kuari.trackall.bean.UserProfile;
import in.kuari.trackall.utils.AppController;

import static android.R.drawable.*;


public class LoginSignUp extends AppCompatActivity {

    private Activity activity;
   private static final int GP_SIGN_IN = 105;
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

//Google Analytics starts
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        //Google Analytics end
        //initialize GoogleApiClient
        initialize();
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
    protected void onPause() {
        super.onPause();
        mProgressDialog.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            // Log.d(TAG, "Got cached sign-in");
            Log.d("onstart","Loginsignup");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // Log.d(TAG, "Not cached sign-in");

            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    hideProgressDialog();
                    //Log.d(TAG, "Sign google");

                    handleSignInResult(googleSignInResult);
                }
            });
        }
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

    private void initialize() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void SignIn() {

        // mGoogleApiClient.stopAutoManage(this);
        //Popup

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GP_SIGN_IN);
 }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("req",requestCode+"cc");
        switch (requestCode) {
            case GP_SIGN_IN:
                Log.d("data", requestCode+" "+resultCode);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
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

}
