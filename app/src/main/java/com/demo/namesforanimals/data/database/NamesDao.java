package com.demo.namesforanimals.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NamesDao {
    @Query("SELECT * FROM Name ORDER BY name ASC")
    LiveData<List<Name>> getAllNamesByAlphabet();

    @Query("SELECT * FROM Name ORDER BY id DESC")
    LiveData<List<Name>> getAllNamesByID();

    @Insert
    void insertName(Name name);

    @Insert
    void insertList(List<Name> names);

    @Delete
    void deleteName(Name name);

    @Query("DELETE FROM Name")
    void deleteAllNames();

}
