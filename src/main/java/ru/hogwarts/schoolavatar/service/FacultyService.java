package ru.hogwarts.schoolavatar.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.schoolavatar.entity.Faculty;
import ru.hogwarts.schoolavatar.entity.Student;
import ru.hogwarts.schoolavatar.repository.FacultyRepository;
import ru.hogwarts.schoolavatar.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Optional<Faculty> getById(long id) {
        return facultyRepository.findById(id);
    }


    public Faculty add(Faculty faculty) {
        faculty.setId(null);
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> getAll() {
        return Collections.unmodifiableCollection(facultyRepository.findAll());
    }

    public Optional<Faculty> update(long id, Faculty newFaculty) {
        return facultyRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(newFaculty.getName());
                    oldStudent.setColor(newFaculty.getColor());
                    return facultyRepository.save(oldStudent);
                });
    }

    public Optional<Faculty> deleteById(long id) {
        return facultyRepository.findById(id)
                .map(faculty -> {
                    facultyRepository.delete(faculty);
                    return faculty;
                });
    }
    public Collection<Faculty> getAllByColor(String color) {
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> getAllByNameOrColor(String nameOrColor) {
        return facultyRepository.findAllByNameContainsIgnoreCaseOrColorContainsIgnoreCase
                (nameOrColor, nameOrColor);
    }

    public List<Student> getStudentByFacultyId(long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }

}
