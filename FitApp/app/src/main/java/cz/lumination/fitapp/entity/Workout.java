package cz.lumination.fitapp.entity;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mkasl on 03.04.2017.
 */

@DatabaseTable(tableName = "exercises")
public class Workout implements Serializable {

    public final static String WORKOUT_ID = "workout_id";

    @DatabaseField(generatedId = true, columnName = WORKOUT_ID)
    private int workoutId;

    @DatabaseField(columnName = "name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }
}