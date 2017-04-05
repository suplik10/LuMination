package cz.lumination.fitapp.dao;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;

/**
 * Created by mkasl on 03.04.2017.
 */

public interface WorkoutDao {
    public List<ExerciseDto> getExercisesForWorkout(Workout workout) throws SQLException;

    public boolean create(WorkoutDto workoutDto) throws SQLException;

    public boolean update(WorkoutDto workoutDto) throws SQLException;

    public List<WorkoutDto> findAll() throws SQLException;
}
