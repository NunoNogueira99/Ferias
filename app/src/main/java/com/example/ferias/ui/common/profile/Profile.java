package com.example.ferias.ui.common.profile;

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
import com.example.ferias.data.InternalStorage;
import com.example.ferias.data.common.User;
import com.example.ferias.data.hotel_manager.HotelManager;
import com.example.ferias.data.simple_user.SimpleUser;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;

public class Profile extends Fragment {

    private User user;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;

    private ImageButton bt_Backhome_Profile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

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
                //viewPager.removeView((View) v.getParent());
                NavController navController = Navigation.findNavController(root);
                try {
                    user = (User) InternalStorage.readObject(getContext(), "User");
                    if(user instanceof SimpleUser){
                        navController.navigate(R.id.action_profile_to_simple_user_home);
                    }

                    if(user instanceof HotelManager){
                        navController.navigate(R.id.action_profile_to_hotel_manager_home);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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


}