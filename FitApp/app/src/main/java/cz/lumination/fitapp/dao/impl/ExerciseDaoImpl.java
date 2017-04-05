package cz.lumination.fitapp.dao.impl;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import cz.lumination.fitapp.Application;
import cz.lumination.fitapp.dao.ExerciseDao;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.dtoMapper.ExerciseMapper;
import cz.lumination.fitapp.dtoMapper.WorkoutMapper;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;
import cz.lumination.fitapp.entity.WorkoutExercise;
import cz.lumination.fitapp.persistence.DatabaseHelper;

/**
 * Created by mkasl on 04.04.2017.
 */

public class ExerciseDaoImpl implements ExerciseDao{

    @Override
    public List<WorkoutDto> getWorkoutsForExercise(Exercise exercise) throws SQLException {
        PreparedQuery<Workout> workoutsForExerciseQuery = null;

        if (workoutsForExerciseQuery == null) {
            workoutsForExerciseQuery =  makeWorkoutsForExerciseQuery();
        }
        try {
            workoutsForExerciseQuery.setArgumentHolderValue(0, exercise);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Workout, Integer> workoutDao;
        workoutDao = databaseHelper.getWorkoutDao();

        return WorkoutMapper.entityListToDtoList(workoutDao.query(workoutsForExerciseQuery));
    }

    @Override
    public boolean create(ExerciseDto exerciseDto) throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Exercise, Integer> exerciseDao;
        Exercise exerciseEntity;

        exerciseDao = databaseHelper.getExerciseDao();


        //create entity
        exerciseEntity = ExerciseMapper.dtoToEntity(exerciseDto);
        int id = exerciseDao.create(exerciseEntity);

        //create exercises
        //TODO exercises exists already now

        //create workExercise
        if(exerciseDto.getWorkouts() != null && exerciseDto.getWorkouts().size() > 0){
            final Dao<WorkoutExercise, Integer> workoutExercisesDao;
            workoutExercisesDao = databaseHelper.getWorkoutExercisesDao();

            for(int i = 0; i < exerciseDto.getWorkouts().size(); i++) {
                workoutExercisesDao.create(new WorkoutExercise(exerciseEntity, WorkoutMapper.dtoToEntity(exerciseDto.getWorkouts().get(i))));
            }
        }

        OpenHelperManager.releaseHelper();

        return true;
    }

    @Override
    public List<ExerciseDto> findAll() throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Exercise, Integer> exerciseDao;
        exerciseDao = databaseHelper.getExerciseDao();

        return ExerciseMapper.entityListToDtoList(exerciseDao.queryForAll());
    }

    private PreparedQuery<Workout> makeWorkoutsForExerciseQuery() throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<WorkoutExercise, Integer> workoutExerciseDao;
        workoutExerciseDao = databaseHelper.getWorkoutExercisesDao();

        // build our inner query for UserPost objects
        QueryBuilder<WorkoutExercise, Integer> workoutExerciseQb = workoutExerciseDao.queryBuilder();
        // just select the post-id field
        workoutExerciseQb.selectColumns(WorkoutExercise.EXERCISE_ID_FIELD_NAME);
        SelectArg exerciseSelectArg = new SelectArg();
        // you could also just pass in user1 here
        workoutExerciseQb.where().eq(WorkoutExercise.WORKOUT_ID_FIELD_NAME, exerciseSelectArg);

        // build our outer query for Post objects
        final Dao<Workout, Integer> workoutDao;
        workoutDao = databaseHelper.getWorkoutDao();

        QueryBuilder<Workout, Integer> workoutQb = workoutDao.queryBuilder();
        // where the id matches in the post-id from the inner query
        workoutQb.where().in(Workout.WORKOUT_ID, workoutExerciseQb);
        return workoutQb.prepare();
    }
}
