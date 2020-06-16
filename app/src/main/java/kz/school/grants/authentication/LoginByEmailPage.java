package kz.school.grants.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.Nullable;

import kz.school.grants.MenuActivity;
import kz.school.grants.R;


public class LoginByEmailPage extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText email;
    EditText password;
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        initWidgets();
        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this);
    }

    public void initWidgets() {
        btnLogin = findViewById(R.id.btnLogin);
        email = findViewById(R.id.emailToLogin);
        password = findViewById(R.id.passwordToLogin);
        progressBar = findViewById(R.id.progressBarForLogin);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        final String emails = email.getText().toString();
        String passwords = password.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);

        if (TextUtils.isEmpty(emails) || TextUtils.isEmpty(passwords)) {

            Snackbar.make(btnLogin, getString(R.string.fill_all), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);

        } else {
            auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(LoginByEmailPage.this, MenuActivity.class);
                            startActivity(intent);

                        } else {

                            String subbed = getString(R.string.login_or_pass_error);
                            Snackbar.make(btnLogin, subbed, Toast.LENGTH_SHORT).setActionTextColor(getResources().getColor(R.color.red)).show();
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);

                        }
                    });
        }
    }
}
