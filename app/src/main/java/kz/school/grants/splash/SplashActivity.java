package kz.school.grants.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import kz.school.grants.MenuActivity;
import kz.school.grants.R;
import kz.school.grants.authentication.LoginByEmailPage;

public class SplashActivity extends AppCompatActivity {

    Animation animation;
    ImageView imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageView2 = findViewById(R.id.imageView2);

        animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        imageView2.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginByEmailPage.class));
            }
        }, 3000);
    }
}