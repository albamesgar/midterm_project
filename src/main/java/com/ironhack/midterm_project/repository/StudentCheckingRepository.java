package com.ironhack.midterm_project.repository;

import com.ironhack.midterm_project.model.accounts.StudentChecking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentCheckingRepository extends JpaRepository<StudentChecking,Long> {
}
