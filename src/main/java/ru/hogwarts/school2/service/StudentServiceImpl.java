package ru.hogwarts.school2.service;

import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school2.exception.StudentNotFoundException;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.repository.StudentRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    private final Logger logger = (Logger) LoggerFactory.getLogger(AvatarServiceImpl.class);


    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;

    }

    @Override
    public Student createStudent(Student student) {
        logger.info("Отработал метод createStudent");
        return studentRepository.save(student);

    }

    @Override
    public Student findStudent(long id) {
        logger.info("Отработал метод findStudent");
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @Override
    public Student editStudent(long id, Student student) {
        logger.info("Отработал метод editStudent");
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        student.setId(id);
        studentRepository.save(student);
        return student;
    }

    @Override
    public Student deleteStudent(long id) {
        logger.info("Отработал метод deleteStudent");
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return student;

    }

    @Override
    public List<Student> filterAllByAge(int age) {
        logger.info("Отработал метод filterAllByAge");
        return studentRepository.findAll().stream().
                filter(student -> student.getAge() == age)
                .toList();

    }

    @Override
    public List<Student> findByAgeBetween(int ageMin, int ageMax) {
        logger.info("Отработал метод findByAgeBetween");
        return studentRepository.findByAgeBetween(ageMin, ageMax);
    }

    @Override
    public List<Student> findStudentsByFacultyId(Long id) {
        logger.info("Отработал метод findStudentsByFacultyId");
        return studentRepository.findByFacultyId(id);
    }

}
