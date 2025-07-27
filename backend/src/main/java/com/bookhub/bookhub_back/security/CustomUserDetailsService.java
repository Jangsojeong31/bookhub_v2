package com.bookhub.bookhub_back.security;

import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public UserPrincipal loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId));

        String authorityName = employee.getPositionId().getAuthority().getAuthorityName();

        return new UserPrincipal(employee);
    }
}
