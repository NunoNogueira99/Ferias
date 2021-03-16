package com.example.ferias.ui.simple_user.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.ferias.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Profile extends Fragment {

    private FirebaseAuth mAuth;

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    private ImageButton bt_Backhome_Profile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.simple_user_fragment_profile, container, false);

        viewPager = root.findViewById(R.id.pager);
        tabLayout = root.findViewById(R.id.tab_layout);

        pageAdapter = new PageAdapter (getParentFragmentManager());

        pageAdapter.addFragment(new PersonalData(),"Personal Data");
        pageAdapter.addFragment(new Preferences(), "Preferences");
        pageAdapter.addFragment(new Security(), "Security");
        pageAdapter.addFragment(new PaymentDetails(), "Payment Details");

        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

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

        bt_Backhome_Profile = root.findViewById(R.id.bt_Backhome_Profile);
        bt_Backhome_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.removeView((View) v.getParent());
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_simple_user_profile_to_simple_user_home);
            }
        });
       /* mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userprofile = snapshot.getValue(User.class);

                if (userprofile != null) {
                    et_FullName.setText(userprofile.get_Name());
                    //et_Age.setText(userprofile.get_Birthday().toString());
                    et_Phone.setText(userprofile.get_Phone());
                    et_EmailAddress.setText(userprofile.get_Email());
                    et_Password.setText(userprofile.get_Password());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something wrong happened!", Toast.LENGTH_LONG).show();
            }
        });*/

        return root;
    }
}