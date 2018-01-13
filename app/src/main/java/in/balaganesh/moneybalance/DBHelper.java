package in.balaganesh.moneybalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by balaganesh on 06/01/18.
 */

class DBHelper extends SQLiteOpenHelper {

    // All Static variables
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MoneyBalance";

    // Accounts table name
    private static final String TABLE_ACCOUNTS = "accounts";

    // Account Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "account_name";
    private static final String KEY_AMOUNT = "amount";

    protected DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_AMOUNT + " TEXT" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new account
    void addAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, account.accountName);
        values.put(KEY_AMOUNT, String.valueOf(round(account.amount)));

        // Inserting Row
        db.insert(TABLE_ACCOUNTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single account
    Account getAccount(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ACCOUNTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_AMOUNT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Account account = new Account(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return account
        return account;
    }

    // Getting All Accounts
    protected List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<Account>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                // Adding account to list
                accountList.add(account);
            } while (cursor.moveToNext());
        }

        // return account list
        return accountList;
    }

    // Updating single account
    protected int updateAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, account.accountName);
        values.put(KEY_AMOUNT, String.valueOf(round(account.amount)));

        // updating row
        return db.update(TABLE_ACCOUNTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(account.id) });
    }

    // Deleting single account
    protected void deleteAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACCOUNTS, KEY_ID + " = ?",
                new String[] { String.valueOf(account.id) });
        db.close();
    }

    // Getting accounts Count
    protected int getAccountsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    protected BigDecimal round(BigDecimal value) {

        value = value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        return value;
    }
}
