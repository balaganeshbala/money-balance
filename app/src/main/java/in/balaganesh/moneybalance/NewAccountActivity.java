package in.balaganesh.moneybalance;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class NewAccountActivity extends AppCompatActivity {

    private EditText accountField;
    private EditText amountField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        accountField = (EditText) findViewById(R.id.account_name_edit_text);
        amountField = (EditText) findViewById(R.id.amount_edit_text);
    }

    public void addAccount(View view) {
        String accountValue = accountField.getText().toString();
        String amountValue = amountField.getText().toString();

        if (amountValue.trim().length() > 0 && !amountValue.equals(".") && Double.parseDouble(amountValue) != 0.0) {
            Account newAccount = new Account(0, accountValue, Double.parseDouble(amountValue));

            DBHelper dbHelper = new DBHelper(this);
            dbHelper.addAccount(newAccount);
            finish();
        }
    }

    public void goBack(View view) {
        this.finish();
    }
}
