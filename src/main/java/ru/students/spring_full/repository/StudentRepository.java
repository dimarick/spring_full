package ru.students.spring_full.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.students.spring_full.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
