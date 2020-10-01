package com.mypack.todo.services;

import com.mypack.todo.dao.BaseDao;
import com.mypack.todo.dao.ToDoDao;
import com.mypack.todo.dto.BaseSearch;
import com.mypack.todo.entity.ToDoEntity;
import com.mypack.todo.exceptions.ToDoRunTimeException;
import com.mypack.todo.services.interfaces.ToDoService;
import com.mypack.todo.util.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    private ToDoDao toDoDao;

    private final Map<String, BaseDao> daoMap = new HashMap<>();

    @PostConstruct
    public void init() {
        daoMap.put("todo", toDoDao);
    }

    private BaseDao getDao(final String searchTable) {
        final BaseDao dao = this.daoMap.get(searchTable);
        if (dao == null) {
            throw new ToDoRunTimeException(AppConstants.DAO_MISSING + searchTable);
        }
        return dao;
    }

    @Override
    public Page<ToDoEntity> getToDoList(final BaseSearch search) {
        return this.findAllWithPagination(getDao(search.getSearchTable()), search);
    }

    @Override
    public ToDoEntity saveToDo(final ToDoEntity todo) {
        return toDoDao.save(todo);
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

    @Override
    public void deleteItem(Long id) {
        toDoDao.deleteById(id);
    }

    @Override
    public List<ToDoEntity> findAll(BaseSearch search) {
        return this.findAll(getDao(search.getSearchTable()), search);
    }
}
