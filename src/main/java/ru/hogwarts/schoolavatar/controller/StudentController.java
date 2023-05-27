package ru.hogwarts.schoolavatar.controller;


import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolavatar.entity.Faculty;
import ru.hogwarts.schoolavatar.entity.Student;
import ru.hogwarts.schoolavatar.service.StudentService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public Optional<Student> getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping
    public Student add(@RequestBody Student student) {
        return studentService.add(student);
    }

    @PutMapping
    public Optional<Student> update(@RequestBody long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("{id}")
    public Optional<Student> deleteById(@PathVariable Long id) {
        return studentService.deleteById(id);
    }

    @GetMapping
    public Collection<Student> getAll(@RequestParam(value = "age", required = false) Integer age) {
        return Optional.ofNullable(age)
                .map(studentService::getAllByAge)
                .orElseGet(studentService::getALl);
    }

    @GetMapping(params = {"min", "max"})
    public Collection<Student> getAllByAgeBetween(@RequestParam("min") int minAge,
                                                  @RequestParam ("max") int maxAge) {
        return studentService.getAllByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public Optional<Faculty> getFacultyByStudentId(@PathVariable long id) {
        return studentService.getFacultyByStudentId(id);
    }


}
