package com.mypack.todo.services;

import com.mypack.todo.dao.ToDoDao;
import com.mypack.todo.entity.ToDoEntity;
import com.mypack.todo.exceptions.ToDoRunTimeException;
import com.mypack.todo.services.interfaces.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoDao toDoDao;

    @PostConstruct
    public void init() {
        daoMap.put("todo", toDoDao);
    }

    @Override
    public void markItemAsCompleted(final Long id, final String completed) {
        final Optional<ToDoEntity> toDoEntity = toDoDao.findById(id);
        if(toDoEntity.isPresent()) {
            final ToDoEntity entity = toDoEntity.get();
            entity.setIsCompleted(completed);
            toDoDao.save(entity);
        } else {
            throw new ToDoRunTimeException("System not able to find ToDo item with given id:" + id);
        }
    }
}
