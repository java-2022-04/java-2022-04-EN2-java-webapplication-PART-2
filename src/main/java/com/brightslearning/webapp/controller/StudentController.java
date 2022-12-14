package com.brightslearning.webapp.controller;

import com.brightslearning.webapp.entity.Student;
import com.brightslearning.webapp.service.SearchService;
import com.brightslearning.webapp.service.StudentHTMLService;
import com.brightslearning.webapp.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class StudentController {

    private final StudentHTMLService studentsHtmlService;
    private final StudentService studentService;
    private final SearchService searchService;

    @Autowired
    public StudentController(StudentHTMLService studentsHtmlService,
                             StudentService studentService,
                             SearchService searchService) {

        this.studentsHtmlService = studentsHtmlService;
        this.studentService = studentService;
        this.searchService = searchService;
    }

    @GetMapping("/") //http://localhost:8080/?name=Bob
    public String index(Model model, @RequestParam String name) {
        model.addAttribute("name", name);
        return "index";
    }

    @PostMapping("/saveStudent") //http://localhost:8080/saveStudent
    public String saveStudent(@RequestParam String name,
                              @RequestParam String lastName,
                              @RequestParam Integer age,
                              @RequestParam String email,
                              @RequestParam String occupation, Model model) {
        Student student = studentService.saveNewStudent(name, lastName, age, email, occupation);
        model.addAttribute("student", student);
        return "studentsaved";
    }

    @GetMapping("/students") //http://localhost:8080/students
    public String displayAlleStudents(Model model) {
        List<Student> students = studentService.allStudents();
        model.addAttribute("students", students);
        return "students";
    }


    @PostMapping("/add")
    @ResponseBody
    public String addStudent(@RequestParam String name,
                             @RequestParam String lastName,
                             @RequestParam Integer age,
                             @RequestParam String email,
                             @RequestParam String occupation) {

        Student student = studentService.saveNewStudent(name, lastName, age, email, occupation);
        return studentsHtmlService.newStudentCreatedAsHtml(student) +
                studentsHtmlService.studentListAsHtml();
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateStudent(@RequestParam Long id,
                                @RequestParam String name,
                                @RequestParam String lastName,
                                @RequestParam Integer age,
                                @RequestParam String email,
                                @RequestParam String occupation) {
        Student updatedStudent = studentService.updateStudent(id, name, lastName, age, email, occupation);
        return studentsHtmlService.studentHasBeenUpdatedAsHtml(updatedStudent) +
                studentsHtmlService.studentListAsHtml();
    }

    @GetMapping("/edit")
    @ResponseBody
    public String editStudent(@RequestParam String id) {
        return studentsHtmlService.studentEditFormHtml(id);
    }

    @GetMapping("/delete")
    @ResponseBody
    public String deleteStudent(@RequestParam Long id) {
        studentService.deleteStudent(id);
        return studentsHtmlService.studentHasBeenRemovedAsHtml(id);
    }

    @GetMapping("/search")
    @ResponseBody
    public String searchByLastName(@RequestParam String lastName) {
        List<Student> students = searchService.findStudentByLastName(lastName);
        return studentsHtmlService.studentListAsHtml(students);
    }


}
