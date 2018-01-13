package in.balaganesh.moneybalance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class Account implements Serializable {

    protected String accountName;
    protected BigDecimal amount;
    protected int id;

    protected Account() {
        this.id = 0;
        this.accountName = new String();
        this.amount = new BigDecimal("0.0");
    }

    protected Account(int id, String name, String amount) {
        this.id = id;
        this.accountName = name;
        this.amount = new BigDecimal(amount);
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

    private BigDecimal getTotal() {
        BigDecimal total = new BigDecimal(0.0);
        for (Account account:
             accountList) {
            total = total.add(account.amount);
        }
        return dbHelper.round(total);
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
