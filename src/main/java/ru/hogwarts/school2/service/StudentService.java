package ru.hogwarts.school2.service;

import ru.hogwarts.school2.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentService {
    Student createStudent(Student student);

    Student findStudent(long id);

    Student editStudent(long id, Student student);

    Student deleteStudent(long id);

    List<Student> filterAllByAge(int age);

    List<Student> findByAgeBetween(int ageMin, int ageMax);

    List<Student> findStudentsByFacultyId(Long id);
    Collection<Student> readByFacultyId(long facultyId);

    Collection<String> getFilteredByName();

    Double getAllStudentsByAvgAge();
}
