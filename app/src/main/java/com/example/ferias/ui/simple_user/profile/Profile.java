package com.example.ferias.ui.simple_user.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ferias.R;
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.simple_user.User;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class Profile extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private User user;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    private ImageButton bt_Backhome_Profile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.simple_user_fragment_profile, container, false);

        readUserData();

        initializeElements(root);

        clickListeners(root);

        return root;
    }


    private void initializeElements(View root) {
        viewPager = root.findViewById(R.id.pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        pageAdapter = new PageAdapter (getParentFragmentManager());

        pageAdapter.addFragment(new PersonalData(),"Personal Data");
        pageAdapter.addFragment(new Preferences(), "Preferences");
        pageAdapter.addFragment(new Security(), "Security");

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        bt_Backhome_Profile = root.findViewById(R.id.bt_Backhome_Profile);
    }

    private void clickListeners(View root) {

        bt_Backhome_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.removeView((View) v.getParent());
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_profile_simpleuser_to_home_simple_user);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void readUserData() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                Log.e("User",user.toString());
                if(user == null){
                    Log.e("ERROR", "User"+firebaseUser.getUid()+"is unexpectedly null");
                    Toast.makeText(getContext(),"Error: could not fetch user", Toast.LENGTH_LONG).show();
                }
                try {
                    InternalStorage.writeObject(getContext(), "User", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);

                try {
                    InternalStorage.writeObject(getContext(), "User", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR", "getUser:onCancelled",error.toException());
            }
        });
    }


}