package com.example.myfinances;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initRadioGroup();
        initSave();
        initCancel();
    }

    private void initRadioGroup() {
        // values for radio group and editTexts
        RadioGroup rg = findViewById(R.id.radioGroup);
        EditText accountNumber = findViewById(R.id.editAccountNum);
        EditText initialBalance = findViewById(R.id.editInitialBal);
        EditText currentBalance = findViewById(R.id.editCurrentBal);
        EditText interestRate = findViewById(R.id.editInterestRate);
        EditText paymentAmount = findViewById(R.id.editPaymentAmount);

        //  Retrieve saved selection from SharedPreferences
        String savedSelection = getSharedPreferences("MyFinancePrefs", MODE_PRIVATE)
                .getString("selected_account_type", ""); // Default is empty string

        //  Match the saved selection to a RadioButton ID
        if (savedSelection.equals("CD")) {
            rg.check(R.id.radioCD);
            paymentAmount.setEnabled(false);
            interestRate.setEnabled(true);
            currentBalance.setEnabled(true);
            initialBalance.setEnabled(true);
            accountNumber.setEnabled(true);
        } else if (savedSelection.equals("Loan")) {
            rg.check(R.id.radioLoan);
            paymentAmount.setEnabled(true);
            interestRate.setEnabled(true);
            currentBalance.setEnabled(true);
            initialBalance.setEnabled(true);
            accountNumber.setEnabled(true);
        } else if (savedSelection.equals("Checking")) {
            rg.check(R.id.radioChecking);
            accountNumber.setEnabled(true);
            currentBalance.setEnabled(true);
            initialBalance.setEnabled(false);
            interestRate.setEnabled(false);
            paymentAmount.setEnabled(false);

        }


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String selectedOption = "";

                if (checkedId == R.id.radioCD) {
                    selectedOption = "CD";
                    paymentAmount.setEnabled(false);
                    interestRate.setEnabled(true);
                    currentBalance.setEnabled(true);
                    initialBalance.setEnabled(true);
                    accountNumber.setEnabled(true);
                } else if (checkedId == R.id.radioLoan) {
                    selectedOption = "Loan";
                    paymentAmount.setEnabled(true);
                    interestRate.setEnabled(true);
                    currentBalance.setEnabled(true);
                    initialBalance.setEnabled(true);
                    accountNumber.setEnabled(true);
                } else if (checkedId == R.id.radioChecking) {
                    selectedOption = "Checking";
                    rg.check(R.id.radioChecking);
                    accountNumber.setEnabled(true);
                    currentBalance.setEnabled(true);
                    initialBalance.setEnabled(false);
                    interestRate.setEnabled(false);
                    paymentAmount.setEnabled(false);
                }

                // Save selection to SharedPreferences
                getSharedPreferences("MyFinancePrefs", MODE_PRIVATE)
                        .edit()
                        .putString("selected_account_type", selectedOption)
                        .apply();

                Log.d("RadioSelection", "Saved selection: " + selectedOption);
            }
        });
    }

    private void initSave() {
        Button button = findViewById(R.id.buttonSave);

        button.setOnClickListener(v -> {
            String accountType = getSharedPreferences("MyFinancePrefs", MODE_PRIVATE)
                    .getString("selected_account_type", "");

            String accountNumber = ((EditText) findViewById(R.id.editAccountNum)).getText().toString();
            String initialBalance = ((EditText) findViewById(R.id.editInitialBal)).getText().toString();
            String currentBalance = ((EditText) findViewById(R.id.editCurrentBal)).getText().toString();
            String interestRate = ((EditText) findViewById(R.id.editInterestRate)).getText().toString();
            String paymentAmount = ((EditText) findViewById(R.id.editPaymentAmount)).getText().toString();

            EditText accountNumberEdit = findViewById(R.id.editAccountNum);
            EditText initialBalEdit = findViewById(R.id.editInitialBal);
            EditText currentBalEdit = findViewById(R.id.editCurrentBal);
            EditText interestRateEdit = findViewById(R.id.editInterestRate);
            EditText paymentAmountEdit = findViewById(R.id.editPaymentAmount);



            try {
                FinanceDBHelper dbHelper = new FinanceDBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("account_number", accountNumber);
                values.put("current_balance", currentBalance);

                if (accountType.equals("CD")) {
                    values.put("initial_balance", initialBalance);
                    values.put("interest_rate", interestRate);
                    db.insertOrThrow("cds", null, values);
                } else if (accountType.equals("Loan")) {
                    values.put("initial_balance", initialBalance);
                    values.put("interest_rate", interestRate);
                    values.put("payment_amount", paymentAmount);
                    db.insertOrThrow("loans", null, values);
                } else if (accountType.equals("Checking")) {
                    db.insertOrThrow("checking", null, values);
                }
                accountNumberEdit.setText(" ");
                initialBalEdit.setText(" ");
                currentBalEdit.setText(" ");
                interestRateEdit.setText(" ");
                paymentAmountEdit.setText(" ");



                db.close();

                Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
                Log.d("Database", "Successfully saved data");

            } catch (Exception e) {

                Toast.makeText(this, "Error: Account already exists", Toast.LENGTH_LONG).show();
                Log.e("SQL ERROR", e.toString());
            }
        });
    }

    private void initCancel(){
        Button buttonCancel = findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(v ->{

            EditText accountNumberEdit = findViewById(R.id.editAccountNum);
            EditText initialBalEdit = findViewById(R.id.editInitialBal);
            EditText currentBalEdit = findViewById(R.id.editCurrentBal);
            EditText interestRateEdit = findViewById(R.id.editInterestRate);
            EditText paymentAmountEdit = findViewById(R.id.editPaymentAmount);

            accountNumberEdit.setText(" ");
            initialBalEdit.setText(" ");
            currentBalEdit.setText(" ");
            interestRateEdit.setText(" ");
            paymentAmountEdit.setText(" ");



        });

    }



}