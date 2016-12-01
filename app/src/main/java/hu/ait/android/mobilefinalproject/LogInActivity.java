package hu.ait.android.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        firebaseAuth = FirebaseAuth.getInstance();
    }

    @OnClick(R.id.btnRegister)
    void registerClick() {
        if (!isFormValid()){
            return;
        }
        showProgressDialog();
//        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        hideProgressDialog();
//
//                        if (task.isSuccessful()) {
//                            FirebaseUser fbUser = task.getResult().getUser();
//
//                            fbUser.updateProfile(new UserProfileChangeRequest.Builder().
//                                    setDisplayName(usernameFromEmail(fbUser.getEmail())).build());
//
//                            User user = new User(usernameFromEmail(fbUser.getEmail()), fbUser.getEmail());
//                            databaseReference.child("users").child(fbUser.getUid()).setValue(user);
//
//                            Toast.makeText(LoginActivity.this, "User created", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }

    @OnClick(R.id.btnLogin)
    void loginClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();
//        firebaseAuth.signInWithEmailAndPassword(
//                etEmail.getText().toString(),
//                etPassword.getText().toString()
//        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
//
//                if (task.isSuccessful()){
//                    //open new activity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }


    private boolean isFormValid() {
        if(TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            return false;
        }

        return true;
    }
}
