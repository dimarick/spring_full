package ru.students.spring_full.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.students.spring_full.entity.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
}
