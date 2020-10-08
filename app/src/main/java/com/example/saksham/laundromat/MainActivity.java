package com.example.saksham.laundromat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saksham.laundromat.fragments.ContactusFragment;
import com.example.saksham.laundromat.fragments.HomeFragment;
import com.example.saksham.laundromat.fragments.LostFragment;
import com.example.saksham.laundromat.fragments.PrevFragment;
import com.example.saksham.laundromat.fragments.RegisterFragment;
import com.example.saksham.laundromat.fragments.TrackFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient mGoogleApiClient;

    private final String HOME="Home";
    private final String TRACK ="Track";
    private final String CONTACTUS ="Contact Us";
    private final String LOST ="Lost";
    private final String REGISTER ="Register";
    private final String PREVIOUS ="Previous Washes";
    HomeFragment homeFragment;
    PrevFragment prevFragment;
    TrackFragment trackFragment;
    ContactusFragment contactusFragment;
    LostFragment lostFragment;
    RegisterFragment registerFragment;


    NavigationView navigationView;
    private void initializeFragments() {
        homeFragment=new HomeFragment();
        prevFragment=new PrevFragment();
        trackFragment=new TrackFragment();
        contactusFragment=new ContactusFragment();
        lostFragment=new LostFragment();
        registerFragment=new RegisterFragment();
        navigationView.getMenu().performIdentifierAction(R.id.nav_home,0);
        navigationView.setCheckedItem(R.id.nav_home);
        setTitle(HOME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .build();

        mGoogleApiClient.connect();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeFragments();
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
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        String error = connectionResult.getErrorMessage();
        Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment=null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment=homeFragment;
            MainActivity.super.setTitle(HOME);

        }
        if (id == R.id.nav_washes) {
            // Handle the camera action
            fragment=prevFragment;
            MainActivity.super.setTitle(PREVIOUS);

        }
        if (id == R.id.nav_contacts) {
            // Handle the camera action
            fragment=contactusFragment;
            MainActivity.super.setTitle(CONTACTUS);

        }if (id == R.id.nav_lostFound) {
            // Handle the camera action
            fragment=lostFragment;
            MainActivity.super.setTitle(LOST);

        }if (id == R.id.nav_rPlan) {
            // Handle the camera action
            fragment=registerFragment;
            MainActivity.super.setTitle(REGISTER);

        }
        if (id == R.id.nav_trackWash) {
            // Handle the camera action
            fragment=trackFragment;
            MainActivity.super.setTitle(TRACK);

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if(fragment!=null)
        {
            FragmentManager fragmentManager= getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
        }
        return true;
    }
}
