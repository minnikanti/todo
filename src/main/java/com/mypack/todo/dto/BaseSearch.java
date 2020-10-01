package com.mypack.todo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseSearch {

    private int pageIndex;
    private int pageSize;
    private String sortField;
    private String sortType;
    private String searchTable;
    private List<SearchCriteria> searchCriteria;
}
