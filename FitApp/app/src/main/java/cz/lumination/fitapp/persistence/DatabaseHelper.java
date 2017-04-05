package cz.lumination.fitapp.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import cz.lumination.fitapp.R;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;
import cz.lumination.fitapp.entity.WorkoutExercise;

/**
 * Created by mkasl on 01.04.2017.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "fitApp.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<Exercise, Integer> exerciseDao;
    private Dao<Workout, Integer> workoutDao;
    private Dao<WorkoutExercise, Integer> workoutExercisesDao;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {

            // Create tables. This onCreate() method will be invoked only once of the application life time i.e. the first time when the application starts.
            TableUtils.createTable(connectionSource, Exercise.class);
            TableUtils.createTable(connectionSource, Workout.class);
            TableUtils.createTable(connectionSource, WorkoutExercise.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to create databases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
        try {

            // In case of change in database of next version of application, please increase the value of DATABASE_VERSION variable, then this method will be invoked
            //automatically. Developer needs to handle the upgrade logic here, i.e. create a new table or a new column to an existing table, take the backups of the
            // existing database etc.

            TableUtils.dropTable(connectionSource, Exercise.class, true);
            TableUtils.dropTable(connectionSource, Workout.class, true);
            TableUtils.dropTable(connectionSource, WorkoutExercise.class, true);
            onCreate(sqliteDatabase, connectionSource);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
                    + newVer, e);
        }
    }

    // Create the getDao methods of all database tables to access those from android code.
    // Insert, delete, read, update everything will be happened through DAOs

    public Dao<Exercise, Integer> getExerciseDao() throws SQLException {
        if (exerciseDao == null) {
            exerciseDao = getDao(Exercise.class);
        }

        return exerciseDao;
    }

    public Dao<Workout, Integer> getWorkoutDao() throws SQLException {
        if (workoutDao == null) {
            workoutDao = getDao(Workout.class);
        }

        return workoutDao;
    }

    public Dao<WorkoutExercise, Integer> getWorkoutExercisesDao() throws SQLException {
        if (workoutExercisesDao == null) {
            workoutExercisesDao = getDao(WorkoutExercise.class);
        }

        return workoutExercisesDao;
    }
}