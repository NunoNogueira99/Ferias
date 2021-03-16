package com.example.ferias.ui.simple_user.profile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ferias.R;


public class PaymentDetails extends Fragment {

    private TextView bt_OpenCreateCard;

    private LinearLayout ll_create_card;

    private EditText et_CardName;
    private TextView tv_CardName;

    private EditText et_CardNumber;
    private TextView tv_CardNumber;

    private EditText et_ExpirationDate;
    private TextView tv_ExpirationDate;

    private EditText et_CardCVC;
    private TextView tv_CardCVC;

    private Button bt_CreateCard;

    private LinearLayout ll_view_cards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.simple_user_fragment_profile_payment_details, container, false);

        inicialize_elements(root);

        clickListener();

        return root;
    }

    private void clickListener() {
        bt_OpenCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_create_card.getVisibility() == View.VISIBLE){
                    ll_create_card.setVisibility(View.GONE);
                    ll_view_cards.setVisibility(View.VISIBLE);
                    bt_OpenCreateCard.setText("Create card");
                }
                else{
                    ll_view_cards.setVisibility(View.GONE);
                    ll_create_card.setVisibility(View.VISIBLE);
                    bt_OpenCreateCard.setText("Cancel");
                }
            }
        });

        bt_CreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean correct = false;

                String name = et_CardName.getText().toString().trim();
                String number = et_CardNumber.toString().trim();
                String cvc = et_ExpirationDate.getText().toString().trim();
                String date = et_CardCVC.getText().toString().trim();

                if(name.isEmpty() || name.length() <= 6){
                    tv_CardName.setTextColor(Color.RED);
                }
                else{
                    tv_CardName.setTextColor(Color.parseColor("#E1FFFFFF"));
                }

                if(number.isEmpty()){
                    tv_CardNumber.setTextColor(Color.RED);
                }
                else{
                    tv_CardNumber.setTextColor(Color.parseColor("#E1FFFFFF"));
                }

                if(date.isEmpty()){
                    tv_ExpirationDate.setTextColor(Color.RED);
                }
                else{
                    tv_ExpirationDate.setTextColor(Color.parseColor("#E1FFFFFF"));
                }

                if(cvc.isEmpty() || cvc.length() >= 4){
                    tv_CardCVC.setTextColor(Color.RED);
                }
                else{
                    tv_CardCVC.setTextColor(Color.parseColor("#E1FFFFFF"));
                }
            }
        });
    }

    private void inicialize_elements(View root) {
        bt_OpenCreateCard = root.findViewById(R.id.bt_OpenCreateCard);
        bt_OpenCreateCard.setText("Create card");

        ll_create_card = root.findViewById(R.id.ll_create_card);
        ll_create_card.setVisibility(View.GONE);

        et_CardName = root.findViewById(R.id.et_CardName);
        tv_CardName = root.findViewById(R.id.tv_CardName);

        et_CardNumber = root.findViewById(R.id.et_CardNumber);
        tv_CardNumber = root.findViewById(R.id.tv_CardNumber);

        et_ExpirationDate = root.findViewById(R.id.et_ExpirationDate);
        tv_ExpirationDate = root.findViewById(R.id.tv_ExpirationDate);

        et_CardCVC = root.findViewById(R.id.et_CardCVC);
        tv_CardCVC = root.findViewById(R.id.tv_CardCVC);

        bt_CreateCard = root.findViewById(R.id.bt_CreateCard);

        ll_view_cards = root.findViewById(R.id.ll_view_cards);
    }
}