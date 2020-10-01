package com.mypack.todo.controller;

import com.mypack.todo.dto.BaseSearch;
import com.mypack.todo.dto.ToDoResponse;
import com.mypack.todo.entity.ToDoEntity;
import com.mypack.todo.services.interfaces.ToDoService;
import com.mypack.todo.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/todo")
@Api(value = "ToDO App")
public class ToDoController {

    @Autowired
    private ToDoService toDoService;

    @PostMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "List all the ToDo Items")
    public ResponseEntity<Page<ToDoEntity>> getToDoItems(@RequestBody BaseSearch search) {
        final Page<ToDoEntity> todoPage = toDoService.getToDoList(search);
        return ResponseEntity.ok().body(todoPage);
    }

    @PostMapping(value = "/save", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Save/Update ToDo item in system")
    public ResponseEntity<ToDoResponse> saveToDoItem(@RequestBody ToDoEntity todo) {
        try {
            toDoService.saveToDo(todo);
        } catch (final Exception e) {
            return ResponseEntity.ok().body(new ToDoResponse(HttpStatus.BAD_REQUEST.name(), AppConstants.ITEM_NOT_ADDED_TOLIST));
        }
        return ResponseEntity.ok().body(new ToDoResponse(HttpStatus.OK.name(), AppConstants.ITEM_ADDED_TOLIST));
    }

    @GetMapping(value = "/complete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Mark ToDo item as completed in system")
    public ResponseEntity<ToDoResponse> toggleComplete(@RequestParam(name = "id") Long id, @RequestParam(name = "completed") String completed) {
        toDoService.markItemAsCompleted(id, completed);
        return ResponseEntity.ok().body(new ToDoResponse(HttpStatus.OK.name(), AppConstants.ITEM_COMPLETED));
    }

    @GetMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Delete ToDo item from system")
    public ResponseEntity<ToDoResponse> deleteItem(@PathVariable(name = "id") Long id) {
        toDoService.deleteItem(id);
        return ResponseEntity.ok().body(new ToDoResponse(HttpStatus.OK.name(), AppConstants.ITEM_COMPLETED));
    }

    @PostMapping(value = "/download", produces = "text/csv")
    @ApiOperation(value = "Delete ToDo item from system")
    public void download(@RequestBody BaseSearch search, final HttpServletResponse response) throws IOException {
        List<ToDoEntity> todoList = toDoService.findAll(search);
        response.setContentType("text/csv");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=todolist" + Instant.now().toEpochMilli() + ".csv";
        response.setHeader(headerKey, headerValue);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        String[] csvHeader = {"ID", "Name", "Description", "Expire by Date", "Is Completed"};
        String[] nameMapping = {"id", "name", "description", "expireDate", "isCompleted"};

        csvWriter.writeHeader(csvHeader);

        for (ToDoEntity toDo : todoList) {
            csvWriter.write(toDo, nameMapping);
        }
        csvWriter.close();
    }

    @GetMapping("/")
    @ApiOperation(value = "Swagger UI", hidden = true)
    public RedirectView swagger() {
        return new RedirectView("swagger-ui.html");
    }
}
