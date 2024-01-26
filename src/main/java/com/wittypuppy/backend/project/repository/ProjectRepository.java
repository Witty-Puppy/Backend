package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
