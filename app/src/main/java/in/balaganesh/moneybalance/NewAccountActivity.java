package in.balaganesh.moneybalance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        if (accountValue.trim().length() > 0 && amountValue.trim().length() > 0 && !amountValue.equals(".")) {
            Account newAccount = new Account(0, accountValue, amountValue);

            DBHelper dbHelper = new DBHelper(this);
            dbHelper.addAccount(newAccount);
            finish();
        } else {
            Toast.makeText(this, "Invalid Value", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        this.finish();
    }
}
