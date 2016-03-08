package in.kuari.trackall.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.squareup.picasso.Picasso;

import in.kuari.trackall.R;
import in.kuari.trackall.bean.UserProfile;
import in.kuari.trackall.databases.MYSQLHandler;
import in.kuari.trackall.fragments.AppSettings;
import in.kuari.trackall.fragments.CourierFragment;
import in.kuari.trackall.fragments.ECommerceFragment;
import in.kuari.trackall.fragments.FlightsFragment;
import in.kuari.trackall.fragments.HomeFragment;
import in.kuari.trackall.utils.CircularImage;
import in.kuari.trackall.utils.FunctionTools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

private Activity activity;
    private NavigationView navigationView;
    private TextView loginBtn;
    private static final int REQUEST_LOGIN=1000;
    private String accName;
    private String urlDP;
    private TextView profileName;
    private ImageView profileImage;
    private TextView loginText;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
activity=this;
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        CheckSharedPreferance();

//Calling after login/signup
       Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String comingFrom="";
            comingFrom=bundle.getString("comingFrom");
            Log.d("cmng",comingFrom+"c");
            if(comingFrom!=null&&comingFrom.equals("login")){
                accName= UserProfile.getDisplayName();
                urlDP=UserProfile.getImageURL();
                updateUiInfo();
            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              FeedBackSuggestions();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       navigationView.setNavigationItemSelectedListener(this);
        if(savedInstanceState==null)
        displayFragment(1);

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        initialize();
        if(mGoogleApiClient!=null) {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            // Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            account = result.getSignInAccount();
            if (account != null) {
                UserProfile.setDisplayName(account.getDisplayName());
                UserProfile.setImageURL(account.getPhotoUrl().toString());
                //Log.d("img",account.getPhotoUrl()+"dd");
            }
        }
        }
    }*/
    private void initialize() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d("confild", connectionResult.toString());
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
       // Toast.makeText(activity,"j1j",Toast.LENGTH_SHORT).show();

        if (id == R.id.nav_home) {
            displayFragment(1);
        } else if (id == R.id.list_all_courier) {
            displayFragment(2);
        } else if (id == R.id.list_all_flights) {
            displayFragment(3);

        } else if (id == R.id.nav_ecommerce) {
            displayFragment(4);

        }

         else if (id == R.id.nav_share) {
            displayFragment(6);
        }
        else if (id == R.id.nav_set) {
            displayFragment(7);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    private void displayFragment(int id){
    Fragment fragment=null;
switch (id) {
    case 1:fragment = new HomeFragment();
        break;

    case 2:fragment = new CourierFragment();

        break;
    case 3:fragment = new FlightsFragment();
        break;

    case 4:fragment = new ECommerceFragment();
        break;

    case 6:ShareAppDownloadLink();
        break;

    case 7:fragment=new AppSettings();
        break;

    default:
            fragment = new HomeFragment();
}
    if(fragment!=null){
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.inc,fragment).commit();
    }

}

    /**
     * A method to handle the feedback suggestion functionlity
     */
    private void  FeedBackSuggestions() {
        final EditText suggestion = new EditText(activity);
        if (!FunctionTools.isConnected(activity)) {
            Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show();
            //Change toast to option for switching internet connection
        } else {
            new AlertDialog.Builder(activity)
                    .setTitle("FeedBack/Suggestions")
                    .setView(suggestion)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String msg = suggestion.getText().toString();
                            if (msg.length() == 0) {
                                suggestion.setError("Type your message here");
                                Toast.makeText(activity, "Message not sent", Toast.LENGTH_SHORT).show();
                            }
                            else
                                SendFeedback(msg);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
    //Call MySql sync
void SendFeedback(String msg){

        MYSQLHandler handler = new MYSQLHandler(activity);
        handler.SendMail(msg);
}
void CheckSharedPreferance(){

    SharedPreferences pref=getSharedPreferences("TRACKALL",MODE_PRIVATE);
    boolean frsttime=pref.getBoolean("FirstTime",true);
    if(frsttime) {
        SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
        editor.putBoolean("FirstTime",false);
        editor.commit();
        /*pref=getSharedPreferences("TRACKALL",MODE_PRIVATE);
        frsttime=pref.getBoolean("FirstTime",true);
       */ LoadLogoDialog();
    }
    //Log.d("loj",frsttime+"");
}
    private void LoadLogoDialog(){
        new AlertDialog.Builder(activity)
                .setTitle("Select if you want to load Logos also")
                .setMessage("You can also change the settings later")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
                        editor.putBoolean("LoadLogo",true);
                        editor.commit();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
                        editor.putBoolean("LoadLogo",false);
                        editor.commit();

                    }
                })
                .show();
    }

    private void ShareAppDownloadLink(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Download this amazing app trackAll and keep track of everything \n http://bit.ly/1R30Vtu";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Track All");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public void loginSignup(View view){

        String txt=((TextView)view).getText().toString();
        if(txt!=null&&txt.equals("Signup/Login")) {
            Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
            startActivity(intent);
        }
    }

    /**
     * Update name and profile picture of logged in user
     */
    private void updateUiInfo(){
       // Log.d("ff",accName+urlDP);

        View inflatedView=navigationView.getHeaderView(0);
        profileName = (TextView) inflatedView.findViewById(R.id.profileName);
        profileImage = (ImageView)inflatedView.findViewById(R.id.profileImage);
        loginText = (TextView)inflatedView.findViewById(R.id.login_txt);
if(profileImage!=null&&profileName!=null&&loginText!=null) {
    profileName.setText(accName);
    loginText.setText("Signout");
    loginText.setVisibility(View.INVISIBLE);
    Picasso.with(this).load(urlDP).transform(new CircularImage()).into(profileImage);
}
       Log.d("UI","Updated");

    }
}

