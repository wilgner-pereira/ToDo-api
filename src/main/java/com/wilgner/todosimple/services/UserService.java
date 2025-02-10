package com.wilgner.todosimple.services;

import com.wilgner.todosimple.models.User;
import com.wilgner.todosimple.models.enums.ProfileEnum;
import com.wilgner.todosimple.repositories.TaskRepository;
import com.wilgner.todosimple.repositories.UserRepository;
import com.wilgner.todosimple.services.exceptions.DataBindingViolationException;
import com.wilgner.todosimple.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UserService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    public User findById(Long id){
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow( ()-> new ObjectNotFoundException("User not found" + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj){
        obj.setId(null);
        obj.setPassword(bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(bCryptPasswordEncoder.encode(newObj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try{
            this.userRepository.deleteById(id);
        }catch(Exception e){
            throw new DataBindingViolationException("Não é possível excluir por haver entidades relacionadas");
        }
    }


}
