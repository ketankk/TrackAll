package in.kuari.trackall.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import in.kuari.trackall.R;
import in.kuari.trackall.adapter.PagerAdapter;
import in.kuari.trackall.bean.UserProfile;
import in.kuari.trackall.databases.MYSQLHandler;
import in.kuari.trackall.gcm.GCM;
import in.kuari.trackall.utils.AppController;
import in.kuari.trackall.utils.CircularImage;
import in.kuari.trackall.utils.FunctionTools;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
private int displayFragment=1;
    private static final String TAG = "MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //if coming from on clicking URL open directly courier fragment
        Uri data=getIntent().getData();
        if(data!=null){
            displayFragment=2;
           /* String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String first = params.get(0);
//            String second = params.get(1);
            Log.d("s",scheme+"-"+host+"-"+first+"-");*/
        }


        setContentView(R.layout.activity_main);

        //Google Analytics starts
        analytics();
        //Google Analytics end

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activity = this;
        ViewPager viewPager= (ViewPager) findViewById(R.id.viewpager);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        TabLayout tabLayout= (TabLayout) findViewById(R.id.sliding_tabs);
tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_star_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_ecart);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_cart);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_flight_white_24dp);
        CheckSharedPreferance();

}
    Tracker mTracker;
    private void analytics(){
        new GCM(this).start();
        AppController appController= (AppController) getApplication();
        mTracker=appController.getDefaultTracker();
    }
    @Override
    public void onResume() {
        super.onResume();
        new GCM(this).start();

        mTracker.setScreenName(TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    @Override
    public void supportInvalidateOptionsMenu() {
        super.supportInvalidateOptionsMenu();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the BookMarkFragment/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                openSettingsActivity();
                break;

            case R.id.action_feedback:
                sendFeedback();
                break;
            case R.id.action_share:
                ShareAppDownloadLink();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    private void openSettingsActivity(){
        startActivity(new Intent(this,SettingsActivity.class));
    }

  /*  @SuppressWarnings("StatementWithEmptyBody")
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

        } else if (id == R.id.nav_share) {
            displayFragment(6);
        } else if (id == R.id.nav_set) {
            displayFragment(7);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
*/
   /* private void displayFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case 1:
                fragment = new BookMarkFragment();
                break;

            case 2:
                fragment = new CourierFragment();

                break;
            case 3:
                fragment = new FlightsFragment();
                break;

            case 4:
                fragment = new ECommerceFragment();
                break;

            case 6:
                ShareAppDownloadLink();
                break;



            default:
                fragment = new BookMarkFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.inc, fragment).commit();
        }

    }*/

    /**
     * A method to handle the feedback suggestion functionlity
     */
    private void FeedBackSuggestions() {

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
                            } else
                                SendFeedback(msg);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }

    //Call MySql sync
    void SendFeedback(String msg) {

        MYSQLHandler handler = new MYSQLHandler(activity);
        handler.SendMail(msg);
    }

    void CheckSharedPreferance() {

        SharedPreferences pref = getSharedPreferences("TRACKALL", MODE_PRIVATE);
        boolean frsttime = pref.getBoolean("FirstTime", true);
        if (frsttime) {
            SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
            editor.putBoolean("FirstTime", false);
            editor.commit();

        }
        //Log.d("loj",frsttime+"");
    }

    private void LoadLogoDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Select if you want to load Logos also")
                .setMessage("You can also change the settings later")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
                        editor.putBoolean("LoadLogo", true);
                        editor.commit();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("TRACKALL", MODE_PRIVATE).edit();
                        editor.putBoolean("LoadLogo", false);
                        editor.commit();

                    }
                })
                .show();
    }

    private void sendFeedback() {
        String emailId="sultanmirzadev@gmail.com";


        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for trackAll");

        Uri uri=Uri.parse("mailto:"+emailId);
        intent.setData(uri);
        activity.startActivity(intent);

    }

    private void ShareAppDownloadLink() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Download this amazing app trackAll and keep track of everything \n http://bit.ly/1R30Vtu";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "trackAll");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }



}

