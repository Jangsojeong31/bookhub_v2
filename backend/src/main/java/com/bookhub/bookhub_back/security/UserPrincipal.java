package com.bookhub.bookhub_back.security;

import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
public class UserPrincipal implements UserDetails {
    private final Long employeeId;
    private final String loginId;
    @JsonIgnore
    private final String password;
    private final Long branchId;
    private final EmployeeStatus status;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Employee employee) {
        this.employeeId = employee.getEmployeeId();
        this.loginId = employee.getLoginId();
        this.password = employee.getPassword();
        this.branchId = employee.getBranchId().getBranchId();
        this.status = employee.getStatus();
        String role = "ROLE_" + employee.getPositionId().getAuthority().getAuthorityName();
        this.authorities = Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return status == EmployeeStatus.EMPLOYED;
    }
}
