package ru.hogwarts.school2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }
    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                getBaseUrl() + "/add",
                faculty,
                Faculty.class
        );
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
        assertThat(response.getBody().getColor()).isEqualTo("Красный");
    }
    @Test
    public void testGetFaculty() {

        Faculty faculty = createTestFaculty();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
      getBaseUrl() + "{{id}}/get",
                Faculty.class,
                faculty.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
    }

    @Test
    public void testEditFaculty() {
        Faculty faculty = createTestFaculty();
        faculty.setColor("Золотой");

        restTemplate.put(
                "http://localhost:" + port + "/faculty/" + faculty.getId() + "/edit",
                faculty
        );

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + faculty.getId() + "/get",
                Faculty.class
        );
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getColor()).isEqualTo("Золотой");
    }

    @Test
    public void testDeleteFaculty() {
  Faculty faculty = createTestFaculty();

        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl() + "/{id}/delete",
                HttpMethod.DELETE,
                null,
                Faculty.class,
                faculty.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFilterAllByColor() {
        Faculty faculty = createTestFaculty();
        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/get/by-color?color=Красный",
                List.class,
                faculty.getColor()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testGetFacultyByColorOrName() {

        ResponseEntity<List> response = restTemplate.getForEntity(
                getBaseUrl() + "/get/by-color-or-name?color=Красный&name=Гриффиндор",
                List.class,
                "Красный",
               "Гриффиндор"
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
    private Faculty createTestFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Тестовый факультет");
        faculty.setColor("green");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/faculty"+ "/add",
                faculty,
               Faculty.class

        );
        if (response.getBody() == null) {
            throw new RuntimeException("Не удалось создать тестовый факультет");
        }
        return response.getBody();
    }
}