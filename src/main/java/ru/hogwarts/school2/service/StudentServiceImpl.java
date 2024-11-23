package ru.hogwarts.school2.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.hogwarts.school2.exception.StudentNotFoundException;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.repository.StudentRepository;

import java.util.List;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

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

    @Override
    public Collection<Student> readByFacultyId(long facultyId) {
        logger.info("Отработал метод readByFacultyId");
        return studentRepository.findByFacultyId(facultyId);
    }

    @Override
    public Collection<String> getFilteredByName(){
        logger.info("Отработал метод getFilteredByName");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }
    @Override
    public Double getAllStudentsByAvgAge(){
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    @Override
    public void getNames() {
        logger.info("Отработал метод getNames");
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6) {
            logger.warn("Недостаточно студентов");
            return;
        }

        System.out.println("Основной поток:");
        System.out.println(students.get(0).getName());
        System.out.println(students.get(1).getName());


        Thread thread1 = new Thread(() -> {
            System.out.println("Второй поток:");
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Третий поток:");
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании завершения потоков: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void getNamesSync() {
        logger.info("Отработал метод getNamesSync");
        List<Student> students = studentRepository.findAll();
        if (students.size() < 6) {
            logger.warn("Недостаточно студентов");
            return;
        }

        printNameSynchronized(students.get(0));
        printNameSynchronized(students.get(1));

        Thread thread1 = new Thread(() -> {
            printNameSynchronized(students.get(2));
            printNameSynchronized(students.get(3));
        });

        Thread thread2 = new Thread(() -> {
            printNameSynchronized(students.get(4));
            printNameSynchronized(students.get(5));
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Ошибка при ожидании завершения потоков: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void printNameSynchronized(Student student) {
        try {
            System.out.println("Поток " + Thread.currentThread().getName() +
                    ": Студент - " + student.getName());
        } catch (Exception e) {
            logger.error("Ошибка при выводе имени студента: {}", e.getMessage());
        }
    }

}
