package ru.hogwarts.school2.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.repository.StudentRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student createStudent(Student student) {
        return studentRepository.save(student);

    }

    @Override
    public Student findStudent(long id) {
        return studentRepository.findById(id).orElseThrow();
    }

    @Override
    public void editStudent(long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException();
        }
        student.setId(id);
        studentRepository.save(student);
    }

    @Override
    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow();
        studentRepository.delete(student);
        return student;

    }

    @Override
    public List<Student> filterAllByAge(int age) {
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age)
                .toList();

    }
}
