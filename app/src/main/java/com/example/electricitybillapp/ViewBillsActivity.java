package com.example.electricitybillapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewBillsActivity extends AppCompatActivity {

    ListView listViewBills;
    Button buttonClearAll;
    DatabaseHelper dbHelper;
    ArrayList<HashMap<String, String>> billList = new ArrayList<>();
    ArrayList<Integer> recordIDs = new ArrayList<>();
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bills);

        listViewBills = findViewById(R.id.listViewBills);
        buttonClearAll = findViewById(R.id.buttonClearAll);
        dbHelper = new DatabaseHelper(this);

        loadBills();

        listViewBills.setOnItemClickListener((parent, view, position, id) -> {
            int selectedID = recordIDs.get(position);
            Intent intent = new Intent(ViewBillsActivity.this, BillDetailsActivity.class);
            intent.putExtra("record_id", selectedID);
            startActivity(intent);
        });

        // Button Clear All
        buttonClearAll.setOnClickListener(v -> {
            if (billList.isEmpty()) {
                Toast.makeText(this, "No data to delete.", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete all saved bills?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteAll();      // 🔥 Delete all from DB
                        billList.clear();          // Kosongkan list data
                        recordIDs.clear();         // Kosongkan ID list
                        adapter.notifyDataSetChanged(); // Refresh UI
                        Toast.makeText(this, "All records deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadBills() {
        Cursor cursor = dbHelper.getAllData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No records found.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            int id = cursor.getInt(0);
            String month = cursor.getString(1);
            double finalCost = cursor.getDouble(5);

            map.put("title", "Month: " + month);
            map.put("subtitle", "Final Cost: RM " + String.format("%.2f", finalCost));
            billList.add(map);
            recordIDs.add(id);
        }

        adapter = new SimpleAdapter(
                this,
                billList,
                android.R.layout.simple_list_item_2,
                new String[]{"title", "subtitle"},
                new int[]{android.R.id.text1, android.R.id.text2}
        );

        listViewBills.setAdapter(adapter);
    }
}