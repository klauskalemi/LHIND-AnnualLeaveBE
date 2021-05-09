package com.lhind.annualleave.services;

import com.lhind.annualleave.persistence.models.LeaveRequest;
import com.lhind.annualleave.persistence.models.User;
import com.lhind.annualleave.persistence.repositories.ILeaveRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class LeaveRequestServiceTest {
    @Autowired
    private LeaveRequestService leaveRequestService;
    @MockBean
    private ILeaveRequestRepository leaveRequestRepository;

    @Test
    void testFindAllMethodFromLeaveRequestsService() {
        LeaveRequest firstRequest = getLeaveRequestObject(LocalDate.now(), 7);
        LeaveRequest secondRequest = getLeaveRequestObject(LocalDate.now().plusWeeks(1), 7);

        Mockito.when(leaveRequestRepository.findAll())
                .thenReturn(Stream.of(firstRequest, secondRequest).collect(Collectors.toList()));

        Assertions.assertEquals(2, leaveRequestService.findAll().size());
    }

    @Test
    void testSaveMethodFromLeaveRequestsServiceWithValidDates() {
        LeaveRequest request = getLeaveRequestObject(LocalDate.now(), 7);
        Mockito.when(leaveRequestRepository.save(request)).thenReturn(request);
        Assertions.assertEquals(leaveRequestService.save(request), request);
    }

    @Test
    void testSaveMethodWhenUserOfLeaveHasNotPassedProbationPeriod() {
        LeaveRequest request = getLeaveRequestObject(LocalDate.now(), 7);
        request.setUser(getUserObject(2));

        Assertions.assertThrows(RuntimeException.class, () -> leaveRequestService.save(request));
    }

    @Test
    void testUpdateMethodFromService() {
        LeaveRequest request = getLeaveRequestObject(LocalDate.now(), 4);
        Mockito.when(leaveRequestRepository.save(request)).thenReturn(request);
        Assertions.assertEquals(leaveRequestService.update(request), request);
    }

    private LeaveRequest getLeaveRequestObject(LocalDate startDate, int leaveDays) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(1);
        leaveRequest.setStartDate(startDate);
        leaveRequest.setEndDate(startDate.plus(leaveDays, ChronoUnit.DAYS));
        leaveRequest.setUser(getUserObject(4));

        return leaveRequest;
    }

    private User getUserObject(int monthsOld) {
        User user = new User();
        user.setId(1);
        user.setCreatedAt(LocalDate.now().minusMonths(monthsOld));

        return user;
    }
}