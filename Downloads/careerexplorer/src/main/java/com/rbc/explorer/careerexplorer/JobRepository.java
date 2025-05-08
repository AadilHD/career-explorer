package com.rbc.explorer.careerexplorer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {

    // Enables search by job title (case-insensitive, partial match)
    List<Job> findByTitleContainingIgnoreCase(String keyword);
}