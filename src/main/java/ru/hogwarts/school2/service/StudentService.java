package ru.hogwarts.school2.service;

import ru.hogwarts.school2.model.Student;

import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(Student student);

    Student deleteStudent(long id);

    List<Student> filterAllByAge (int age);
}
