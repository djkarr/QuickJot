package xyz.danielkarr.quickjot;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Notes {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "Note")
    public String note;

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getNote() {

        return note;
    }

    @Override
    public String toString(){
        return id + " :  " + note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }
}