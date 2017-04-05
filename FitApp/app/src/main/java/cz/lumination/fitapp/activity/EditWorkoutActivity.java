package cz.lumination.fitapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.R;
import cz.lumination.fitapp.activity.workout.ListFragment;
import cz.lumination.fitapp.dao.ExerciseDao;
import cz.lumination.fitapp.dao.WorkoutDao;
import cz.lumination.fitapp.dao.impl.ExerciseDaoImpl;
import cz.lumination.fitapp.dao.impl.WorkoutDaoImpl;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;

/**
 * Created by mkasl on 04.04.2017.
 */

public class EditWorkoutActivity extends AppCompatActivity {
    private static final int MIN_TEXT_LENGTH = 4;
    private static final String EMPTY_STRING = "";

    private WorkoutDao workoutDao = new WorkoutDaoImpl();
    private ExerciseDao exerciseDao = new ExerciseDaoImpl();

    private TextInputLayout textInputLayoutName;
    private EditText editTextName;

    private boolean readOnly = false;
    private Button button;

    private List<ExerciseDto> exercisesAll;
    private CharSequence[] exercisesStrings;
    private final ArrayList selectedItems = new ArrayList();
    private boolean[] checkedItemsArray;

    private WorkoutDto workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_workout_activity);

        this.workout = (WorkoutDto) getIntent().getSerializableExtra("workout");
        this.readOnly = getIntent().getBooleanExtra("readOnly", false);

        if(this.workout == null){
            this.workout = new WorkoutDto();
        }

        initInputFields(workout);

        this.exercisesAll = this.getExercises();

        button = (Button) findViewById(R.id.addExercise);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showExercisesDialog();
            }
        });

        if(this.readOnly){
           // this.setReadOnlyMode();
        }

        ListFragment listFragment =  ListFragment.newInstance();
        listFragment.setExercisesDto(this.workout.getExercises());
        showFragment(listFragment);

    }

    private List<ExerciseDto> getExercises(){
        List<ExerciseDto> exercisesAll = new ArrayList<>();
        try {
            exercisesAll = this.exerciseDao.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.exercisesStrings = new CharSequence[exercisesAll.size()];
        this.checkedItemsArray = new boolean[exercisesAll.size()];
        for(int i = 0; i < exercisesAll.size(); i++) {
            this.exercisesStrings[i] = exercisesAll.get(i).getName();

            if(this.workout.getExercises() != null){
                for(int y = 0; y < this.workout.getExercises().size(); y++) {
                    if(exercisesAll.get(i).getExerciseId() == this.workout.getExercises().get(y).getExerciseId()){
                        this.checkedItemsArray[i] = true;
                        this.selectedItems.add(new Integer(i));
                    }
                }
            }else{
                this.checkedItemsArray[i] = false;
            }
        }
        return exercisesAll;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, "fragment").commit();
    }

    private void showExercisesDialog() {
        AlertDialog dialog;
        //following code will be in your activity.java file
        //final CharSequence[] items = {" Easy "," Medium "," Hard "," Very Hard "};
        // arraylist to keep the selected items


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select exercises to workout");
        builder.setMultiChoiceItems(this.exercisesStrings, this.checkedItemsArray,
                new DialogInterface.OnMultiChoiceClickListener() {
                    // indexSelected contains the index of item (of which checkbox checked)
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected,
                                        boolean isChecked) {
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            // write your code when user checked the checkbox
                            selectedItems.add(indexSelected);
                            checkedItemsArray[indexSelected] = true;
                        } else if (selectedItems.contains(indexSelected)) {
                            // Else, if the item is already in the array, remove it
                            // write your code when user Uchecked the checkbox
                            selectedItems.remove(Integer.valueOf(indexSelected));
                            checkedItemsArray[indexSelected] = false;
                        }
                    }
                })
                // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        saveSelectedItemsToObject(selectedItems);
                        dialog.dismiss();
                        //  Your code when user clicked on OK
                        //  You can write the code  to save the selected item here

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel
                        dialog.dismiss();
                    }
                });

        dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();
    }

    private void saveSelectedItemsToObject(ArrayList seletedItems) {
        List<ExerciseDto> selectedExercises = new ArrayList<>();
        for(int i = 0; i < seletedItems.size(); i++) {
            selectedExercises.add(this.exercisesAll.get((int) seletedItems.get(i)));
        }
        this.workout.setExercises(selectedExercises);
        ListFragment listFragment = ListFragment.newInstance();
        listFragment.setExercisesDto(this.workout.getExercises());
        showFragment(listFragment);
    }

    private void initInputFields(WorkoutDto workout) {

        textInputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        editTextName = (EditText) findViewById(R.id.inputName);
        textInputLayoutName.setHint(getString(R.string.workoutName));
        editTextName.setOnEditorActionListener(EditWorkoutActivity.ActionListener.newInstance(this));

        if(workout != null){
            editTextName.setText(workout.getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //if(!this.readOnly){
            getMenuInflater().inflate(R.menu.add_workout_menu, menu);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveWorkoutButton) {
            saveWorkout();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveWorkout() {
        this.workout.setName(!editTextName.getText().toString().equalsIgnoreCase("") ? editTextName.getText().toString() : null);
        try {
            if(this.workout.getWorkoutId() != 0){
                this.workoutDao.update(this.workout);
            }else{
                this.workoutDao.create(this.workout);
            }

            Intent intent = new Intent();
            intent.putExtra("save", true);
            setResult(RESULT_OK, intent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setReadOnlyMode(){
        this.editTextName.setEnabled(false);
        //this.button.setEnabled(false);
    }

    //TODO validation fields
    private boolean shouldShowError() {
        int textLength = editTextName.getText().length();
        return textLength > 0 && textLength < MIN_TEXT_LENGTH;
    }

    private void showError() {
        editTextName.setError("Eror no");
    }

    private void hideError() {
        editTextName.setError(EMPTY_STRING);
    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<EditWorkoutActivity> mainActivityWeakReference;

        public static EditWorkoutActivity.ActionListener newInstance(EditWorkoutActivity mainActivity) {
            WeakReference<EditWorkoutActivity> mainActivityWeakReference = new WeakReference<>(mainActivity);
            return new EditWorkoutActivity.ActionListener(mainActivityWeakReference);
        }

        private ActionListener(WeakReference<EditWorkoutActivity> mainActivityWeakReference) {
            this.mainActivityWeakReference = mainActivityWeakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            EditWorkoutActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                if (actionId == EditorInfo.IME_ACTION_GO && mainActivity.shouldShowError()) {
                    mainActivity.showError();
                } else {
                    mainActivity.hideError();
                }
            }
            return true;
        }
    }
}