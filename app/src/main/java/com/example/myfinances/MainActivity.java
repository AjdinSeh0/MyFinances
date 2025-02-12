package com.example.myfinances;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioGroup;

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

        // 3️⃣ Set up listener (same as before)
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



}