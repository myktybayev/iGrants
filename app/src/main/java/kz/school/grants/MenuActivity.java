package kz.school.grants;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dk.view.folder.ResideMenu;
import com.dk.view.folder.ResideMenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import kz.school.grants.about_us_menu.AboutUsFragment;
import kz.school.grants.advice_menu.AdviceFragment;
import kz.school.grants.authentication.LoginByEmailPage;
import kz.school.grants.granttar_menu.GranttarFragment;
import kz.school.grants.hostels.HostelsFragment;
import kz.school.grants.news_menu.NewsFragment;
import kz.school.grants.spec_menu.SpecFragment;
import kz.school.grants.univer_menu.UniversFragment;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    public ResideMenu resideMenu;
    public ResideMenuItem specMenu, univerMenu, grantTypeMenu, hostelMenu, newsMenu;
    public ResideMenuItem sendReportMenu, aboutUsMenu;
    public static Toolbar actionToolbar;
    SpecFragment specFragment;
    NewsFragment newsFragment;
    AboutUsFragment aboutUsFragment;
    AdviceFragment adviceFragment;
    HostelsFragment hostelsFragment;
    UniversFragment universFragment;
    GranttarFragment granttarFragment;
    FirebaseAuth mAuth;
    boolean adminSigned = false;
    ImageView loginImage;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        setUpMenu();
        setupViews(savedInstanceState);
    }

    public void setupViews(Bundle savedInstanceState) {

        actionToolbar = findViewById(R.id.toolbar);
//        loginImage = findViewById(R.id.loginImage);

        setSupportActionBar(actionToolbar);
        actionToolbar.setNavigationIcon(R.drawable.ic_home_white);
        actionToolbar.setNavigationOnClickListener(view -> resideMenu.openMenu(ResideMenu.DIRECTION_LEFT));

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            adminSigned = true;
        }

        specFragment = new SpecFragment();
        newsFragment = new NewsFragment();
        aboutUsFragment = new AboutUsFragment();
        adviceFragment = new AdviceFragment();
        hostelsFragment = new HostelsFragment();
        universFragment = new UniversFragment();
        granttarFragment = new GranttarFragment();

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(getString(R.string.menu_mamandyktar));
            changeFragment(specFragment);
        }

        checkInternetConnection();
        addListenerToLogOut();

//        if(adminSigned) loginImage.setImageDrawable(getDrawable(R.drawable.ic_exit_to_app));

        /*
        loginImage.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MenuActivity.this, LoginByEmailPage.class));
        });
        */
    }

    public static void setTitle(String title) {
        actionToolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {

    }

    public void addListenerToLogOut() {
        mDatabaseRef.child("logOutAll").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && !isAdmin()) {
                    Toast.makeText(MenuActivity.this, "Мұғалімдердің пароль өзгерді, жасаушыларға хабарласуыңызды сұраймыз!", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MenuActivity.this, LoginByEmailPage.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_back);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        resideMenu.setScaleValue(0.6f);

        newsMenu = new ResideMenuItem(this, R.drawable.ic_news, getString(R.string.menu_news));
        specMenu = new ResideMenuItem(this, R.drawable.ic_home_white, getString(R.string.menu_mamandyktar));
        grantTypeMenu = new ResideMenuItem(this, R.drawable.ic_featured_list, getString(R.string.menu_granttar));
        univerMenu = new ResideMenuItem(this, R.drawable.ic_univer, getString(R.string.menu_univer_code));
        hostelMenu = new ResideMenuItem(this, R.drawable.icon_hostel, getString(R.string.menu_hostel));
        sendReportMenu = new ResideMenuItem(this, R.drawable.ic_send_report, getString(R.string.menu_report));
        aboutUsMenu = new ResideMenuItem(this, R.drawable.ic_about, getString(R.string.menu_about));

        newsMenu.setOnClickListener(this);
        specMenu.setOnClickListener(this);
        grantTypeMenu.setOnClickListener(this);
        univerMenu.setOnClickListener(this);
        hostelMenu.setOnClickListener(this);
        sendReportMenu.setOnClickListener(this);
        aboutUsMenu.setOnClickListener(this);

        resideMenu.addMenuItem(newsMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(specMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(grantTypeMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(univerMenu, ResideMenu.DIRECTION_LEFT);
//        resideMenu.addMenuItem(hostelMenu, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(sendReportMenu, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(aboutUsMenu, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

    }

    @Override
    public void onClick(View view) {

        if (view == specMenu) {
            changeFragment(specFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_mamandyktar));
        } else if (view == newsMenu) {
            changeFragment(newsFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_news));
            actionToolbar.setNavigationIcon(R.drawable.ic_news);

        } else if (view == aboutUsMenu) {
            changeFragment(aboutUsFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_about));
            actionToolbar.setNavigationIcon(R.drawable.ic_about);

        } else if (view == sendReportMenu) {
            changeFragment(adviceFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_report));
            actionToolbar.setNavigationIcon(R.drawable.ic_send_report);

        } else if (view == univerMenu) {
            changeFragment(universFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_univer_code));
        } else if (view == hostelMenu) {
            changeFragment(hostelsFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_hostel));
        } else if (view == grantTypeMenu) {
            changeFragment(granttarFragment);
            getSupportActionBar().setTitle(getString(R.string.menu_granttar));
        }

//        else if (view == grantTypeMenu) {
//            changeFragment(new AboutUsFragment());
//            getSupportActionBar().setTitle(getString(R.string.menu_about_us));
//            actionToolbar.setNavigationIcon(R.drawable.ic_info_outline_black_24dp);
//
//        }

        resideMenu.closeMenu();
    }

    private void checkInternetConnection() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.check_inet), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
//            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            //Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment) {
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }
}
