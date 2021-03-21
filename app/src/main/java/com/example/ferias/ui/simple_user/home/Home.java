package com.example.ferias.ui.simple_user.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ferias.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Home extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private GoogleSignInClient googleSignInClient;

    private ShapeableImageView bt_ProfileMenu;
    private LinearLayout profile_menu;
    private Button bt_editProfile, bt_Logout;

    ShapeableImageView profile_pic;
    ExtendedFloatingActionButton bookings;
    FloatingActionButton favs;

    TextInputLayout date_selection;
    TextInputEditText date;

    MaterialDatePicker.Builder builder;
    MaterialDatePicker materialDatePicker;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.simple_user_fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser != null){
            googleSignInClient = GoogleSignIn.getClient(getActivity(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        }
        bt_ProfileMenu = root.findViewById(R.id.bt_ProfileMenu);

        profile_menu = root.findViewById(R.id.profile_menu);
        profile_menu.setVisibility(View.GONE);

        bt_editProfile = root.findViewById(R.id.bt_editProfile);
        bt_Logout = root.findViewById(R.id.bt_Logout);

        profile_pic = root.findViewById(R.id.bt_ProfileMenu);
        profile_pic.setShapeAppearanceModel(profile_pic.getShapeAppearanceModel().toBuilder().setAllCorners(CornerFamily.ROUNDED,32).build());

        bookings = root.findViewById(R.id.my_bookings_btn);
        favs = root.findViewById(R.id.my_favs_btn);

        date_selection = root.findViewById(R.id.textinput_date_selection);
        date = root.findViewById(R.id.textinput_date);
        date.setInputType(InputType.TYPE_NULL);

        builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date");
        materialDatePicker = builder.setTheme(R.style.CustomDatePicker).build();

        clickListener(root);

        return root;
    }

    private void clickListener(View root) {

        ConstraintLayout cl_Home = root.findViewById(R.id.cl_Home);

        cl_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile_menu.setVisibility(View.GONE);
            }
        });

        bt_ProfileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profile_menu.getVisibility() == View.GONE){
                    profile_menu.setVisibility(View.VISIBLE);
                }
                else{
                    profile_menu.setVisibility(View.GONE);
                }

            }
        });

        bt_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_simple_user_home_to_simple_user_home_profile);
            }
        });

        bt_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                boolean googleLogin = sharedPref.getBoolean("GoogleLogin", false);

                if(googleLogin == true){
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                firebaseAuth.signOut();
                            }
                        }
                    });
                }
                else{
                    firebaseAuth.signOut();
                }
                
                NavController navController = Navigation.findNavController(root);
                navController.navigate(R.id.action_simple_user_home_to_home_main);
            }
        });

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                date.setText(materialDatePicker.getHeaderText());
            }
        });

    }
}