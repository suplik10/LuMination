package cz.lumination.fitapp.dao;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;

/**
 * Created by mkasl on 01.04.2017.
 */

public interface ExerciseDao {
    public List<WorkoutDto> getWorkoutsForExercise(Exercise exercise) throws SQLException;

    public boolean create(ExerciseDto exerciseDto) throws SQLException;

    public List<ExerciseDto> findAll() throws SQLException;
}
