package com.example.electricitybillapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editTextUnits, editTextRebate;
    Button buttonCalculate, buttonSave, buttonViewList, buttonAbout;
    TextView textViewResult;

    String selectedMonth;
    double totalCharges, finalCost;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editTextUnits = findViewById(R.id.editTextUnits);
        editTextRebate = findViewById(R.id.editTextRebate);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonViewList = findViewById(R.id.buttonViewList);
        buttonAbout = findViewById(R.id.buttonAbout);
        textViewResult = findViewById(R.id.textViewResult);

        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapter);

        // Tekan butang kira
        buttonCalculate.setOnClickListener(v -> calculateBill());

        // Simpan ke database
        buttonSave.setOnClickListener(v -> {
            String unitsStr = editTextUnits.getText().toString().trim();
            String rebateStr = editTextRebate.getText().toString().trim();

            if (unitsStr.isEmpty() || rebateStr.isEmpty() || textViewResult.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please calculate the bill before saving.", Toast.LENGTH_SHORT).show();
                return;
            }

            int units = Integer.parseInt(unitsStr);
            double rebate = Double.parseDouble(rebateStr);

            boolean inserted = dbHelper.insertData(selectedMonth, units, rebate, totalCharges, finalCost);

            if (inserted) {
                Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show();

                // Kosongkan input & reset result
                editTextUnits.setText("");
                editTextRebate.setText("");
                textViewResult.setText("Result will appear here");
                spinnerMonth.setSelection(0); // optional: reset ke bulan pertama
            } else {
                Toast.makeText(this, "Save failed!", Toast.LENGTH_SHORT).show();
            }
        });

        // ButtonView: Buka ViewBillsActivity
        buttonViewList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewBillsActivity.class);
            startActivity(intent);
        });

        // Button About: Buka AboutActivity
        buttonAbout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
    }

    private void calculateBill() {
        selectedMonth = spinnerMonth.getSelectedItem().toString();
        String unitsStr = editTextUnits.getText().toString().trim();
        String rebateStr = editTextRebate.getText().toString().trim();

        if (unitsStr.isEmpty()) {
            editTextUnits.setError("Please enter units used.");
            return;
        }
        if (rebateStr.isEmpty()) {
            editTextRebate.setError("Please enter rebate percentage.");
            return;
        }

        int units;
        double rebate;

        try {
            units = Integer.parseInt(unitsStr);
            rebate = Double.parseDouble(rebateStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers only.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rebate < 0 || rebate > 5) {
            editTextRebate.setError("Rebate must be between 0% and 5%.");
            return;
        }

        totalCharges = computeCharges(units);
        finalCost = totalCharges - (totalCharges * rebate / 100);

        String result = "Month: " + selectedMonth + "\n"
                + "Units Used: " + units + " kWh\n"
                + "Total Charges: RM " + String.format("%.2f", totalCharges) + "\n"
                + "Final Cost (after " + rebate + "% rebate): RM " + String.format("%.2f", finalCost);

        textViewResult.setText(result);
    }

    private double computeCharges(int units) {
        double total = 0;
        int remaining = units;

        if (remaining > 200) {
            total += 200 * 0.218;
            remaining -= 200;
        } else {
            return remaining * 0.218;
        }

        if (remaining > 100) {
            total += 100 * 0.334;
            remaining -= 100;
        } else {
            return total + remaining * 0.334;
        }

        if (remaining > 300) {
            total += 300 * 0.516;
            remaining -= 300;
        } else {
            return total + remaining * 0.516;
        }

        total += remaining * 0.546;
        return total;
    }
}
