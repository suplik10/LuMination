package cz.lumination.fitapp.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mkasl on 03.04.2017.
 */

public class WorkoutExercise {

    public final static String EXERCISE_ID_FIELD_NAME = "exercise_id_FK";
    public final static String WORKOUT_ID_FIELD_NAME = "workout_id_FK";

    @DatabaseField(foreign = true, columnName = EXERCISE_ID_FIELD_NAME)
    Exercise exercise;

    @DatabaseField(foreign = true, columnName = WORKOUT_ID_FIELD_NAME)
    Workout workout;

    public WorkoutExercise() {
    }

    public WorkoutExercise(Exercise exercise, Workout workout) {
        this.exercise = exercise;
        this.workout = workout;
    }
}
