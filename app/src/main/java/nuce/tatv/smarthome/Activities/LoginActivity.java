package nuce.tatv.smarthome.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nuce.tatv.smarthome.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvForgotPassword, tvRegister;
    private EditText edtUser, edtPass;
    private Button btnLogin;
    private CheckBox cbRemember;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        init();

        setListeners();
    }

    public void init() {
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRemember);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);


        sharedPreferences = getSharedPreferences("dataLogin", MODE_PRIVATE);
        //get data to sharedFreferences
        edtUser.setText(sharedPreferences.getString("user", ""));
        edtPass.setText(sharedPreferences.getString("pass", ""));
        cbRemember.setChecked(sharedPreferences.getBoolean("checked", false));
    }
    private void setListeners() {
        tvForgotPassword.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                checkValidation();
                break;
            case R.id.tvForgotPassword:
//                Intent itForgotPassword = new Intent(this, ForgotPasswordActivity.class);
//                startActivity(itForgotPassword);
                break;
            case R.id.tvRegister:
//                Intent itRegister = new Intent(this, RegisterActivity.class);
//                startActivity(itRegister);
                break;
        }
    }

    private void checkValidation() {
        final String username = edtUser.getText().toString();
        final String password = edtPass.getText().toString();
        if (username.equals("")) {
            edtUser.setError("Username must input");
            edtUser.requestFocus();
        } else if (password.equals("")) {
            edtPass.setError("Password must input");
            edtPass.requestFocus();
        } else {
            if (username.equals("admin") && password.equals("admin")){
                if (cbRemember.isChecked()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("user", username);
                    editor.putString("pass", password);
                    editor.putBoolean("checked", true);
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("user");
                    editor.remove("pass");
                    editor.remove("checked");
                    editor.commit();
                }
//                Toast.makeText(getApplication(), "Login success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", username);
                intent.putExtra("email", "admin@gmail.com");
                startActivity(intent);
            }else {
                Toast.makeText(getApplication(), "Username or Password not match!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
