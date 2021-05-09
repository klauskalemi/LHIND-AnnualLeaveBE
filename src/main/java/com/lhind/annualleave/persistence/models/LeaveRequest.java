package com.lhind.annualleave.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = LeaveRequest.TABLE_NAME)
public class LeaveRequest implements Serializable {
    public static final String TABLE_NAME = "leave_requests";
    public static final int STATUS_WAITING = 0;
    public static final int STATUS_ACCEPTED = 1;
    public static final int STATUS_REJECTED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, insertable = false, unique = true, nullable = false)
    private long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    private int status = STATUS_WAITING;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    @JsonIgnoreProperties(ignoreUnknown = true, value = {"leaveRequests"})
    private User user;

    public LeaveRequest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isRequestWaiting() {
        return this.status == STATUS_WAITING;
    }

    public boolean isRequestAccepted() {
        return this.status == STATUS_ACCEPTED;
    }

    public boolean isRequestRejected() {
        return this.status == STATUS_REJECTED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveRequest that = (LeaveRequest) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
