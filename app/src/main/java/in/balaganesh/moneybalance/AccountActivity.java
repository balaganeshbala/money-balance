package in.balaganesh.moneybalance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountActivity extends AppCompatActivity {

    private Account account;
    private TextView accountNameText;
    private TextView amountText;
    private EditText amountField;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        dbHelper = new DBHelper(this);
        Intent intent = this.getIntent();
        account = (Account) intent.getSerializableExtra("account");

        String accountName = account.accountName;
        java.math.BigDecimal amount = account.amount;

        accountNameText = (TextView) findViewById(R.id.account_name_text);
        amountText = (TextView) findViewById(R.id.amount_text);
        accountNameText.setText(accountName);
        amountText.setText("₹ " + amount);

        amountField = (EditText) findViewById(R.id.amount_edit_text);
    }

    public void addAmount(View view) {

        final String amountValue = amountField.getText().toString();
        if (amountValue.trim().length() > 0 && !amountValue.equals(".")) {
            new AlertDialog.Builder(this)
                .setTitle("Adding Amount")
                .setMessage("Are you sure want to add amount ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        java.math.BigDecimal amount = account.amount.add(new java.math.BigDecimal(amountValue));
                        account.amount = amount;
                        dbHelper.updateAccount(account);
                        amountText.setText("₹ " + dbHelper.round(account.amount).toString());
                        amountField.setText(null);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
        } else {
            Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();
        }
    }

    public void subtractAmount(View view) {
        final String amountValue = amountField.getText().toString();
        if (amountValue.trim().length() > 0 && !amountValue.equals(".")) {
            new AlertDialog.Builder(this)
                .setTitle("Subtracting Amount")
                .setMessage("Are you sure want to subtract amount ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        java.math.BigDecimal amount = account.amount.subtract(new java.math.BigDecimal(amountValue));
                        account.amount = amount;
                        dbHelper.updateAccount(account);
                        amountText.setText("₹ " + dbHelper.round(account.amount).toString());
                        amountField.setText(null);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
        } else {
            Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();
        }
    }

    public void goBack(View view) {
        finish();
    }

    public void deleteAccount(View view) {
        new AlertDialog.Builder(this)
            .setTitle("Deleting Account")
            .setMessage("Are you sure want to delete the account ?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dbHelper.deleteAccount(account);
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, null).show();
    }
}
