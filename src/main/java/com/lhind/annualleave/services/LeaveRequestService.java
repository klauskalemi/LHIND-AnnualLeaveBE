package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.LeaveRequest;
import com.lhind.annualleave.persistence.repositories.ILeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {
    private final ILeaveRequestRepository iLeaveRequestRepository;

    @Autowired
    public LeaveRequestService(ILeaveRequestRepository iLeaveRequestRepository) {
        this.iLeaveRequestRepository = iLeaveRequestRepository;
    }

    public List<LeaveRequest> findAll() {
        return iLeaveRequestRepository.findAll();
    }

    public Optional<LeaveRequest> findById(long id) {
        return iLeaveRequestRepository.findById(id);
    }

    public LeaveRequest save(LeaveRequest leaveRequest) {
        checkIfValidLeaveRequest(leaveRequest);
        return iLeaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest update(LeaveRequest leaveRequest) {
        checkIfValidLeaveRequest(leaveRequest);
        return iLeaveRequestRepository.save(leaveRequest);
    }

    public boolean delete(long id) {
        Optional<LeaveRequest> leaveRequestOptional = iLeaveRequestRepository.findById(id);
        if (!leaveRequestOptional.isPresent()) {
            return false;
        }
        iLeaveRequestRepository.delete(leaveRequestOptional.get());

        return true;
    }

    private void checkIfValidLeaveRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
            throw new RuntimeException("Leave end date cannot be before start date.");
        }
        if (ChronoUnit.DAYS.between(leaveRequest.getUser().getCreatedAt(), leaveRequest.getStartDate()) < 90) {
            throw new RuntimeException("Leave cannot be requested. User must be working for over 90 days.");
        }
    }

}
