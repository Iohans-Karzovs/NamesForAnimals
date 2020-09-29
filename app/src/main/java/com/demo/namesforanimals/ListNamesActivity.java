package com.demo.namesforanimals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.namesforanimals.data.Names;
import com.demo.namesforanimals.data.NamesAdapter;
import com.demo.namesforanimals.data.database.Name;
import com.demo.namesforanimals.data.database.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListNamesActivity extends AppCompatActivity {

    private ViewModel viewModel;
    private List<Name> names = new ArrayList<>();
    private NamesAdapter maleNamesAdapter;
    private NamesAdapter femaleNamesAdapter;
    private NamesAdapter maleNamesByIdAdapter;
    private NamesAdapter femaleNamesByIdAdapter;
    private boolean isMale;
    private boolean sortedByName;
    private boolean isClicked;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_names);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())
                .create(ViewModel.class);
        maleNamesAdapter = new NamesAdapter(names);
        maleNamesByIdAdapter = new NamesAdapter(names);
        femaleNamesAdapter = new NamesAdapter(names);
        femaleNamesByIdAdapter = new NamesAdapter(names);
        getData();
        Intent intent = getIntent();
        if (intent.hasExtra("isMale")) {
            isMale = intent.getBooleanExtra("isMale", true);
        }
        if (intent.hasExtra("sortedByName")) {
            sortedByName = intent.getBooleanExtra("sortedByName", true);
        } else {
            sortedByName = true;
        }
        setAdapters();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_activity, menu);
        MenuItem menuItemNames = menu.findItem(R.id.namesListMenuItem);
        MenuItem menuItemSortBy = menu.findItem(R.id.sortByMenuItem);
        if (isMale) {
            menuItemNames.setTitle(R.string.male_names);
        } else {
            menuItemNames.setTitle(R.string.female_names);
        }
        if (sortedByName) {
            menuItemSortBy.setTitle(R.string.sort_by_name);
        } else {
            menuItemSortBy.setTitle(R.string.sort_by_id);
        }
        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemSortBy = menu.findItem(R.id.sortByMenuItem);
        if (isClicked) {
            menuItemSortBy.setTitle(R.string.sort_by_id);
            sortedByName = false;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAllMenuItem:
                if (!isMale) {
                    viewModel.deleteAllMaleNames();
                } else {
                    viewModel.deleteAllFemaleNames();
                }
                return true;
            case R.id.restoreAllMenuItem:
                viewModel.deleteAllMaleNames();
                viewModel.deleteAllFemaleNames();
                viewModel.insertListOfFemaleNames(Names.getFemaleNamesList());
                viewModel.insertListOfMaleNames(Names.getMaleNamesList());
                return true;
            case R.id.addNameMenuItem:
                Intent intent = new Intent(this, AddNameActivity.class);
                intent.putExtra("sortedByName", sortedByName);
                intent.putExtra("isMale", isMale);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void getData() {
        LiveData<List<Name>> maleNamesByAlphabet = viewModel.getMaleNamesByAlphabet();
        maleNamesByAlphabet.observe(this, new Observer<List<Name>>() {
            @Override
            public void onChanged(List<Name> names) {
                maleNamesAdapter.setNames(names);
            }
        });
        LiveData<List<Name>> femaleNamesByAlphabet = viewModel.getFemaleNamesByAlphabet();
        femaleNamesByAlphabet.observe(this, new Observer<List<Name>>() {
            @Override
            public void onChanged(List<Name> names) {
                femaleNamesAdapter.setNames(names);
            }
        });
        LiveData<List<Name>> maleNamesById = viewModel.getMaleNamesById();
        maleNamesById.observe(this, new Observer<List<Name>>() {
            @Override
            public void onChanged(List<Name> names) {
                maleNamesByIdAdapter.setNames(names);
            }
        });
        LiveData<List<Name>> femaleNameById = viewModel.getFemaleNamesById();
        femaleNameById.observe(this, new Observer<List<Name>>() {
            @Override
            public void onChanged(List<Name> names) {
                femaleNamesByIdAdapter.setNames(names);
            }
        });
    }

    private void setAdapters() {
        if (isMale) {
            if (sortedByName) {
                recyclerView.setAdapter(maleNamesAdapter);
                sortedByName = false;
            } else {
                recyclerView.setAdapter(maleNamesByIdAdapter);
                sortedByName = true;
            }
            isMale = false;
        } else {
            if (sortedByName) {
                recyclerView.setAdapter(femaleNamesAdapter);
                sortedByName = false;
            } else {
                recyclerView.setAdapter(femaleNamesByIdAdapter);
                sortedByName = true;
            }
            isMale = true;
        }
    }

    private void remove(int position) {
        NamesAdapter adapter = (NamesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            if (!isMale) {
                Name name = adapter.getNames().get(position);
                viewModel.deleteMaleName(name);
            } else {
                Name name = adapter.getNames().get(position);
                viewModel.deleteFemaleName(name);
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void OnClickSortBySex(MenuItem item) {
        if (isMale) {
            recyclerView.setAdapter(maleNamesAdapter);
            isMale = false;
            item.setTitle(R.string.female_names);
        } else {
            recyclerView.setAdapter(femaleNamesAdapter);
            isMale = true;
            item.setTitle(R.string.male_names);
        }
        isClicked = true;
    }

    public void onClickSortById(MenuItem item) {
        Log.i("testt", "isMale: " + Boolean.toString(isMale) + " sortedByName: " + Boolean.toString(sortedByName));
        if (isClicked) {
            item.setTitle(R.string.sort_by_id);
            sortedByName = false;
            isClicked = false;
        }
        if (!isMale) {
            if (sortedByName) {
                recyclerView.setAdapter(maleNamesAdapter);
                sortedByName = false;
                item.setTitle(R.string.sort_by_id);
            } else {
                recyclerView.setAdapter(maleNamesByIdAdapter);
                sortedByName = true;
                item.setTitle(R.string.sort_by_name);
            }
        } else {
            if (sortedByName) {
                recyclerView.setAdapter(femaleNamesAdapter);
                sortedByName = false;
                item.setTitle(R.string.sort_by_id);
            } else {
                recyclerView.setAdapter(femaleNamesByIdAdapter);
                sortedByName = true;
                item.setTitle(R.string.sort_by_name);
            }

        }
        Log.i("testt", "isMale: " + Boolean.toString(isMale) + " sortedByName: " + Boolean.toString(sortedByName));
    }


}