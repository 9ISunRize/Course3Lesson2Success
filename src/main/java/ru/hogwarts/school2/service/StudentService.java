package ru.hogwarts.school2.service;

import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student createStudent(Student student);

    Student findStudent(long id);

    void editStudent(long id, Student student);

    Student deleteStudent(long id);

    List<Student> filterAllByAge(int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);
    List <Student> findStudentsByFacultyId(Long id);


}
