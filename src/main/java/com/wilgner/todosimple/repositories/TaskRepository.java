package com.wilgner.todosimple.repositories;

import com.wilgner.todosimple.models.Task;
import com.wilgner.todosimple.models.projection.TaskProjection;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

   List<TaskProjection> findByUser_id(Long id);

//    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
//    List<Task> procurarporID(@Param("id") Long id);

//    @Query(value = "SELECT * FROM task  t WHERE t.user_id = :id", nativeQuery = true)
//    List<Task> procurarporID(@Param("id") Long id);

}
