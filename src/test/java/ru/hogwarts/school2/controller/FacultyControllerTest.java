package ru.hogwarts.school2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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
                getBaseUrl() + "/{id}/get",
                Faculty.class,
                faculty.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(faculty.getId());
    }

    @Test
    public void testEditFaculty() {
        Faculty faculty = createTestFaculty();
        faculty.setColor("Золотой");

        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl() + "/{id}/edit",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class,
                faculty.getId()
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertEquals("Золотой", response.getBody().getColor());
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
        // Given
        Faculty faculty = createTestFaculty();
        String color = faculty.getColor();
        System.out.println("Created faculty with color: " + color);

        String url = getBaseUrl() + "/get/by-color?color=" + color;
        System.out.println("Request URL: " + url);

        // When
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                getBaseUrl() + "/get/by-color?color={color}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Faculty>>() {},
                color
        );

        // Then
        System.out.println("Response status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).asList().isNotEmpty();
        assertThat(response.getBody().get(0).getColor()).isEqualTo(color);
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