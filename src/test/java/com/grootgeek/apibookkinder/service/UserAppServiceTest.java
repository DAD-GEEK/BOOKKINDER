package com.grootgeek.apibookkinder.service;

import com.grootgeek.apibookkinder.entities.UserAppEntity;
import com.grootgeek.apibookkinder.repository.UserAPPRepository;
import com.grootgeek.apibookkinder.utils.Logger;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Nested
class UserAppServiceTest {

    @Mock
    private UserAPPRepository userAPPRepositoryMock = mock(UserAPPRepository.class);
    @Mock
    private Logger utilLogsMock = mock(Logger.class);

    @InjectMocks
    private UserAppService userAppService = new UserAppService(utilLogsMock, userAPPRepositoryMock);

    @Test
    void saveNewUser() {
        // Arrange or Given
        UserAppEntity newUser = new UserAppEntity();
        newUser.setEmail("user@example.com");
        when(userAPPRepositoryMock.save(any(UserAppEntity.class))).thenReturn(newUser);

        // Act or Then
        UserAppEntity result = userAppService.saveNewUser(newUser);

        // Assert or Than
        assertNotNull(result);
        assertThat(result).isEqualTo(newUser);
    }
    @Test
    void saveNewUserFailedDueToNonExistingUser() {
        UserAppEntity newUser = new UserAppEntity();
        newUser.setEmail("user@example.com");
        when(userAPPRepositoryMock.save(any(UserAppEntity.class))).thenReturn(newUser);
        UserAppEntity result = userAppService.saveNewUser(new UserAppEntity());
        assertThat(result).isNull();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }
    @Test
    void saveNewUserFailedDueToException() {
        UserAppEntity newUser = new UserAppEntity();
        newUser.setEmail("user@example.com");
        when(userAPPRepositoryMock.save(any(UserAppEntity.class))).thenThrow(new RuntimeException());
        UserAppEntity result = userAppService.saveNewUser(newUser);
        assertThat(result).isNull();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }

    @Test
    void findByEmail() {
        // Arrange
        UserAppEntity userFound = new UserAppEntity();
        userFound.setEmail("user@example.com");

        when(userAPPRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.of(userFound));

        // Act
        UserAppEntity result = userAppService.findByEmail("user@example.com");

        // Assert
        assertNotNull(result);
        assertThat(result).isEqualTo(userFound);
    }

    @Test
    void findByEmailFailedDueToNonExistingUser() {
        when(userAPPRepositoryMock.findByEmail("nonexisting@example.com")).thenReturn(empty());
        UserAppEntity result = userAppService.findByEmail("nonexisting@example.com");
        assertThat(result).isNull();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }


    @Test
    void findByEmailFailedDueToException() {
        when(userAPPRepositoryMock.findByEmail("")).thenThrow(new RuntimeException());
        UserAppEntity result = userAppService.findByEmail("");
        assertThat(result).isNull();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }

    @Test
    void validatePassword() {
        // Arrange
        UserAppEntity user = new UserAppEntity();
        user.setEmail("user@example.com");
        String hashedPassword = new BCryptPasswordEncoder().encode("password");
        user.setPassword(hashedPassword);

        when(userAPPRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        Boolean result = userAppService.validatePassword("user@example.com", "password");

        // Assert
        assertTrue(result);
    }

    @Test
    void validatePasswordWithIncorrectPassword() {
        // Arrange
        UserAppEntity user = new UserAppEntity();
        user.setEmail("user@example.com");
        String hashedPassword = new BCryptPasswordEncoder().encode("password");
        user.setPassword(hashedPassword);

        when(userAPPRepositoryMock.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        // Act
        Boolean result = userAppService.validatePassword("user@example.com", "wrong password");

        // Assert
        assertFalse(result);
    }

    @Test
    void validatePasswordFailedDueToNonExistingUser() {
        when(userAPPRepositoryMock.findByEmail("nonexisting@example.com")).thenReturn(empty());

        Boolean result = userAppService.validatePassword("nonexisting@example.com", "password");

        assertThat(result).isFalse();
    }

    @Test
    void updateUserAppSuccessfully() {
        UserAppEntity existingUser = new UserAppEntity();
        existingUser.setEmail("existing@example.com");
        UserAppEntity updateUser = new UserAppEntity();
        updateUser.setEmail("existing@example.com");
        updateUser.setName("Updated Name");

        when(userAPPRepositoryMock.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser));
        when(userAPPRepositoryMock.save(any(UserAppEntity.class))).thenReturn(updateUser);

        UserAppEntity result = userAppService.updateUserApp(updateUser);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updateUser.getName());
        assertThat(result.getLastName()).isEqualTo(updateUser.getLastName());
        assertThat(result).isEqualTo(updateUser);
    }

    @Test
    void updateUserAppFailedDueToNonExistingUser() {
        UserAppEntity existingUser = new UserAppEntity();
        existingUser.setEmail("NOexisting@example.com");
        UserAppEntity updateUser = new UserAppEntity();
        updateUser.setEmail("existing@example.com");
        updateUser.setName("Updated Name");

        when(userAPPRepositoryMock.findByEmail("existing@example.com")).thenReturn(Optional.of(updateUser));
        when(userAPPRepositoryMock.save(any(UserAppEntity.class))).thenReturn(existingUser);

        UserAppEntity result = userAppService.updateUserApp(updateUser);

        assertThat(result).isNull();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }


    @Test
    void deleteByEmail() {
        // Arrange
        String emailToDelete = "user@example.com";

        // Mock del repositorio para que no lance excepciones al llamar a deleteByEmail
        doNothing().when(userAPPRepositoryMock).deleteByEmail(emailToDelete);


        // Act
        Boolean result = userAppService.deleteByEmail(emailToDelete);

        // Assert
        assertTrue(result);

    }

    @Test
    void deleteByEmailFailedDueToException() {
        doThrow(new RuntimeException()).when(userAPPRepositoryMock).deleteByEmail("user@example.com");

        Boolean result = userAppService.deleteByEmail("user@example.com");

        assertThat(result).isFalse();
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }


    @Test
    void findAll() {
        // Arrange
        UserAppEntity user1 = new UserAppEntity();
        user1.setEmail("user1@example.com");

        when(userAPPRepositoryMock.findAll()).thenReturn(List.of(user1));

        // Act
        List<UserAppEntity> result = userAPPRepositoryMock.findAll();

        // Assert
        assertNotNull(result);

    }

    @Test
    void findAllFailedDueToException() {

        when(userAPPRepositoryMock.findAll()).thenThrow(new RuntimeException());
        List<UserAppEntity> result = userAppService.findall();
        assertThat(result).isEqualTo(List.of());
        verify(utilLogsMock, times(1)).logApiError(anyString());
    }
}


