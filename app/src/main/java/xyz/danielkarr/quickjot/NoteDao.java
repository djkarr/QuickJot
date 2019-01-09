package xyz.danielkarr.quickjot;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * from notes")
    List<Notes> getAll();

    @Query("Select * from notes where category = :category")
    List<Notes> getByCategory(String category);

    @Query("select * from notes where id = :id")
    List<Notes> getById(int id);

    @Update
    void updateNote(Notes note);

    @Query("delete from notes where id = :id")
    void deleteNoteById(int id);

    @Delete
    void deleteNote(Notes note);

    @Insert
    void insertNote(Notes note);
}