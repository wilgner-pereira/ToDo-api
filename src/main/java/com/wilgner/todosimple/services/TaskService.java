package com.wilgner.todosimple.services;

import com.wilgner.todosimple.models.Task;
import com.wilgner.todosimple.models.User;
import com.wilgner.todosimple.models.enums.ProfileEnum;
import com.wilgner.todosimple.repositories.TaskRepository;
import com.wilgner.todosimple.security.UserSpringSecurity;
import com.wilgner.todosimple.services.exceptions.AuthorizationException;
import com.wilgner.todosimple.services.exceptions.DataBindingViolationException;
import com.wilgner.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

   @Autowired
   private TaskRepository taskRepository;

   @Autowired
   private UserService userService;

   public Task findByid(Long id){
       Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Task not found! ID: " + id + "Type: " + Task.class.getName()));

       UserSpringSecurity userSpringSecurity = UserService.autheticated();

       if(Objects.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
           throw new AuthorizationException("Acesso negado!");
       return task;
   }

   public List<Task> findByAllUser(){
       UserSpringSecurity userSpringSecurity = UserService.autheticated();
       if(Objects.isNull(userSpringSecurity))
           throw new AuthorizationException("Acesso negado!");

        List<Task> tasks = this.taskRepository.findByUser_id(userSpringSecurity.getId());
        return tasks;
   }

    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.autheticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User user = this.userService.findById(userSpringSecurity.getId());
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

   private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
       return task.getUser().getId().equals(userSpringSecurity.getId());
   }

}
