package com.alternative.cap.restmindv3.fragment;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.util.BreathLogItem;
import com.alternative.cap.restmindv3.util.StepLogItem;
import com.alternative.cap.restmindv3.util.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    static RegisterListener listener;

    private DatabaseReference databaseReference;
    private DatabaseReference reference;
    private FirebaseUser user;
    private UserDetails userDetails;

    private Button registerBtn;
    private EditText userNameEditText;
    private EditText emailEditText;

    public RegisterFragment() {
    }

    public static RegisterFragment newInstance(RegisterListener passingListener) {
        Bundle args = new Bundle();
        listener = passingListener;
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        reference = databaseReference.child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initInstance(rootView, savedInstanceState);
        workbench(rootView, savedInstanceState);
        return rootView;
    }

    private void initInstance(View rootView, Bundle savedInstanceState) {
        registerBtn = rootView.findViewById(R.id.registerBtn);
        userNameEditText = rootView.findViewById(R.id.userNameEditText);
        emailEditText = rootView.findViewById(R.id.emailEditText);
    }

    private void workbench(View rootView, Bundle savedInstanceState){
        userNameEditText.requestFocus();
        
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                if (userName.isEmpty() || (userName.length() < 5 || userName.length() > 36)){
                    userNameEditText.setError("user name must have 5 to 36 character");
                    userNameEditText.requestFocus();
                    return;
                }else if (email.isEmpty()){
                    emailEditText.setError("Please enter email!");
                    emailEditText.requestFocus();
                    return;
                }
                ArrayList<BreathLogItem> breathLog = new ArrayList<>();
                ArrayList<StepLogItem> stepLog = new ArrayList<>();

                userDetails = new UserDetails(userName, email);
                userDetails.setBreath_log(breathLog);
                userDetails.setStep_log(stepLog);

                reference.child(user.getUid()).setValue(userDetails);


                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(userName).build();

                user.updateEmail(email);
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    listener.onRegis();
                                    getChildFragmentManager().popBackStack();
                                }
                            }
                        });
            }
        });
    }

    public interface RegisterListener{
        void onRegis();
    }

}
