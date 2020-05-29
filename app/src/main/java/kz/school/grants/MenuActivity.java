package kz.school.grants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dk.view.folder.ResideMenu;
import com.dk.view.folder.ResideMenuItem;

import kz.school.grants.spec_menu.SpecFragment;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {


    public ResideMenu resideMenu;
    public ResideMenuItem specMenu, univerMenu, grantTypeMenu, hostelMenu, dormitoryMenu;
    public ResideMenuItem sendReportMenu, aboutUsMenu;
    public static Toolbar actionToolbar;
    SpecFragment specFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);
        setUpMenu();
        setupViews(savedInstanceState);
    }

    public void setupViews(Bundle savedInstanceState) {

        actionToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionToolbar);
        actionToolbar.setNavigationIcon(R.drawable.ic_home_white);
        actionToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });


        specFragment = new SpecFragment();


        if (savedInstanceState == null) {
            changeFragment(specFragment);
            setTitle("Мамандықтар");
        }

        checkInternetConnection();
    }

    public static void setTitle(String title) {
        actionToolbar.setTitle(title);
    }


    @Override
    public void onBackPressed() {

    }

    private void setUpMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.pr_icon);
//        resideMenu.setBackground(R.drawable.univer);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);

        resideMenu.setScaleValue(0.6f);

        specMenu = new ResideMenuItem(this, R.drawable.ic_home_white, getString(R.string.menu_mamandyktar));
        grantTypeMenu = new ResideMenuItem(this, R.drawable.ic_featured_list, getString(R.string.menu_granttar));
        univerMenu = new ResideMenuItem(this, R.drawable.ic_univer, getString(R.string.menu_univer_code));
        hostelMenu = new ResideMenuItem(this, R.drawable.icon_hostel, getString(R.string.menu_hostel));
        dormitoryMenu = new ResideMenuItem(this, R.drawable.ic_hostel, getString(R.string.menu_dormitory));
        sendReportMenu = new ResideMenuItem(this, R.drawable.ic_send_report, getString(R.string.menu_report));
        aboutUsMenu = new ResideMenuItem(this, R.drawable.ic_about, getString(R.string.menu_about));

        specMenu.setOnClickListener(this);
        grantTypeMenu.setOnClickListener(this);
        univerMenu.setOnClickListener(this);
        hostelMenu.setOnClickListener(this);
        dormitoryMenu.setOnClickListener(this);
        sendReportMenu.setOnClickListener(this);
        aboutUsMenu.setOnClickListener(this);

        resideMenu.addMenuItem(specMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(grantTypeMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(univerMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(hostelMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(dormitoryMenu, ResideMenu.DIRECTION_LEFT);

        resideMenu.addMenuItem(sendReportMenu, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(aboutUsMenu, ResideMenu.DIRECTION_RIGHT);
//        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

    }

    @Override
    public void onClick(View view) {

        if (view == specMenu) {
            changeFragment(specFragment);
            getSupportActionBar().setTitle("Мамандықтар");
//            actionToolbar.setNavigationIcon(R.drawable.ic_list_black_24dp);
        }
//        else if (view == univerMenu) {
//            changeFragment(new GroupsFragment());
//            getSupportActionBar().setTitle(getString(R.string.menu_groups));
//            actionToolbar.setNavigationIcon(R.drawable.ic_groups);
//
//        } else if (view == grantTypeMenu) {
//            changeFragment(new AboutUsFragment());
//            getSupportActionBar().setTitle(getString(R.string.menu_about_us));
//            actionToolbar.setNavigationIcon(R.drawable.ic_info_outline_black_24dp);
//
//        }

        resideMenu.closeMenu();
    }

    private boolean checkInternetConnection() {
        if (isNetworkAvailable()) {
            return true;

        } else {
            Toast.makeText(this, "Check inet connection", Toast.LENGTH_SHORT).show();
        }
        return false;
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
