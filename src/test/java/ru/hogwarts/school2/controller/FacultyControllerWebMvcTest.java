package ru.hogwarts.school2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FacultyService facultyService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void getFacultyTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName("Гриффиндор");
            faculty.setColor("Красный");

            when(facultyService.findFaculty(1L)).thenReturn(faculty);

            mockMvc.perform(get("/faculty/1/get"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Гриффиндор"))
                    .andExpect(jsonPath("$.color").value("Красный"));
        }

        @Test
        void createFacultyTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setName("Слизерин");
            faculty.setColor("Зеленый");

            Faculty createdFaculty = new Faculty();
            createdFaculty.setId(1L);
            createdFaculty.setName("Слизерин");
            createdFaculty.setColor("Зеленый");

            when(facultyService.createFaculty(any())).thenReturn(createdFaculty);

            mockMvc.perform(post("/faculty/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(faculty)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Слизерин"))
                    .andExpect(jsonPath("$.color").value("Зеленый"));
        }

        @Test
        void editFacultyTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setName("Хаффлпафф");
            faculty.setColor("Желтый");

            mockMvc.perform(put("/faculty/1/edit")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(faculty)))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteFacultyTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName("Когтевран");
            faculty.setColor("Синий");

            when(facultyService.deleteFaculty(1L)).thenReturn(faculty);

            mockMvc.perform(delete("/faculty/1/delete"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").value("Когтевран"))
                    .andExpect(jsonPath("$.color").value("Синий"));
        }

        @Test
        void filterAllByColorTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName("Гриффиндор");
            faculty.setColor("Красный");

            when(facultyService.filterAllByColor("Красный")).thenReturn(List.of(faculty));

            mockMvc.perform(get("/faculty/get/by-color")
                            .param("color", "Красный"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                    .andExpect(jsonPath("$[0].color").value("Красный"));
        }

        @Test
        void getFacultyByColorOrNameTest() throws Exception {
            Faculty faculty = new Faculty();
            faculty.setId(1L);
            faculty.setName("Слизерин");
            faculty.setColor("Зеленый");

            when(facultyService.getFacultyByColorOrName("Зеленый", "Слизерин"))
                    .thenReturn(List.of(faculty));

            mockMvc.perform(get("/faculty/get/by-color-or-name")
                            .param("color", "Зеленый")
                            .param("name", "Слизерин"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].name").value("Слизерин"))
                    .andExpect(jsonPath("$[0].color").value("Зеленый"));
        }

}
