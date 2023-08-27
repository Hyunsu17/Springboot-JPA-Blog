package com.cos.blog.controller.api;

import com.cos.blog.common.JsonBinder;
import com.cos.blog.common.JsonKeyName;
import com.cos.blog.model.Routine;
import com.cos.blog.model.User;
import com.cos.blog.model.WorkoutElement;
import com.cos.blog.model.WorkoutSet;
import com.cos.blog.service.RoutineService;
import com.cos.blog.service.UserService;
import com.cos.blog.service.WorkoutElementService;
import com.cos.blog.service.WorkoutSetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RoutineApiController {

    private final RoutineService routineService;
    private final WorkoutElementService workoutElementService;
    private final UserService userService;
    private final WorkoutSetService workoutSetService;

    @PostMapping("/routine")
    public ResponseEntity<Object> createRoutine(@RequestBody JsonNode saveObj) {
        Routine bufferedRoutine, routine;
        User bufferedUser, user;
        List<WorkoutElement> bufferedWorkoutElements;
        List<WorkoutElement> workoutElements = new ArrayList<>();
        List<WorkoutSet> bufferedWorkoutSets = new ArrayList<>();
        List<WorkoutSet> workoutSets = new ArrayList<>();

        try {
            bufferedUser = JsonBinder.JsonToModel(saveObj, User.class);
            user = userService.회원찾기(bufferedUser.getUsername());

            bufferedRoutine = JsonBinder.JsonToModel(saveObj, Routine.class);
            routine = Routine.builder().user(user)
                    .name(bufferedRoutine.getName())
                    .build();
            routineService.saveRoutine(routine);

            bufferedWorkoutElements = JsonBinder.JsonListToModelList(saveObj, WorkoutElement.class);
            bufferedWorkoutElements.forEach((element) -> {
                workoutElements.add(WorkoutElement.builder()
                        .workoutName(element.getWorkoutName())
                        .routine(element.getRoutine())
                        .build());
            });
            workoutElementService.saveMultipleElements(workoutElements);

            JsonNode newNode = saveObj.get("workoutSet");
            for (JsonNode jsa : newNode) {
                String elementName = jsa.get("elementName").asText();
                int reps = jsa.get("reps").asInt();
                boolean status = jsa.get("status").asBoolean();
                int weight = jsa.get("weight").asInt();
                WorkoutElement workoutElement = WorkoutElement.builder().workoutName(elementName).build();
                bufferedWorkoutSets.add(WorkoutSet.builder().reps(reps).status(status).weight(weight).workOutElement(workoutElement).build());
            }
            bufferedWorkoutSets.forEach((element) -> {
                workoutSets.add(WorkoutSet.builder()
                        .workOutElement(workoutElementService.findByWorkoutName(element.getWorkOutElement().getWorkoutName()))
                        .status(element.getStatus())
                        .reps(element.getReps())
                        .weight(element.getWeight())
                        .build());
            });

            workoutSetService.saveMultipleSet(workoutSets);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Server Error");
        }

        return ResponseEntity.ok().body("Successfully Created");

    }


}
