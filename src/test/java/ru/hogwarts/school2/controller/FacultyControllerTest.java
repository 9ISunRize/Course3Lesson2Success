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

        Faculty created = createTestFaculty();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + created.getId() + "/get",
                Faculty.class
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
        Faculty faculty = new Faculty();
        faculty.setName("Когтевран");
        faculty.setColor("Синий");

        Faculty created = restTemplate.postForObject(
                "http://localhost:" + port + "/faculty/add",
                faculty,
                Faculty.class
        );

        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty/" + created.getId() + "/delete",
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testFilterAllByColor() {
        Faculty faculty1 = new Faculty();
        faculty1.setName("Гриффиндор");
        faculty1.setColor("Красный");

        Faculty faculty2 = new Faculty();
        faculty2.setName("Другой факультет");
        faculty2.setColor("Красный");

        restTemplate.postForObject(
                "http://localhost:" + port + "/faculty/add",
                faculty1,
                Faculty.class
        );

        restTemplate.postForObject(
                "http://localhost:" + port + "/faculty/add",
                faculty2,
                Faculty.class
        );

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/get/by-color?color=Красный",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void testGetFacultyByColorOrName() {
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        restTemplate.postForObject(
                "http://localhost:" + port + "/faculty/add",
                faculty,
                Faculty.class
        );

        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/get/by-color-or-name?color=Красный&name=Гриффиндор",
                Faculty[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThanOrEqualTo(1);
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