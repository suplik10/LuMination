package cz.lumination.fitapp.dtoMapper;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.dao.ExerciseDao;
import cz.lumination.fitapp.dao.WorkoutDao;
import cz.lumination.fitapp.dao.impl.ExerciseDaoImpl;
import cz.lumination.fitapp.dao.impl.WorkoutDaoImpl;
import cz.lumination.fitapp.dto.ExerciseDto;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.entity.Exercise;
import cz.lumination.fitapp.entity.Workout;

/**
 * Created by mkasl on 04.04.2017.
 */

public class ExerciseMapper {

    public static Exercise dtoToEntity(ExerciseDto exerciseDto){
        Exercise exerciseEntity = new Exercise();
        exerciseEntity.setName(exerciseDto.getName());
        exerciseEntity.setLiftingWeight(exerciseDto.getLiftingWeight());
        exerciseEntity.setExerciseId(exerciseDto.getExerciseId());
        exerciseEntity.setRestBetweenSetsInSeconds(exerciseDto.getRestBetweenSetsInSeconds());
        exerciseEntity.setSets(exerciseDto.getSets());
        exerciseEntity.setReps(exerciseDto.getReps());
        exerciseEntity.setType(exerciseDto.getType());

        return exerciseEntity;
    }

    public static ExerciseDto entityToDto(Exercise exerciseEntity){
        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setExerciseId(exerciseEntity.getExerciseId());
        exerciseDto.setName(exerciseEntity.getName());
        exerciseDto.setReps(exerciseEntity.getReps());
        exerciseDto.setSets(exerciseEntity.getSets());
        exerciseDto.setRestBetweenSetsInSeconds(exerciseEntity.getRestBetweenSetsInSeconds());
        exerciseDto.setLiftingWeight(exerciseEntity.getLiftingWeight());
        exerciseDto.setType(exerciseEntity.getType());

        ExerciseDao exerciseDao = new ExerciseDaoImpl();
        try {
            exerciseDto.setWorkouts(exerciseDao.getWorkoutsForExercise(exerciseEntity));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exerciseDto;
    }

    public static List<Exercise> dtoListToEntityList (@NotNull List<ExerciseDto> exerciseDtos){
        List<Exercise> exercises = new ArrayList<>();

        for(int i = 0; i < exerciseDtos.size(); i++) {
            exercises.add(ExerciseMapper.dtoToEntity(exerciseDtos.get(i)));
        }
        return exercises;
    }

    public static List<ExerciseDto> entityListToDtoList (@NotNull List<Exercise> exerciseEntities){
        List<ExerciseDto> exerciseDtos = new ArrayList<>();

        for(int i = 0; i < exerciseEntities.size(); i++) {
            exerciseDtos.add(ExerciseMapper.entityToDto(exerciseEntities.get(i)));
        }
        return exerciseDtos;
    }
}
