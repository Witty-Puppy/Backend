package com.wittypuppy.backend.attendance.repository;

import com.wittypuppy.backend.attendance.entity.ApprovalLine;
import com.wittypuppy.backend.attendance.entity.AttendanceManagement;
import com.wittypuppy.backend.attendance.entity.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ManagementRepository extends JpaRepository <AttendanceManagement, Long> {


    @Query(value = "SELECT " +
            "A.attendance_management_code, " +
            "A.attendance_management_arrival_time, " +
            "A.attendance_management_departure_time, " +
            "A.attendance_management_state, " +
            "A.attendance_management_work_day, " +
            "A.employee_code, B.employee_name " +
            "FROM tbl_attendance_management A " +
            "LEFT JOIN tbl_employee B ON A.employee_code = B.employee_code " +
            "WHERE A.employee_code = :employeeCode " +
            "AND A.attendance_management_work_day = CURDATE()",
            nativeQuery = true)
    AttendanceManagement attendanceCommute(Long employeeCode);



    //전체 연차 수량
    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceTotalVacation(Long employeeCode);


    //사용한 연차 수량
    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_used_status = 'Y' " +
            "AND vacation_type = '연차' " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceUseVacation(Long employeeCode);


    //사용한 반차 수량
    @Query(value = "SELECT COUNT(*) " +
            "FROM tbl_vacation " +
            "WHERE employee_code = :employeeCode " +
            "AND vacation_used_status = 'Y' " +
            "AND vacation_type = '반차' " +
            "AND vacation_expiration_date > NOW()",
            nativeQuery = true)
    Long attendanceUseHalfVacation(Long employeeCode);


    AttendanceManagement findFirstByAttendanceEmployeeCode_EmployeeCodeOrderByAttendanceManagementCodeDesc(Long employeeCode);



}
