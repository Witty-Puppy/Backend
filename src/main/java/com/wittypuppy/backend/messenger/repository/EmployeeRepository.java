package com.wittypuppy.backend.messenger.repository;

import com.wittypuppy.backend.messenger.entity.ChatroomProfile;
import com.wittypuppy.backend.messenger.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
