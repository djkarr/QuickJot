package xyz.danielkarr.quickjot;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Notes.class}, version = 1)
public abstract class MyDatabase extends RoomDatabase {
    public abstract NoteDao mNoteDao();
}
