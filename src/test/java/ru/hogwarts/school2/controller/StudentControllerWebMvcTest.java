package ru.hogwarts.school2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school2.model.Faculty;
import ru.hogwarts.school2.model.Student;
import ru.hogwarts.school2.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getStudent() throws Exception {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("Гарри Поттер");
        student.setAge(11);

        when(studentService.findStudent(id)).thenReturn(student);

        mockMvc.perform(get("/student/{id}/get", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(11));
    }

    @Test
    void createStudent() throws Exception {
        Student student = new Student();
        student.setName("Рон Уизли");
        student.setAge(11);

        Student savedStudent = new Student();
        savedStudent.setId(1L);
        savedStudent.setName("Рон Уизли");
        savedStudent.setAge(11);

        when(studentService.createStudent(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Рон Уизли"));
    }

    @Test
    void editStudent() throws Exception {
        Long id = 1L;
        Student student = new Student();
        student.setName("Гермиона Грейнджер");
        student.setAge(12);

        mockMvc.perform(put("/student/{id}/edit", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteStudent() throws Exception {
        Long id = 1L;
        Student deletedStudent = new Student();
        deletedStudent.setId(id);

        when(studentService.deleteStudent(id)).thenReturn(deletedStudent);

        mockMvc.perform(delete("/student/{id}/delete", id))
                .andExpect(status().isOk());
    }

    @Test
    void filterAllByAge() throws Exception {
        int age = 11;
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", age),
                new Student(2L, "Рон Уизли", age)
        );

        when(studentService.filterAllByAge(age)).thenReturn(students);

        mockMvc.perform(get("/student/get/by-age")
                        .param("age", String.valueOf(age)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Рон Уизли"));
    }

    @Test
    void findByAgeBetween() throws Exception {
        int ageMin = 11;
        int ageMax = 13;
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", 11),
                new Student(2L, "Гермиона Грейнджер", 12)
        );

        when(studentService.findByAgeBetween(ageMin, ageMax)).thenReturn(students);

        mockMvc.perform(get("/student/get/by-age-between")
                        .param("ageMin", String.valueOf(ageMin))
                        .param("ageMax", String.valueOf(ageMax)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));
    }

    @Test
    void findFacultyByStudentId() throws Exception {
        Long id = 1L;
        Student student = new Student();
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        student.setFaculty(faculty);

        when(studentService.findStudent(id)).thenReturn(student);

        mockMvc.perform(get("/student/{id}/get-faculty-by-student", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Гриффиндор"));
    }

    @Test
    void findStudentByFacultyId() throws Exception {
        Long facultyId = 1L;
        List<Student> students = List.of(
                new Student(1L, "Гарри Поттер", 11),
                new Student(2L, "Рон Уизли", 11)
        );

        when(studentService.findStudentsByFacultyId(facultyId)).thenReturn(students);

        mockMvc.perform(get("/student/{id}/get-student-by-faculty", facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Рон Уизли"));
    }
}
