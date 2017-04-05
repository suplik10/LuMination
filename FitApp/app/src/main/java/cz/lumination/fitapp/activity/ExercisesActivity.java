package cz.lumination.fitapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.lumination.fitapp.R;
import cz.lumination.fitapp.dao.ExerciseDao;
import cz.lumination.fitapp.dao.impl.ExerciseDaoImpl;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.persistence.DatabaseHelper;

/**
 * Created by mkasl on 01.04.2017.
 */

public class ExercisesActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private StableArrayAdapter adapter;
    private List<ExerciseDto> exercises;
    private ExerciseDao exerciseDao = new ExerciseDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises_activity);

        ListView listview = (ListView) findViewById(R.id.listExercises);
        listview.setOnItemClickListener(this);
        this.adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1,this.getExercises());
        listview.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabExercises);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExercise();
            }
        });

    }

    private List<ExerciseDto> getExercises() {
        List<ExerciseDto> exercises = new ArrayList<>();
        try {
            exercises = exerciseDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.exercises = exercises;
        return exercises;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent NovaAktivita = new Intent(this, ExerciseActivity.class);
            //startActivity(NovaAktivita);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void addExercise(){
        startActivityForResult(new Intent(this, EditExerciseActivity.class), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                boolean created = false;
                created = data.getBooleanExtra("save", created);
                if(created) {
                    View view = this.findViewById(android.R.id.content);
                    Snackbar.make(view, "Exercise saved!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    this.adapter.getData().clear();
                    this.adapter.getData().addAll(this.getExercises());
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent showExercise = new Intent(this, EditExerciseActivity.class);
        showExercise.putExtra("exercise", this.exercises.get(position));
        showExercise.putExtra("readOnly", true);
        startActivity(showExercise);
    }

    private class StableArrayAdapter extends ArrayAdapter<ExerciseDto> {

        Context context;
        List<ExerciseDto> objects;
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<ExerciseDto> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.exercises_row_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            textView.setText(objects.get(position).getName());
            TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            textView2.setText(objects.get(position).getType());
            return rowView;
        }

        public List<ExerciseDto> getData(){
            return this.objects;
        }

    }
}

