package ru.students.spring_security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.students.spring_security.entity.Student;
import ru.students.spring_security.repository.StudentRepository;

@Slf4j
@Controller
public class StudentController {
    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping({"/list", "/"})
    public ModelAndView getAllStudents() {
        log.info("/list -> connection");
        var mav = new ModelAndView("list-students");
        mav.addObject("students", studentRepository.findAll());

        return mav;
    }

    @GetMapping("/addStudentForm")
    public ModelAndView addStudentForm() {
        log.info("/addStudentForm");
        var mav = new ModelAndView("add-student-form");
        mav.addObject("student", new Student());

        return mav;
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student student) {
        log.info("/saveStudent");
        studentRepository.save(student);

        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long studentId) {
        log.info("/showUpdateForm");
        var mav = new ModelAndView("add-student-form");
        var optionalStudent = studentRepository.findById(studentId);
        var student = new Student();
        if (optionalStudent.isPresent()) {
            student = optionalStudent.get();
        }

        mav.addObject("student", student);

        return mav;
    }

    @GetMapping("/deleteStudent")
    public String deleteStudent(@RequestParam Long studentId) {
        log.info("/deleteStudent");
        studentRepository.deleteById(studentId);

        return "redirect:/list";
    }
}
