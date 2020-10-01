package com.mypack.todo.services.interfaces;

import com.mypack.todo.dao.BaseDao;
import com.mypack.todo.dto.BaseSearch;
import com.mypack.todo.entity.BaseEntity;
import com.mypack.todo.util.ToDoJpaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseService<T extends BaseEntity> {
    default Page<T> findAllWithPagination(final BaseDao<T> dao, final BaseSearch search) {
        final ToDoJpaSpecification<T> specification = new ToDoJpaSpecification();
        specification.add(search.getSearchCriteria());

        final Sort sort = Sort.by(Sort.Direction.fromString(search.getSortType()), search.getSortField());
        final Pageable paging = PageRequest.of(search.getPageIndex(), search.getPageSize(), sort);

        return dao.findAll(specification, paging);
    }

    default List<T> findAll(final BaseDao<T> dao, final BaseSearch search) {
        final ToDoJpaSpecification<T> specification = new ToDoJpaSpecification();
        specification.add(search.getSearchCriteria());

        return dao.findAll(specification);
    }
}
