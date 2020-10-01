package com.mypack.todo.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mypack.todo.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private transient Object value;
    @JsonValue
    private SearchOperation operation;
}
