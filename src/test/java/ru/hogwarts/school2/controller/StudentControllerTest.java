package ru.hogwarts.school2.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;


    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void testCreateStudent() {
        // Given
        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(11);

        // When
        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                student,
                Student.class
        );
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гарри Поттер");
        assertThat(response.getBody().getAge()).isEqualTo(11);
    }

    @Test
    void testGetStudent() {
        // Given
        Student student = createTestStudent();

        // When
        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/{id}/get",
                Student.class,
                student.getId()
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(student.getId());
    }

    @Test
    void testEditStudent() {

        Student student = createTestStudent();
        System.out.println("Created student ID: " + student.getId());
        System.out.println("Created student initial name: " + student.getName());

        student.setName("Рон Уизли");
        System.out.println("Updated student name: " + student.getName());


        String url = getBaseUrl() + "/{id}/edit";
        System.out.println("Request URL: " + url);

        ResponseEntity<Student> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Student.class,
                student.getId()
        );

        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Рон Уизли");
    }

    @Test
    void testDeleteStudent() {
        // Given
        Student student = createTestStudent();

        // When
        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl() + "/{id}/delete",
                HttpMethod.DELETE,
                null,
                Student.class,
                student.getId()
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testFilterAllByAge() {
        // Given
        Student student = createTestStudent();

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/get/by-age?age={age}",
                List.class,
                student.getAge()
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testFindByAgeBetween() {
        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/get/by-age-between?ageMin={ageMin}&ageMax={ageMax}",
                List.class,
                10,
                20
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testFindFacultyByStudentId() {
        // Given
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        // Сначала сохраняем факультет
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty/add",
                faculty,
                Faculty.class
        );
        assertThat(facultyResponse.getBody()).isNotNull();

        // Создаем студента с привязкой к сохраненному факультету
        Student student = createTestStudent();
        student.setFaculty(facultyResponse.getBody());

        // Обновляем студента с установленным факультетом
        restTemplate.put(
                getBaseUrl() + "/{id}/edit",
                student,
                student.getId()
        );

        // When
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/{id}/get-faculty-by-student",
                Faculty.class,
                student.getId()
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
    }

    @Test
    void testFindStudentByFacultyId() {
        // Given
        Faculty faculty = new Faculty();
        faculty.setId(1L);

        // When
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/{id}/get-student-by-faculty",
                List.class,
                faculty.getId()
        );

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    private Student createTestStudent() {
        Student student = new Student();
        student.setName("Тестовый студент");
        student.setAge(15);
        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                student,
                Student.class

        );
        if (response.getBody() == null) {
            throw new RuntimeException("Не удалось создать тестового студента");
        }
        return response.getBody();
    }
}
