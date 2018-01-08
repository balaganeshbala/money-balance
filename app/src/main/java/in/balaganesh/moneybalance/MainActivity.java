package in.balaganesh.moneybalance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class Account implements Serializable {

    protected String accountName;
    protected double amount;
    protected int id;

    protected Account() {
        this.id = 0;
        this.accountName = new String();
        this.amount = 0;
    }

    protected Account(int id, String name, double amount) {
        this.id = id;
        this.accountName = name;
        this.amount = amount;
    }
}

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Account> accountArrayList;
    private List<Account> accountList;
    private AccountListAdapter adapter;
    private TextView totalAmountText;
    private DBHelper dbHelper;
    private TextView noAccountsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noAccountsText = (TextView) findViewById(R.id.no_accounts_text);
        listView = (ListView) findViewById(R.id.list);
        totalAmountText = (TextView) findViewById(R.id.total_amount_text);

        accountArrayList = new ArrayList<>();
        dbHelper = new DBHelper(this);
        accountList = dbHelper.getAllAccounts();
        accountArrayList.addAll(accountList);
        totalAmountText.setText("₹ " + getTotal());
        adapter = new AccountListAdapter(accountArrayList, this);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                Account account = accountList.get(i);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });

    }

    private double getTotal() {
        double total = 0.0;
        for (Account account:
             accountList) {
            total += account.amount;
        }
        return dbHelper.round(total, 2);
    }
    

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper != null) {
            accountList = dbHelper.getAllAccounts();
            if (accountList.size() == 0) {
                noAccountsText.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                noAccountsText.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            totalAmountText.setText("₹ " + getTotal());
            adapter.refreshAccounts(accountList);
        }
    }

    public void addNewAccount(View view) {
        Intent intent = new Intent(this, NewAccountActivity.class);
        startActivity(intent);
    }
}
