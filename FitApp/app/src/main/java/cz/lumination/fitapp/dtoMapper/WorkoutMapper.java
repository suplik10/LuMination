package cz.lumination.fitapp.dtoMapper;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.lumination.fitapp.dao.WorkoutDao;
import cz.lumination.fitapp.dao.impl.WorkoutDaoImpl;
import cz.lumination.fitapp.dto.WorkoutDto;
import cz.lumination.fitapp.entity.Workout;

/**
 * Created by mkasl on 03.04.2017.
 */

public class WorkoutMapper {

    public static Workout dtoToEntity(WorkoutDto workoutDto){
        Workout workoutEntity = new Workout();
        workoutEntity.setName(workoutDto.getName());
        workoutEntity.setWorkoutId(workoutDto.getWorkoutId());

        return workoutEntity;
    }

    public static WorkoutDto entityToDto(Workout workoutEntity){
        WorkoutDto workoutDto = new WorkoutDto();
        workoutDto.setWorkoutId(workoutEntity.getWorkoutId());
        workoutDto.setName(workoutEntity.getName());

        WorkoutDao workoutDao = new WorkoutDaoImpl();
        try {
            workoutDto.setExercises(workoutDao.getExercisesForWorkout(workoutEntity));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workoutDto;
    }

    public static List<WorkoutDto> entityListToDtoList (@NotNull List<Workout> workoutsEntities){
        List<WorkoutDto> workoutDtos = new ArrayList<>();

        for(int i = 0; i < workoutsEntities.size(); i++) {
            workoutDtos.add(WorkoutMapper.entityToDto(workoutsEntities.get(i)));
        }
        return workoutDtos;
    }
}
