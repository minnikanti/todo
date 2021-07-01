package com.mypack.todo.services.interfaces;

import com.mypack.todo.dao.BaseDao;
import com.mypack.todo.dto.BaseSearch;
import com.mypack.todo.entity.BaseEntity;
import com.mypack.todo.exceptions.ToDoRunTimeException;
import com.mypack.todo.util.AppConstants;
import com.mypack.todo.util.ToDoJpaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BaseService<T extends BaseEntity> {

    Map<String, BaseDao> daoMap = new HashMap<>();

    default BaseDao getDao(final String searchTable) {
        final BaseDao dao = this.daoMap.get(searchTable);
        if (dao == null) {
            throw new ToDoRunTimeException(AppConstants.DAO_MISSING + searchTable);
        }
        return dao;
    }

    default ToDoJpaSpecification<T> getSpecification(BaseSearch search) {
        final ToDoJpaSpecification<T> specification = new ToDoJpaSpecification();
        specification.add(search.getSearchCriteria());
        return specification;
    }

    default Page findAllWithPagination(final String dao, final BaseSearch search) {
        final Sort sort = Sort.by(Sort.Direction.fromString(search.getSortType()), search.getSortField());
        final Pageable paging = PageRequest.of(search.getPageIndex(), search.getPageSize(), sort);

        return getDao(dao).findAll(getSpecification(search), paging);
    }

    default List<T> findAll(final String dao, final BaseSearch search) {
        return getDao(dao).findAll(getSpecification(search));
    }

    default T save(final String dao, final T entity) {
        return (T) getDao(dao).save(entity);
    }

    default void delete(final String dao, final Long entity) {
        getDao(dao).delete(entity);
    }
}
