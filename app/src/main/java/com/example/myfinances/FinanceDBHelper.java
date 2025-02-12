package com.example.myfinances;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FinanceDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 1;

    // Create Checking Account Table (Base Account)
    private static final String CREATE_TABLE_CHECKING =
            "CREATE TABLE checking (" +
                    "account_number TEXT PRIMARY KEY, " +
                    "current_balance REAL NOT NULL);";

    // Create CDs Table (References Checking)
    private static final String CREATE_TABLE_CDS =
            "CREATE TABLE cds (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account_number TEXT NOT NULL, " +
                    "initial_balance REAL NOT NULL, " +
                    "current_balance REAL NOT NULL, " +
                    "interest_rate REAL NOT NULL, " +
                    "FOREIGN KEY (account_number) REFERENCES checking(account_number) ON DELETE CASCADE);";

    // Create Loans Table (References Checking)
    private static final String CREATE_TABLE_LOANS =
            "CREATE TABLE loans (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account_number TEXT NOT NULL, " +
                    "initial_balance REAL NOT NULL, " +
                    "current_balance REAL NOT NULL, " +
                    "payment_amount REAL NOT NULL, " +
                    "interest_rate REAL NOT NULL, " +
                    "FOREIGN KEY (account_number) REFERENCES checking(account_number) ON DELETE CASCADE);";

    public FinanceDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHECKING);
        db.execSQL(CREATE_TABLE_CDS);
        db.execSQL(CREATE_TABLE_LOANS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FinanceDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to " +
                        newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS cds");
        db.execSQL("DROP TABLE IF EXISTS loans");
        db.execSQL("DROP TABLE IF EXISTS checking");
        onCreate(db);
    }
}
