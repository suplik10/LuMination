package cz.lumination.fitapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.lang.ref.WeakReference;
import java.sql.SQLException;

import cz.lumination.fitapp.R;
import cz.lumination.fitapp.dao.ExerciseDao;
import cz.lumination.fitapp.dao.impl.ExerciseDaoImpl;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.persistence.DatabaseHelper;

/**
 * Created by mkasl on 01.04.2017.
 */

public class EditExerciseActivity  extends AppCompatActivity {

    private static final int MIN_TEXT_LENGTH = 4;
    private static final String EMPTY_STRING = "";

    private ExerciseDao exerciseDao = new ExerciseDaoImpl();

    private TextInputLayout textInputLayoutLiftingWeight;
    private EditText editTextLiftingWeight;

    private TextInputLayout textInputLayoutRestBetweenSets;
    private EditText editTextRestBetweenSets;

    private TextInputLayout textInputLayoutSets;
    private EditText editTextSets;

    private TextInputLayout textInputLayoutReps;
    private EditText editTextReps;

    private TextInputLayout textInputLayoutName;
    private EditText editTextName;

    private TextInputLayout textInputLayoutType;
    private EditText editTextType;

    private boolean readOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_exercise_activity);

        ExerciseDto exercise = (ExerciseDto) getIntent().getSerializableExtra("exercise");
        this.readOnly = getIntent().getBooleanExtra("readOnly", false);

        initInputFields(exercise);

        if(this.readOnly){
            this.setReadOnlyMode();
        }
    }

    private void initInputFields(ExerciseDto exercise) {
        textInputLayoutLiftingWeight = (TextInputLayout) findViewById(R.id.inputLayoutLiftingWeight);
        editTextLiftingWeight = (EditText) findViewById(R.id.inputLiftingWeight);
        textInputLayoutLiftingWeight.setHint(getString(R.string.liftingWeight));
        editTextLiftingWeight.setOnEditorActionListener(ActionListener.newInstance(this));

        textInputLayoutRestBetweenSets = (TextInputLayout) findViewById(R.id.inputLayoutRestBetweenSets);
        editTextRestBetweenSets = (EditText) findViewById(R.id.inputRestBetweenSets);
        textInputLayoutRestBetweenSets.setHint(getString(R.string.restBetweenSets));
        editTextRestBetweenSets.setOnEditorActionListener(ActionListener.newInstance(this));

        textInputLayoutSets = (TextInputLayout) findViewById(R.id.inputLayoutSets);
        editTextSets = (EditText) findViewById(R.id.inputSets);
        textInputLayoutSets.setHint(getString(R.string.sets));
        editTextSets.setOnEditorActionListener(ActionListener.newInstance(this));

        textInputLayoutReps = (TextInputLayout) findViewById(R.id.inputLayoutReps);
        editTextReps = (EditText) findViewById(R.id.inputReps);
        textInputLayoutReps.setHint(getString(R.string.reps));
        editTextReps.setOnEditorActionListener(ActionListener.newInstance(this));

        textInputLayoutName = (TextInputLayout) findViewById(R.id.inputLayoutName);
        editTextName = (EditText) findViewById(R.id.inputName);
        textInputLayoutName.setHint(getString(R.string.exerciseName));
        editTextName.setOnEditorActionListener(ActionListener.newInstance(this));

        textInputLayoutType = (TextInputLayout) findViewById(R.id.inputLayoutType);
        editTextType = (EditText) findViewById(R.id.inputType);
        textInputLayoutType.setHint(getString(R.string.type));
        editTextType.setOnEditorActionListener(ActionListener.newInstance(this));

        if(exercise != null){
            editTextLiftingWeight.setText(Integer.toString(exercise.getLiftingWeight()));
            editTextRestBetweenSets.setText(Integer.toString(exercise.getRestBetweenSetsInSeconds()));
            editTextSets.setText(Integer.toString(exercise.getSets()));
            editTextReps.setText(Integer.toString(exercise.getReps()));
            editTextName.setText(exercise.getName());
            editTextType.setText(exercise.getType());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(!this.readOnly){
            getMenuInflater().inflate(R.menu.add_exercise_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.saveExerciseButton) {
            saveExercise();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveExercise() {

        ExerciseDto newExercise = new ExerciseDto();

        newExercise.setName(!editTextName.getText().toString().equalsIgnoreCase("") ? editTextName.getText().toString() : null);
        newExercise.setType(!editTextType.getText().toString().equalsIgnoreCase("") ? editTextType.getText().toString() : null);
        newExercise.setReps(!editTextReps.getText().toString().equalsIgnoreCase("") ? Integer.parseInt(editTextReps.getText().toString()) : null);
        newExercise.setSets(!editTextSets.getText().toString().equalsIgnoreCase("") ? Integer.parseInt(editTextSets.getText().toString()) : null);
        newExercise.setRestBetweenSetsInSeconds(!editTextRestBetweenSets.getText().toString().equalsIgnoreCase("") ? Integer.parseInt(editTextRestBetweenSets.getText().toString()) : null);
        newExercise.setLiftingWeight(!editTextLiftingWeight.getText().toString().equalsIgnoreCase("") ? Integer.parseInt(editTextLiftingWeight.getText().toString()) : null);

        try {
            this.exerciseDao.create(newExercise);
            Intent intent = new Intent();
            intent.putExtra("save", true);
            setResult(RESULT_OK, intent);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setReadOnlyMode(){
        editTextLiftingWeight.setEnabled(false);
        editTextRestBetweenSets.setEnabled(false);
        editTextSets.setEnabled(false);
        editTextReps.setEnabled(false);
        editTextName.setEnabled(false);
        editTextType.setEnabled(false);
    }

    //TODO validation fields
    private boolean shouldShowError() {
        int textLength = editTextLiftingWeight.getText().length();
        return textLength > 0 && textLength < MIN_TEXT_LENGTH;
    }

    private void showError() {
        textInputLayoutLiftingWeight.setError("Eror no");
    }

    private void hideError() {
        textInputLayoutLiftingWeight.setError(EMPTY_STRING);
    }

    private static final class ActionListener implements TextView.OnEditorActionListener {
        private final WeakReference<EditExerciseActivity> mainActivityWeakReference;

        public static ActionListener newInstance(EditExerciseActivity mainActivity) {
            WeakReference<EditExerciseActivity> mainActivityWeakReference = new WeakReference<>(mainActivity);
            return new ActionListener(mainActivityWeakReference);
        }

        private ActionListener(WeakReference<EditExerciseActivity> mainActivityWeakReference) {
            this.mainActivityWeakReference = mainActivityWeakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            EditExerciseActivity mainActivity = mainActivityWeakReference.get();
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