package ru.hogwarts.schoolavatar.controller;


import org.springframework.web.bind.annotation.*;
import ru.hogwarts.schoolavatar.entity.Faculty;
import ru.hogwarts.schoolavatar.entity.Student;
import ru.hogwarts.schoolavatar.service.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public Optional<Faculty> getById(@PathVariable Long id) {
        return facultyService.getById(id);
    }
    @PostMapping
    public Faculty add(@RequestBody Faculty faculty) {
        return facultyService.add(faculty);
    }

    @PutMapping("{id}")
    public Optional<Faculty> update(@PathVariable long id, @RequestBody Faculty faculty) {
        return facultyService.update(id, faculty);
    }

    @DeleteMapping("{id}")
    public Optional<Faculty> deleteById(@PathVariable Long id) {
        return facultyService.deleteById(id);
    }

    @GetMapping(params = "color")
    public Collection<Faculty> getALl(@RequestParam(value = "color", required = false) String color) {
        return Optional.ofNullable(color)
                .map(facultyService::getAllByColor)
                .orElseGet(facultyService::getAll);
    }

    @GetMapping(params = "nameOrColor")
    public Collection<Faculty> getAllByNameOrColor(@RequestParam("nameOrColor") String nameOrColor)   {
        return facultyService.getAllByNameOrColor(nameOrColor);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudentByFacultyId(@PathVariable long id) {
        return facultyService.getStudentByFacultyId(id);
    }


}
