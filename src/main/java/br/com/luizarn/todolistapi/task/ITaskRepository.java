package br.com.luizarn.todolistapi.task;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ITaskRepository extends JpaRepository<TaskModel, Integer>{
    List<TaskModel> findByUserId(int userId);
}
