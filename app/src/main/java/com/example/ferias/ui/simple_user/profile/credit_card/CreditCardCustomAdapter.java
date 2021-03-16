package com.example.ferias.ui.simple_user.profile.credit_card;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.ferias.R;
import com.example.ferias.data.simple_user.CreditCard;

import java.util.Calendar;
import java.util.List;

public class CreditCardCustomAdapter implements ListAdapter {

    private List<CreditCard> arrayList;
    private Context context;

    public CreditCardCustomAdapter(Context context, List<CreditCard> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CreditCard subjectData = arrayList.get(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.fragment_payment_details_card_view, null);

            TextView tv_CreditCard_Number = convertView.findViewById(R.id.tv_CreditCard_Number);
            TextView tv_CreditCard_Date = convertView.findViewById(R.id.tv_CreditCard_Date);
            TextView bt_CreditCard_Delete = convertView.findViewById(R.id.bt_CreditCard_Delete);

            String cardnumber = subjectData.get_Number();
            tv_CreditCard_Number.setText(cardnumber.substring(cardnumber.length() - 4));
            tv_CreditCard_Date.setText(subjectData.get_Date());

            bt_CreditCard_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    arrayList.remove(position);
                }
            });
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
