package com.demo.namesforanimals.data.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {
    private static NamesDatabase maleNamesDB;
    private static NamesDatabase femaleNamesDB;
    private LiveData<List<Name>> maleNamesByAlphabet;
    private LiveData<List<Name>> femaleNamesByAlphabet;
    private LiveData<List<Name>> maleNamesById;
    private LiveData<List<Name>> femaleNamesById;

    public ViewModel(@NonNull Application application) {
        super(application);
        maleNamesDB = NamesDatabase.getMaleNamesDatabase(getApplication());
        femaleNamesDB = NamesDatabase.getFemaleNamesDatabase(getApplication());
        maleNamesByAlphabet = maleNamesDB.namesDao().getAllNamesByAlphabet();
        maleNamesById = maleNamesDB.namesDao().getAllNamesByID();
        femaleNamesByAlphabet = femaleNamesDB.namesDao().getAllNamesByAlphabet();
        femaleNamesById = femaleNamesDB.namesDao().getAllNamesByID();
    }

    public LiveData<List<Name>> getMaleNamesByAlphabet() {
        return maleNamesByAlphabet;
    }

    public LiveData<List<Name>> getFemaleNamesByAlphabet() {
        return femaleNamesByAlphabet;
    }

    public LiveData<List<Name>> getMaleNamesById() {
        return maleNamesById;
    }

    public LiveData<List<Name>> getFemaleNamesById() {
        return femaleNamesById;
    }

    public void insertMaleName(Name name) {
        new InsertMaleNameTask().execute(name);
    }

    static class InsertMaleNameTask extends AsyncTask<Name, Void, Void> {
        @Override
        protected Void doInBackground(Name... names) {
            if (names != null && names.length > 0) {
                maleNamesDB.namesDao().insertName(names[0]);
            }
            return null;
        }
    }


    public void insertFemaleName(Name name) {
        new InsertFemaleNameTask().execute(name);
    }

    static class InsertFemaleNameTask extends AsyncTask<Name, Void, Void> {
        @Override
        protected Void doInBackground(Name... names) {
            if (names != null && names.length > 0) {
                femaleNamesDB.namesDao().insertName(names[0]);
            }
            return null;
        }
    }


    public void deleteMaleName(Name name) {
        new DeleteMaleNameTask().execute(name);
    }

    static class DeleteMaleNameTask extends AsyncTask<Name, Void, Void> {
        @Override
        protected Void doInBackground(Name... names) {
            if (names != null && names.length > 0) {
                maleNamesDB.namesDao().deleteName(names[0]);
            }
            return null;
        }
    }


    public void deleteFemaleName(Name name) {
        new DeleteFemaleNameTask().execute(name);
    }

    static class DeleteFemaleNameTask extends AsyncTask<Name, Void, Void> {
        @Override
        protected Void doInBackground(Name... names) {
            if (names != null && names.length > 0) {
                femaleNamesDB.namesDao().deleteName(names[0]);
            }
            return null;
        }
    }


    public void deleteAllMaleNames() {
        new DeleteAllMaleNamesTask().execute();
    }

    static class DeleteAllMaleNamesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            maleNamesDB.namesDao().deleteAllNames();
            return null;
        }
    }


    public void deleteAllFemaleNames() {
        new DeleteAllFemaleNamesTask().execute();
    }

    static class DeleteAllFemaleNamesTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            femaleNamesDB.namesDao().deleteAllNames();
            return null;
        }
    }


    public void insertListOfMaleNames(List<Name> names) {
        new InsertListOfMaleNamesTask().execute(names);
    }

    static class InsertListOfMaleNamesTask extends AsyncTask<List<Name>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Name>... lists) {
            if (lists != null && lists.length > 0) {
                maleNamesDB.namesDao().insertList(lists[0]);
            }
            return null;
        }
    }


    public void insertListOfFemaleNames(List<Name> names) {
        new InsertListOfFemaleNamesTask().execute(names);
    }

    static class InsertListOfFemaleNamesTask extends AsyncTask<List<Name>, Void, Void> {
        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Name>... lists) {
            if (lists != null && lists.length > 0) {
                femaleNamesDB.namesDao().insertList(lists[0]);
            }
            return null;
        }
    }
}
