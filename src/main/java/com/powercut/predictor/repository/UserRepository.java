package com.powercut.predictor.repository;

import com.powercut.predictor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByAreaNameIgnoreCaseAndAlertsEnabledTrue(
            String areaName);
}
