package com.demo.namesforanimals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.demo.namesforanimals.data.database.Name;
import com.demo.namesforanimals.data.database.ViewModel;

public class AddNameActivity extends AppCompatActivity {
    private boolean isMale;
    private boolean sortedByName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_name);
        Intent intent = getIntent();
        if (intent.hasExtra("isMale")) {
            isMale = intent.getBooleanExtra("isMale", true);
        }
        if (intent.hasExtra("sortedByName")) {
            sortedByName = intent.getBooleanExtra("sortedByName", true);
        }
    }

    public void OnClickAddName(View view) {
        EditText editText = findViewById(R.id.addNameEditText);
        ViewModel viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(ViewModel.class);
        String name = editText.getText().toString().trim();
        Intent intent = new Intent(this, ListNamesActivity.class);
        if (!isMale) {
            viewModel.insertMaleName(new Name(name));
            intent.putExtra("isMale", true);
        } else {
            viewModel.insertFemaleName(new Name(name));
            intent.putExtra("isMale", false);
        }
        intent.putExtra("sortedByName", sortedByName);
        startActivity(intent);
    }
}