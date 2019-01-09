package xyz.danielkarr.quickjot;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

public class NoteActivity extends AppCompatActivity {
    private MyDatabase db;
    ArrayList<String> mSpinnerList;
    String mCategory;
    boolean mEditState;
    int mID;

    @BindView(R.id.edit_text_note)
    EditText mEditTextNote;

    @BindView(R.id.category_spinner)
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        SpinnerItems sItems = new SpinnerItems();
        mSpinnerList = sItems.getItems();

        addSpinnerItems();
        db = Room.databaseBuilder(getApplicationContext(),MyDatabase.class, "Notes").build();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("id")){
                mEditState = true;
                mID = extras.getInt("id");
                setUpEditState();
            } else {
                mEditState = false;
            }
        }

    }

    void setUpEditState(){
        new AsyncTask<Integer,Void,Notes>(){
            @Override
            protected Notes doInBackground(Integer... integers){
                List<Notes> notes = db.mNoteDao().getById(integers[0]);
                return notes.get(0);
            }

            @Override
            protected void onPostExecute(Notes note){
                mEditTextNote.setText(note.getNote());
                mCategory = note.getCategory();
                mSpinner.setSelection(getSpinnerIndex(mCategory));
            }
        }.execute(mID);
    }

    int getSpinnerIndex(String topic){
       return mSpinnerList.indexOf(topic);
    }

    void addSpinnerItems(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,mSpinnerList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(arrayAdapter);
        mCategory = mSpinnerList.get(0);
    }

    @OnItemSelected(R.id.category_spinner)
    public void spinnerItemSelected(Spinner spinner, int position){
        mCategory = (String) spinner.getItemAtPosition(position);
    }

    @OnClick(R.id.fab_return)
    void onReturnClick(){
        Intent intent = new Intent(this,ViewActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.fab_save_note)
    void saveNoteClick(){
        String text = mEditTextNote.getText().toString();
        if(text.length() == 0){
            Toast.makeText(this,"Note cannot be empty!",Toast.LENGTH_SHORT).show();
        } else {
            Notes note = new Notes();
            note.setNote(text);
            note.setCategory(mCategory);
            if(mEditState){
                note.setId(mID);
            }
            new AsyncTask<Notes,Void,Void>(){
                @Override
                protected Void doInBackground(Notes... notes){
                    if(mEditState){
                        db.mNoteDao().updateNote(notes[0]);
                    } else {
                        db.mNoteDao().insertNote(notes[0]);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v){
                    refresh();
                }
            }.execute(note);
            Toast.makeText(this,"Saved",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fab_clear)
    void clearClick(){
        if(!mEditState){
            refresh();
            Toast.makeText(this,"Cleared",Toast.LENGTH_SHORT).show();
        } else {
            new AsyncTask<Integer,Void,Void>(){
                @Override
                protected Void doInBackground(Integer... integers){
                    db.mNoteDao().deleteNoteById(integers[0]);
                    return null;
                }

                @Override
                protected void onPostExecute(Void v){
                    refresh();
                }

            }.execute(mID);
            Toast.makeText(this,"Deleted",Toast.LENGTH_SHORT).show();
        }
    }

    void refresh(){
        mEditTextNote.getText().clear();
        mEditState = false;
    }
}
