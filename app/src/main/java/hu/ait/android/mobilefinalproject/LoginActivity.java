package hu.ait.android.mobilefinalproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hu.ait.android.mobilefinalproject.adapter.IconImageAdapter;
import hu.ait.android.mobilefinalproject.fragments.BaseFragment;
import hu.ait.android.mobilefinalproject.fragments.SetLocationDialogFragment;
import hu.ait.android.mobilefinalproject.model.User;


/**
 * LoginActivity.java
 *
 * Created by Carolyn Ryan
 * 11/29/2016
 *
 * LoginActivity for user login, uses firebase
 */
public class LoginActivity extends BaseActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnLogin;
    private ImageView accountIcon;
    private int icon;

    private IconImageAdapter iconImageAdapter;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        findViewsByIDs();

    }

    private void findViewsByIDs() {
        accountIcon = (ImageView) findViewById(R.id.ivAccountIcon);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        findBtnRegister();
        findBtnLogin();
    }

    private void findBtnLogin() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginClick();
            }
        });
    }

    private void findBtnRegister() {
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterClick();
            }
        });
    }

    public void btnRegisterClick() {
        if (!isFormValid()) {
            return;
        }
        showProgressDialog();
        firebaseCreateUser();
    }

    private void firebaseCreateUser() {
        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    createUser(task);
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                actionsOnFailure(e);
            }
        });
    }

    private void actionsOnFailure(@NonNull Exception e) {
        hideProgressDialog();
        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    private void createUser(@NonNull Task<AuthResult> task) {
        FirebaseUser fbUser = getFirebaseUser(task);
        User.UserIcon userIcon = getUserIcon();

        User user = new User(fbUser.getEmail(), usernameFromEmail(fbUser.getEmail()), userIcon, getString(R.string.budapest));

        databaseReference.child("users").child(fbUser.getUid()).setValue(user);
        Toast.makeText(LoginActivity.this, getString(R.string.user_created), Toast.LENGTH_SHORT).show();
    }

    private User.UserIcon getUserIcon() {
        int onRegisterIcon = showChooseIconGridDialog();
        return User.UserIcon.toUserIconFromId(onRegisterIcon);
    }

    @NonNull
    private FirebaseUser getFirebaseUser(@NonNull Task<AuthResult> task) {
        FirebaseUser fbUser = task.getResult().getUser();
        fbUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(usernameFromEmail(fbUser.getEmail())).build());
        return fbUser;
    }

    private int showChooseIconGridDialog() {
        final Dialog userIconDialog = getIconDialog();
        setUpGridView(userIconDialog);
        return icon;


    }

    private void setUpGridView(final Dialog userIconDialog) {
        GridView iconGridView= (GridView)userIconDialog.findViewById(R.id.grid);

        iconImageAdapter = new IconImageAdapter(this);
        iconGridView.setAdapter(iconImageAdapter);
        iconGridView.setNumColumns(3);
        iconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setUserIcon(parent, view, position);
                userIconDialog.dismiss();
                showLocationDialog();
            }
        });
    }

    private void setUserIcon(AdapterView<?> parent, View view, int position) {
        icon = iconImageAdapter.getDrawableID(position, view, parent);
        String iconID = User.UserIcon.fromIconId(icon);

        accountIcon = (ImageView) iconImageAdapter.getView(position, view, parent);
        accountIcon.setImageResource(icon);

        DatabaseReference iconRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(BaseFragment.getUid()).child("icon");
        iconRef.setValue(iconID);
    }

    @NonNull
    private Dialog getIconDialog() {
        final Dialog userIconDialog = new Dialog(this);
        userIconDialog.setContentView(R.layout.icon_grid_dialog);
        userIconDialog.setTitle(getString(R.string.choose_icon));
        userIconDialog.show();
        return userIconDialog;
    }

    private void showLocationDialog() {

        SetLocationDialogFragment setLoc = new SetLocationDialogFragment();
        setLoc.show(getSupportFragmentManager(), SetLocationDialogFragment.TAG);

    }

    public void btnLoginClick() {
        if (!isFormValid()) {
            return;
        }
        showProgressDialog();
        firebaseAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                handleFirebaseLogin(task);
            }
        });

    }

    private void handleFirebaseLogin(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            startActivity(new Intent(LoginActivity.this, NavDrawerActivity.class));
            finish();
        } else {
            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String usernameFromEmail(String email) {
        if (email.contains(getString(R.string.at))) {
            return email.split(getString(R.string.at))[0];
        } else {
            return email;
        }
    }


    private boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.required));
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.required));
            return false;
        }

        return true;
    }

    public void addUserLocation(String location) {
        DatabaseReference iconRef = FirebaseDatabase.getInstance()
                .getReference().child("users").child(BaseFragment.getUid()).child("location");
        iconRef.setValue(location);
    }
}
