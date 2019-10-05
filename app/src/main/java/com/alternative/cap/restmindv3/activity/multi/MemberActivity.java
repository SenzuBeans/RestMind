package com.alternative.cap.restmindv3.activity.multi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.fragment.RegisterFragment;
import com.alternative.cap.restmindv3.activity.single.PhoneAuthActivity;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.rishabhkhanna.customtogglebutton.CustomToggleButton;

public class MemberActivity extends AppCompatActivity implements RegisterFragment.RegisterBtnClickListener {

    public static final int REQUIRE_CODE = 1152;
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private GoogleSignInClient mGoogleSignInClient;

    private EditText phoneNumberEditText;
    private Spinner countyDataSpinner;
    private Button loginBtn;
    private Button loginGuestBtn;
    private Button loginGoogleCloneBtn;
    private FrameLayout contentContainerMemberFragment;
    private SignInButton signInButton;

    private String json;
    private ArrayList<String> phoneCountyNameList;
    private ArrayList<String> phoneCountyCodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init(savedInstanceState);
        workbench(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        loginBtn = findViewById(R.id.loginBtn);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        countyDataSpinner = findViewById(R.id.countyDataSpinner);
        contentContainerMemberFragment = findViewById(R.id.contentContainerMemberFragment);
        mAuth = FirebaseAuth.getInstance();
        phoneCountyNameList = new ArrayList<>();
        phoneCountyCodeList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child("users");

        signInButton = findViewById(R.id.loginGoogleBtn);
        loginGuestBtn = findViewById(R.id.loginGuestBtn);
        loginGoogleCloneBtn = findViewById(R.id.loginGoogleCloneBtn);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void workbench(Bundle savedInstanceState) {
        mAuth.setLanguageCode("th");

        phoneAuth();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        loginGoogleCloneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        loginGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInAnonymously();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MemberActivity.this, "user : " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MemberActivity.this, NavigationHomePageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MemberActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MemberActivity.this, "user : " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MemberActivity.this, NavigationHomePageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void phoneAuth() {
        try {
            InputStream is = getAssets().open("CountyData.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("countries");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemObject = jsonArray.getJSONObject(i);

                phoneCountyNameList.add(itemObject.getString("name"));
                phoneCountyCodeList.add(itemObject.getString("code"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        countyDataSpinner.setAdapter(new ArrayAdapter<>(MemberActivity.this, android.R.layout.simple_spinner_dropdown_item, phoneCountyNameList));
        countyDataSpinner.setSelection(208);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = phoneCountyCodeList.get(countyDataSpinner.getSelectedItemPosition());
                String number = phoneNumberEditText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    phoneNumberEditText.setError("Pleace enter your phone number!");
                    phoneNumberEditText.requestFocus();
                    return;
                }

                Intent intent = new Intent(MemberActivity.this, PhoneAuthActivity.class);
                intent.putExtra("phoneCode", code);
                intent.putExtra("phoneNumber", number);
                startActivityForResult(intent, REQUIRE_CODE);
            }
        });
    }

    private void compareUserData(DataSnapshot dataSnapshot, Intent data) {
        int userCheck = 0;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            userCheck = currentUser.getUid().equals(userId(ds.toString())) ? 1 : 0;
            if (userCheck == 1) {
                break;
            }
        }
        if (userCheck == 1) {
            Intent intent = new Intent(MemberActivity.this, NavigationHomePageActivity.class);
            startActivity(intent);
            finish();
        } else {
            contentContainerMemberFragment.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainerMemberFragment, RegisterFragment.newInstance(data))
                    .commit();
        }
    }

    private String userId(String value) {
        int equalSymbol = 0;
        int commaSymbol = 0;
        for (int i = 0; i < value.length(); i++) {
            char s = value.charAt(i);
            if (s == '=' && equalSymbol == 0) {
                equalSymbol = i;
            }

            if (s == ',' && commaSymbol == 0) {
                commaSymbol = i;
                break;
            }
        }
        return value.substring(equalSymbol + 2, commaSymbol);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUIRE_CODE) {
            if (resultCode == RESULT_OK) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        compareUserData(dataSnapshot, data);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    @Override
    public void onRegisterClicked(Intent inputData) {
        Intent intent = new Intent(MemberActivity.this, NavigationHomePageActivity.class);
        intent.putExtra("userPhoneNumber", inputData.getExtras().getString("userPhoneNumber"));
        startActivity(intent);
        finish();
    }
}
