package com.amanaggarwal1.flashchat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.text.TextUtils.isEmpty;


public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
            finish();
            startActivity(intent);
        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    // Executed when Sign in button pressed
    public void signInExistingUser(View v)   {
        attemptLogin();
    }

    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.amanaggarwal1.flashchat.RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(isEmpty(email)){
            mEmailView.setError(getString(R.string.error_field_required));
            return;
        }
        if(isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            return;
        }
        Toast.makeText(this, "LogIn in process", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(getString(R.string.logcat), "signIn onComplete : " + task.isSuccessful());

                if(task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Welcome " + getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }else{
                    String error = task.getException().getMessage();
                    showErrorDialog(error);
                    Log.d(getString(R.string.logcat), "Problem in signing in : " + error);

                }
            }
        });

    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private String getUsername(){
        return "";
    }
}