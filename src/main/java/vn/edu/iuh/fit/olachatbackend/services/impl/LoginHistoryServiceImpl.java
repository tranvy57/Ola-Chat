/*
 * @ (#) LoginHistoryServiceImpl.java       1.0     30/03/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package vn.edu.iuh.fit.olachatbackend.services.impl;
/*
 * @description:
 * @author: Nguyen Thanh Nhut
 * @date: 30/03/2025
 * @version:    1.0
 */

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.olachatbackend.dtos.LoginHistoryDTO;
import vn.edu.iuh.fit.olachatbackend.entities.LoginHistory;
import vn.edu.iuh.fit.olachatbackend.entities.User;
import vn.edu.iuh.fit.olachatbackend.enums.LoginHistoryStatus;
import vn.edu.iuh.fit.olachatbackend.exceptions.NotFoundException;
import vn.edu.iuh.fit.olachatbackend.mappers.LoginHistoryMapper;
import vn.edu.iuh.fit.olachatbackend.repositories.LoginHistoryRepository;
import vn.edu.iuh.fit.olachatbackend.repositories.UserRepository;
import vn.edu.iuh.fit.olachatbackend.services.EmailService;
import vn.edu.iuh.fit.olachatbackend.services.LoginHistoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginHistoryServiceImpl implements LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final UserRepository userRepository;
    private final LoginHistoryMapper loginHistoryMapper;
    private final EmailService emailService;

    @Override
    public void saveLogin(String userId, String userAgent) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Kiểm tra thiết bị có từng được sử dụng chưa
        boolean isNewDevice = loginHistoryRepository
                .findDistinctUserAgentsByUserId(userId)
                .stream()
                .noneMatch(agent -> agent.equals(userAgent));

        // Nếu thiết bị lạ => gửi cảnh báo qua email
        if (isNewDevice) {
            emailService.sendLoginAlert(user.getEmail(), userAgent);
        }

        LoginHistory loginHistory = new LoginHistory();
        loginHistory.setUser(user);
        loginHistory.setLoginTime(LocalDateTime.now());
        loginHistory.setStatus(LoginHistoryStatus.ONLINE);
        loginHistory.setUserAgent(userAgent);

        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public void saveLogout(String userId) {
        Optional<LoginHistory> lastLogin = loginHistoryRepository.findFirstByUserIdOrderByLoginTimeDesc(userId);
        lastLogin.ifPresent(login -> {
            login.setLogoutTime(LocalDateTime.now());
            login.setStatus(LoginHistoryStatus.OFFLINE);
            loginHistoryRepository.save(login);
        });
    }

    @Override
    public LoginHistoryDTO getRecentLogin(String userId) {
        return loginHistoryRepository.findFirstByUserIdOrderByLoginTimeDesc(userId)
                .map(loginHistoryMapper::toDTO)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public boolean isUserOnline(String userId) {
        return loginHistoryRepository.findFirstByUserIdOrderByLoginTimeDesc(userId)
                .map(login -> login.getStatus() == LoginHistoryStatus.ONLINE)
                .orElse(false);
    }

    @Override
    public List<LoginHistoryDTO> getLoginHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<LoginHistory> historyList = loginHistoryRepository.findAllByUserIdOrderByLoginTimeDesc(userId);
        return historyList.stream()
                .map(loginHistoryMapper::toDTO)
                .toList();
    }

    @Override
    public void pingOnline(String userId) {
        Optional<LoginHistory> lastLogin = loginHistoryRepository.findFirstByUserIdOrderByLoginTimeDesc(userId);

        lastLogin.ifPresent(login -> {
            login.setStatus(LoginHistoryStatus.ONLINE);
            login.setLastPingTime(LocalDateTime.now());
            loginHistoryRepository.save(login);
        });
    }

//    kiểm tra user đã timeout → set OFFLINE
    @Override
    @Scheduled(fixedDelay = 60000) // mỗi phút 1 lần
    public void checkTimeoutUsers() {
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(10); // THỜI GIAN TỰ ĐỘNG OFFLINE

        List<LoginHistory> onlineUsers = loginHistoryRepository.findByStatus(LoginHistoryStatus.ONLINE);
        for (LoginHistory login : onlineUsers) {
            if (login.getLastPingTime() == null || login.getLastPingTime().isBefore(timeoutThreshold)) {
                login.setStatus(LoginHistoryStatus.OFFLINE);
                login.setLogoutTime(LocalDateTime.now());
                loginHistoryRepository.save(login);
            }
        }
    }
}
