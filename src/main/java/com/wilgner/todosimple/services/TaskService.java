package com.wilgner.todosimple.services;

import com.wilgner.todosimple.models.Task;
import com.wilgner.todosimple.models.User;
import com.wilgner.todosimple.repositories.TaskRepository;
import com.wilgner.todosimple.services.exceptions.DataBindingViolationException;
import com.wilgner.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

   @Autowired
   private TaskRepository taskRepository;

   @Autowired
    public UserService userService;

   public Task findByid(Long id){
       Optional<Task> task = this.taskRepository.findById(id);
       return task.orElseThrow(() -> new ObjectNotFoundException("Task not found! ID: " + id + "Type: " + Task.class.getName()));
   }

   public List<Task> findByAllUserId(Long userId){
        List<Task> tasks = this.taskRepository.findByUser_id(userId);
        return tasks;
   }

   @Transactional
   public Task create(Task obj){
       User user = this.userService.findById(obj.getUser().getId());
       obj.setId(null);
       obj.setUser(user);
       obj = this.taskRepository.save(obj);
       return obj;
   }

   public Task update(Task obj){
       Task newObj = findByid(obj.getId());
       newObj.setDescription(obj.getDescription());
       return this.taskRepository.save(newObj);
   }

   public void delete(Long id){
       findByid(id);
       try{
           this.taskRepository.deleteById(id);
       }catch(Exception e){
            throw new DataBindingViolationException("Nãa é possivel deletar pois há entidades relacionadas");
       }
   }

}
