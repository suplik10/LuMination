package cz.lumination.fitapp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.R;
import cz.lumination.fitapp.dao.WorkoutDao;
import cz.lumination.fitapp.dao.impl.WorkoutDaoImpl;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;
import cz.lumination.fitapp.persistence.DatabaseHelper;

/**
 * Created by mkasl on 01.04.2017.
 */

public class WorkoutsActivity extends AppCompatActivity implements ListView.OnItemClickListener{

    private List<WorkoutDto> workouts;
    private StableArrayAdapter adapter;
    private WorkoutDao workoutDao = new WorkoutDaoImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workouts_activity);

        ListView listview = (ListView) findViewById(R.id.listWorkouts);
        listview.setOnItemClickListener(this);
        this.adapter = new WorkoutsActivity.StableArrayAdapter(this,android.R.layout.simple_list_item_1,this.getWorkouts());
        listview.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabWorkouts);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addWorkout();
            }
        });

    }

    private void addWorkout(){
        startActivityForResult(new Intent(this, EditWorkoutActivity.class), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private List<WorkoutDto> getWorkouts(){
        List<WorkoutDto> workouts = new ArrayList<>();
        try {
            workouts = workoutDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.workouts = workouts;
        return workouts;
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
                    Snackbar.make(view, "Workout saved!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    this.adapter.getData().clear();
                    this.adapter.getData().addAll(this.getWorkouts());
                    this.adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent showWorkout = new Intent(this, EditWorkoutActivity.class);
        showWorkout.putExtra("workout", this.workouts.get(position));
        showWorkout.putExtra("readOnly", true);
        startActivityForResult(showWorkout, 1);
    }

    private class StableArrayAdapter extends ArrayAdapter<WorkoutDto> {

        Context context;
        List<WorkoutDto> objects;
        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<WorkoutDto> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            this.objects = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.workout_row_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            textView.setText(objects.get(position).getName());
            //TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
            //textView2.setText(objects.get(position).getType());
            return rowView;
        }

        public List<WorkoutDto> getData(){
            return this.objects;
        }

    }
}
