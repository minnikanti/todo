package com.mypack.todo.services.interfaces;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ToDoService extends BaseService {

    void markItemAsCompleted(Long id, String completed);

}