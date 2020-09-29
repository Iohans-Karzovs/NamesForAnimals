package com.demo.namesforanimals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.demo.namesforanimals.data.Names;
import com.demo.namesforanimals.data.database.Name;
import com.demo.namesforanimals.data.database.ViewModel;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private RadioButton radioButtonForFemale;
    private TextView textView;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioButtonForFemale = findViewById(R.id.radioButtonForFemale);
        textView = findViewById(R.id.textViewName);
        if (savedInstanceState != null) {
            textView.setText(savedInstanceState.getString("name"));
        }
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(ViewModel.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstTime = preferences.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            viewModel.insertListOfMaleNames(Names.getMaleNamesList());
            viewModel.insertListOfFemaleNames(Names.getFemaleNamesList());
            preferences.edit().putBoolean("isFirstTime", false).apply();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        String name = textView.getText().toString();
        if (name != null) {
            outState.putString("name", name);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this, ListNamesActivity.class);
        switch (id) {
            case R.id.femaleNamesListMenuItem:
                intent.putExtra("isMale", false);
                startActivity(intent);
                return true;
            case R.id.maleNamesListItemMenu:
                intent.putExtra("isMale", true);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("SetTextI18n")
    public void OnClickChooseName(View view) {
        final Random random = new Random();
        if (radioButtonForFemale.isChecked()) {
            LiveData<List<Name>> listOfNames = viewModel.getFemaleNamesByAlphabet();
            listOfNames.observe(this, new Observer<List<Name>>() {
                @Override
                public void onChanged(List<Name> names) {
                    if (names.size() > 0) {
                        if (names.size() < 10) {
                            Toast.makeText(MainActivity.this, "В базе данных меньше 10 имен!", Toast.LENGTH_SHORT).show();
                        }
                        Name name = names.get(random.nextInt(names.size()));
                        textView.setText(name.getName());
                    } else {
                        Toast.makeText(MainActivity.this, "В базе данных нет имен!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            LiveData<List<Name>> listOfNames = viewModel.getMaleNamesByAlphabet();
            listOfNames.observe(this, new Observer<List<Name>>() {
                @Override
                public void onChanged(List<Name> names) {
                    if (names.size() > 0) {
                        if (names.size() < 10) {
                            Toast.makeText(MainActivity.this, "В базе данных меньше 10 имен!", Toast.LENGTH_SHORT).show();
                        }
                        Name name = names.get(random.nextInt(names.size()));
                        textView.setText(name.getName());
                    } else {
                        Toast.makeText(MainActivity.this, "В базе данных нет имен!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}

