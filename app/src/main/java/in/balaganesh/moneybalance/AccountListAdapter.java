package in.balaganesh.moneybalance;

import android.content.ClipData;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by balaganesh on 06/01/18.
 */

class AccountListAdapter extends ArrayAdapter<Account>{

    private List<Account> accounts;

    public AccountListAdapter(ArrayList<Account> data, Context context) {
        super(context, R.layout.account_list_row, data);
        this.accounts = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.account_list_row, null);
        }

        Account account = getItem(position);

        if (account != null) {
            TextView accountText = (TextView) view.findViewById(R.id.account_name_text);
            TextView amountText = (TextView) view.findViewById(R.id.amount_text);

            if (accountText != null) {
                accountText.setText(account.accountName);
            }

            if (amountText != null) {
                amountText.setText("₹ " + account.amount);
            }
        }

        return view;
    }

    protected void refreshAccounts(List<Account> events) {
        this.accounts.clear();
        this.accounts.addAll(events);
        notifyDataSetChanged();
    }
}
