package cz.lumination.fitapp.dao.impl;


import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;

import java.sql.SQLException;
import java.util.List;

import cz.lumination.fitapp.Application;
import cz.lumination.fitapp.activity.MainActivity;
import cz.lumination.fitapp.dao.WorkoutDao;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.dtoMapper.ExerciseMapper;
import cz.lumination.fitapp.dtoMapper.WorkoutMapper;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;
import cz.lumination.fitapp.entity.WorkoutExercise;
import cz.lumination.fitapp.persistence.DatabaseHelper;

/**
 * Created by mkasl on 03.04.2017.
 */

public class WorkoutDaoImpl implements WorkoutDao {

    @Override
    public List<ExerciseDto> getExercisesForWorkout(Workout workout) throws SQLException {

        PreparedQuery<Exercise> exercixesForWorkoutQuery = null;

        if (exercixesForWorkoutQuery == null) {
            exercixesForWorkoutQuery = makeExercisesForWorkoutQuery();
        }
        try {
            exercixesForWorkoutQuery.setArgumentHolderValue(0, workout);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Exercise, Integer> exerciseDao;
        exerciseDao = databaseHelper.getExerciseDao();

        return ExerciseMapper.entityListToDtoList(exerciseDao.query(exercixesForWorkoutQuery));
    }

    @Override
    public boolean create(WorkoutDto workoutDto) throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Workout, Integer> workoutDao;
        Workout workoutEntity;

        workoutDao = databaseHelper.getWorkoutDao();


        //create entity
        workoutEntity = WorkoutMapper.dtoToEntity(workoutDto);
        workoutDao.create(workoutEntity);

        //create exercises
        //TODO exercises exists already now

        //create workExercise
        if(workoutDto.getExercises() != null && workoutDto.getExercises().size() > 0){
            final Dao<WorkoutExercise, Integer> workoutExercisesDao;
            workoutExercisesDao = databaseHelper.getWorkoutExercisesDao();

            for(int i = 0; i < workoutDto.getExercises().size(); i++) {
                workoutExercisesDao.create(new WorkoutExercise(ExerciseMapper.dtoToEntity(workoutDto.getExercises().get(i)),workoutEntity));
            }
        }

        OpenHelperManager.releaseHelper();

        return true;
    }

    @Override
    public boolean update(WorkoutDto workoutDto) throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Workout, Integer> workoutDao;
        final Dao<WorkoutExercise, Integer> workoutExercisesDao;
        Workout workoutEntity;

        workoutDao = databaseHelper.getWorkoutDao();
        workoutExercisesDao = databaseHelper.getWorkoutExercisesDao();

        workoutEntity = WorkoutMapper.dtoToEntity(workoutDto);
        workoutDao.update(workoutEntity);

        //delete workExercise
        //TODO dont delete previous data
        DeleteBuilder<WorkoutExercise, Integer> deleteBuilder = workoutExercisesDao.deleteBuilder();
        deleteBuilder.where().eq(WorkoutExercise.WORKOUT_ID_FIELD_NAME, workoutEntity);
        deleteBuilder.delete();

        //create workExercise
        if(workoutDto.getExercises() != null && workoutDto.getExercises().size() > 0){
            for(int i = 0; i < workoutDto.getExercises().size(); i++) {
                workoutExercisesDao.create(new WorkoutExercise(ExerciseMapper.dtoToEntity(workoutDto.getExercises().get(i)),workoutEntity));
            }
        }



        OpenHelperManager.releaseHelper();

        return true;
    }

    private PreparedQuery<Exercise> makeExercisesForWorkoutQuery() throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<WorkoutExercise, Integer> workoutExerciseDao;
        workoutExerciseDao = databaseHelper.getWorkoutExercisesDao();

        // build our inner query for UserPost objects
        QueryBuilder<WorkoutExercise, Integer> workoutExerciseQb = workoutExerciseDao.queryBuilder();
        // just select the post-id field
        workoutExerciseQb.selectColumns(WorkoutExercise.EXERCISE_ID_FIELD_NAME);
        SelectArg workoutSelectArg = new SelectArg();
        // you could also just pass in user1 here
        workoutExerciseQb.where().eq(WorkoutExercise.WORKOUT_ID_FIELD_NAME, workoutSelectArg);

        // build our outer query for Post objects
        final Dao<Exercise, Integer> exerciseDao;
        exerciseDao = databaseHelper.getExerciseDao();

        QueryBuilder<Exercise, Integer> exerciseQb = exerciseDao.queryBuilder();
        // where the id matches in the post-id from the inner query
        exerciseQb.where().in(Exercise.EXERCISE_ID, workoutExerciseQb);
        return exerciseQb.prepare();
    }

    @Override
    public List<WorkoutDto> findAll() throws SQLException {
        DatabaseHelper databaseHelper = OpenHelperManager.getHelper(Application.getContext(),DatabaseHelper.class);
        final Dao<Workout, Integer> workoutDao;
        workoutDao = databaseHelper.getWorkoutDao();

        return WorkoutMapper.entityListToDtoList(workoutDao.queryForAll());
    }
}
