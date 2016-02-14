package in.kuari.trackall.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;

import in.kuari.trackall.R;
import in.kuari.trackall.databases.MYSQLHandler;
import in.kuari.trackall.fragments.CourierFragment;
import in.kuari.trackall.fragments.ECommerceFragment;
import in.kuari.trackall.fragments.FlightsFragment;
import in.kuari.trackall.fragments.HomeFragment;
import in.kuari.trackall.utils.FunctionTools;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private WebView webView;
Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
activity=this;
        CheckSharedPreferance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Suggestion/Feedback coming soon..", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
               */FeedBackSuggestions();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       navigationView.setNavigationItemSelectedListener(this);
if(savedInstanceState==null)
displayFragment(1);

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
        getMenuInflater().inflate(R.menu.main, menu);
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
            displayFragment(5);

        } else if (id == R.id.nav_share) {
            displayFragment(5);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
void displayFragment(int id){
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
    case 5:fragment = new ECommerceFragment();
        break;
    default:
            fragment = new HomeFragment();
}
    if(fragment!=null){
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.inc,fragment).commit();
    }

}
    void getScreenShot(){
        FunctionTools b=new FunctionTools(activity);
    b.takeScreenShot("abc");
    }

    void  FeedBackSuggestions(){
        final EditText suggestion=new EditText(activity);

        new AlertDialog.Builder(activity)
                .setTitle("FeedBack/Suggestions")
                .setView(suggestion)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String msg=suggestion.getText().toString();
                        if(msg.length()==0)
                            suggestion.setError("Type your message here");
                        else
                        SendFeedback(msg);
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }
    //Call MySql sync
void SendFeedback(String msg){
    if(!FunctionTools.isConnected(activity))
    {
        Toast.makeText(activity,"No Internet Connection",Toast.LENGTH_SHORT).show();
        //Change toast to option for switching internet connection
    }
    else {
        MYSQLHandler handler = new MYSQLHandler(activity);
        handler.SendMail(msg);

    }
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
    Log.d("loj",frsttime+"");
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
}

