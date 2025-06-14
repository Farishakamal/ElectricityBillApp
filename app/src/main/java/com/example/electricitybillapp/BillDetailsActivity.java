package com.example.electricitybillapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ✅ Tambah ini

public class BillDetailsActivity extends AppCompatActivity {

    TextView textViewMonth, textViewUnits, textViewRebate, textViewTotal, textViewFinal;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        // ✅ Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarBill);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Komponen UI
        textViewMonth = findViewById(R.id.textMonth);
        textViewUnits = findViewById(R.id.textUnits);
        textViewRebate = findViewById(R.id.textRebate);
        textViewTotal = findViewById(R.id.textTotal);
        textViewFinal = findViewById(R.id.textFinal);

        dbHelper = new DatabaseHelper(this);

        int recordId = getIntent().getIntExtra("record_id", -1);

        if (recordId == -1) {
            Toast.makeText(this, "Invalid record ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbHelper.getAllData();
        boolean found = false;

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            if (id == recordId) {
                found = true;
                String month = cursor.getString(1);
                int units = cursor.getInt(2);
                double rebate = cursor.getDouble(3);
                double total = cursor.getDouble(4);
                double finalCost = cursor.getDouble(5);

                textViewMonth.setText("Month: " + month);
                textViewUnits.setText("Units Used: " + units + " kWh");
                textViewRebate.setText("Rebate: " + rebate + " %");
                textViewTotal.setText("Total Charges: RM " + String.format("%.2f", total));
                textViewFinal.setText("Final Cost: RM " + String.format("%.2f", finalCost));
                break;
            }
        }

        if (!found) {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
            textViewMonth.setText("No record found.");
            textViewUnits.setText("");
            textViewRebate.setText("");
            textViewTotal.setText("");
            textViewFinal.setText("");
        }
    }
}
