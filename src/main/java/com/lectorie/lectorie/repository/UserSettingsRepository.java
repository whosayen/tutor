package com.lectorie.lectorie.repository;

import com.lectorie.lectorie.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, String> {
}
