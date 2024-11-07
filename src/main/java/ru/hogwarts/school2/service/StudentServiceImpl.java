package ru.hogwarts.school2.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school2.exception.StudentNotFoundException;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.repository.FacultyRepository;
import ru.hogwarts.school2.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

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
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public void editStudent(long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        studentRepository.save(student);
    }

    @Override
    public Student deleteStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;

    }

    @Override
    public List<Student> filterAllByAge(int age) {
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age)
                .toList();

    }

    @Override
    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    @Override
    public List<Student> findStudentsByFacultyId(Long id) {
        return studentRepository.findByFacultyId(id);
    }

}
