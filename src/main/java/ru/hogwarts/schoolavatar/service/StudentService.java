package ru.hogwarts.schoolavatar.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.schoolavatar.entity.Avatar;
import ru.hogwarts.schoolavatar.entity.Faculty;
import ru.hogwarts.schoolavatar.entity.Student;
import ru.hogwarts.schoolavatar.repository.AvatarRepository;
import ru.hogwarts.schoolavatar.repository.FacultyRepository;
import ru.hogwarts.schoolavatar.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student add(Student student) {
        student.setId(null);
        student.setFaculty(
                Optional.ofNullable(student.getFaculty())
                .filter(f -> f.getId() != null)
                .flatMap(f -> facultyRepository.findById(f.getId()))
                .orElse(null)
    );
        return studentRepository.save(student);
    }


    public Optional<Student> update(long id, Student newStudent) {
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(newStudent.getName());
                    oldStudent.setAge(newStudent.getAge());
                    oldStudent.setFaculty(
                            Optional.ofNullable(newStudent.getFaculty())
                                    .filter(f -> f.getId() != null)
                                    .flatMap(f -> facultyRepository.findById(f.getId()))
                                    .orElse(null)
                    );
                    return studentRepository.save(oldStudent);
                });
    }

    public Collection<Student> getALl() {
        return Collections.unmodifiableCollection(studentRepository.findAll());
    }

    public Optional<Student> deleteById(long id) {
     return studentRepository.findById(id)
             .map(student -> {
                 studentRepository.delete(student);
                 return student;
             });
    }


    public Optional<Student> getById(long id) {
        return studentRepository.findById(id);
    }

    public Collection<Student> getAllByAge(int age){
        return studentRepository.findAllByAge(age);}

    public Collection<Student> getAllByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findAllByAgeBetween(minAge, maxAge);
    }

    public Optional<Faculty> getFacultyByStudentId(long id) {
        return studentRepository.findById(id)
                .map(Student::getFaculty);
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.getById(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
