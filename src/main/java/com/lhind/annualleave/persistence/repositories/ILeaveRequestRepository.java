package com.lhind.annualleave.persistence.repositories;

import com.lhind.annualleave.persistence.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
}
