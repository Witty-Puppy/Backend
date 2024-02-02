package com.wittypuppy.backend.project.repository;

import com.wittypuppy.backend.project.entity.ProjectPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("Project_ProjectPostRepository")
public interface ProjectPostRepository extends JpaRepository<ProjectPost,Long> {
}
