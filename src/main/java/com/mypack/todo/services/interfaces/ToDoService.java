package com.mypack.todo.services.interfaces;

import com.mypack.todo.dto.BaseSearch;
import com.mypack.todo.entity.ToDoEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ToDoService extends BaseService {

    Page<ToDoEntity> getToDoList(BaseSearch search);

    ToDoEntity saveToDo(ToDoEntity todo);

    void markItemAsCompleted(Long id, String completed);

    void deleteItem(Long id);

    List<ToDoEntity> findAll(BaseSearch search);
}
