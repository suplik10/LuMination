package cz.lumination.fitapp.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by mkasl on 01.04.2017.
 */
@DatabaseTable(tableName = "exercises")
public class Exercise implements Serializable {

    public final static String EXERCISE_ID = "exercise_id";

    @DatabaseField(generatedId = true, columnName = EXERCISE_ID)
    private int exerciseId;

    @DatabaseField(columnName = "name")
    private String name;

    //TODO add to enum
    @DatabaseField(columnName = "type")
    private String type;

    @DatabaseField(columnName = "sets")
    private int sets;

    @DatabaseField(columnName = "reps")
    private int reps;

    @DatabaseField(columnName = "restBetweenSetsInSeconds")
    private int restBetweenSetsInSeconds;

    @DatabaseField(columnName = "liftingWeight")
    private int liftingWeight;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRestBetweenSetsInSeconds() {
        return restBetweenSetsInSeconds;
    }

    public void setRestBetweenSetsInSeconds(int restBetweenSetsInSeconds) {
        this.restBetweenSetsInSeconds = restBetweenSetsInSeconds;
    }

    public int getLiftingWeight() {
        return liftingWeight;
    }

    public void setLiftingWeight(int liftingWeight) {
        this.liftingWeight = liftingWeight;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

}
