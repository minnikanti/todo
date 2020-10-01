package com.mypack.todo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mypack.todo.util.AppConstants;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TODO")
@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class ToDoEntity extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = AppConstants.SERVER_DATE_FORMAT)
    private LocalDate expireDate;
    private String isCompleted;

    public ToDoEntity() {
        //For JPA
    }
}
