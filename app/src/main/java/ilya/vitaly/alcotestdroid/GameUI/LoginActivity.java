package ilya.vitaly.alcotestdroid.GameUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ilya.vitaly.alcotestdroid.Entities.User;
import ilya.vitaly.alcotestdroid.R;

/**
 * Created by ilya on 07/11/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private TextView txtDetails;
    private EditText inputName, inputEmail;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private String userId, userName,userPass, userEmail;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText loginEmail, loginPass,regEmail, regPass, regName;
    private Button btnSignIn,btnSignOut,btnAddItems ,btnRegister,btnShowRegForm;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        // buttons ///////

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPass = (EditText) findViewById(R.id.login_password);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignOut = (Button) findViewById(R.id.sign_out_button);
        btnShowRegForm = (Button) findViewById(R.id.show_register_btn);


        mAuth = FirebaseAuth.getInstance();
        /// database ///
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseDatabase = database.getReference("Users");

//        myRef.setValue("Hello, World!");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail .getText().toString().trim();
                String pass = loginPass .getText().toString().trim();
                if(!email.equals("") && !pass.equals("")){
                    checkAuth(email,pass);
                }else{
                    toastMessage("You didn't fill in all the fields.");
                }
            }
        });

        btnShowRegForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.register_form).setVisibility(View.VISIBLE);
                btnRegister = (Button) findViewById(R.id.register_button);
                regEmail = (EditText) findViewById(R.id.reg_email);
                regPass = (EditText) findViewById(R.id.reg_password);
                regName = (EditText) findViewById(R.id.reg_name);
                btnRegister.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userEmail = regEmail.getText().toString().trim();
                        userPass = regPass.getText().toString().trim();
                        userName = regName.getText().toString().trim();

                        if(!userEmail.isEmpty() && !userPass.isEmpty() && !userName.isEmpty()){
                            createUser();
                        }else {
                            toastMessage("You didn't fill in all the fields.");
                        }
                    }
                });

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                toastMessage("Signing Out...");
            }
        });
    }

    private void createUser() {
        if(userEmail != null && userPass != null && userName != null)
            mAuth.createUserWithEmailAndPassword(userEmail, userPass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                               addUserToDB(new User(user.getUid(),user.getEmail(), userName), mFirebaseDatabase);
                                User localUser = new User (user.getUid(),user.getEmail(), userName);
//                                mFirebaseDatabase.child("users").child(localUser.getID()).setValue(user);
                               toastMessage("You have register " + user.getEmail());
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(context, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
    }

    private void addUserToDB(User user ,DatabaseReference myref) {
        myref.child("ID").child(user.getID()).setValue(user);
    }

    private void checkAuth(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()) {
//                            try {
//                                throw task.getException();
//                            } catch(FirebaseAuthWeakPasswordException e) {
//                                mPassword.setError("---------------WeakPass----------");
//                                mPassword.requestFocus();
//                            } catch(FirebaseAuthInvalidCredentialsException e) {
//                                mEmail.setError("---------------INVALID CRED---------");
//                                mEmail.requestFocus();
//                            } catch(FirebaseAuthUserCollisionException e) {
//                                mEmail.setError("---------------AUTH COLLISION---------");
//                                mEmail.requestFocus();
//                            } catch(Exception e) {
//                                Log.e(TAG, e.getMessage());
//                            }
//                        }

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(context, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            toastMessage(currentUser.getUid());
        } catch (Exception e) {
            mAuth.addAuthStateListener(mAuthListener);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //add a toast to show when successfully signed in
    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
