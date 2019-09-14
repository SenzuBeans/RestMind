package com.alternative.cap.restmindv3.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alternative.cap.restmindv3.R;
import com.alternative.cap.restmindv3.activity.multi.MemberActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingFragment extends Fragment {

    private TextView userName;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        workbench(root, savedInstanceState);
        return root;
    }

    private void workbench(View root, Bundle savedInstanceState) {
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yy hh:mm:ss");
        String format = s.format(new Date());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = databaseReference.child(user.getUid());
//        reference.child("last-login").setValue(format);

        userName =  root.findViewById(R.id.userNameTv);
        userName.setText(user.getDisplayName());

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userName.setText(dataSnapshot.child("name").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        root.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth.getInstance().signOut();
                    getActivity().startActivity(new Intent(getContext(), MemberActivity.class));
                    getActivity().finish();
                }
            }
        });

    }

    
}