package xyz.danielkarr.quickjot;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

public class ViewActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    private MyDatabase db;
    private List<Notes> mNotesList;
    private ArrayList<String> mSpinnerList;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.sort_spinner)
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);

        mNotesList = new ArrayList<>();
        db = Room.databaseBuilder(getApplicationContext(),MyDatabase.class, "Notes").build();
        backgroundQuery BQ = new backgroundQuery();
        BQ.execute();
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        SpinnerItems sItems = new SpinnerItems();
        mSpinnerList = sItems.getItems();
        addSpinnerItems();
    }

    void addSpinnerItems(){
        mSpinnerList.add(0,"All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,mSpinnerList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(arrayAdapter);
    }

    @OnItemSelected(R.id.sort_spinner)
    public void spinnerItemSelected(Spinner spinner, int position){
        String category = (String) spinner.getItemAtPosition(position);
        new AsyncTask<String,Void,List<Notes>>(){
            @Override
            protected List<Notes> doInBackground(String... strings){
                List<Notes> list;
                if(strings[0].equals("All")){
                    list = db.mNoteDao().getAll();
                } else {
                    list = db.mNoteDao().getByCategory(strings[0]);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<Notes> list){
                setAdapter(list);
            }

        }.execute(category);
    }

    /**
     * Sets recyclerview adapter. Called after DB query.
     * @param result
     */
    void setAdapter(List<Notes> result){
        mNotesList = result;
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this,mNotesList);
        adapter.setClickListener(this);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view,int position){
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra("id",mNotesList.get(position).getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }

    private class backgroundQuery extends AsyncTask<String, Void, List<Notes>> {

        backgroundQuery(){
        }

        @Override
        protected List<Notes> doInBackground(String... query) {
            return db.mNoteDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Notes> result) {
            super.onPostExecute(result);
            setAdapter(result);
        }
    }
}
