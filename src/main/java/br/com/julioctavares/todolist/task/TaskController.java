package br.com.julioctavares.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  @Autowired
  private ITaskRepository taskRepository;

  @PostMapping("/")
  public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
    var userId = request.getAttribute("userId");

    taskModel.setUserId((UUID) userId);

    var currentDate = LocalDateTime.now();

    if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
      return ResponseEntity.badRequest().body("Start Date or End Date is invalid");
    }
    
    if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
      return ResponseEntity.badRequest().body("Start Date need to be before End Date");
    }
    var taskCreated = this.taskRepository.save(taskModel);

    return ResponseEntity.status(201).body(taskCreated);
  }

  @GetMapping("/")
  public List<TaskModel> getByUserId(HttpServletRequest request) {
    var userId = request.getAttribute("userId");

    var tasks = this.taskRepository.findByUserId((UUID) userId);

    return tasks;
  }
}
