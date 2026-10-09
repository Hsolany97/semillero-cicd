package edu.semillerocicd.service;

import edu.semillerocicd.domain.Usuario;
import edu.semillerocicd.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void registraUsuarioNormalizandoCorreoYProtegiendoPassword() {
        when(usuarioRepository.existsByCorreoIgnoreCase("harold@test.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.registrar("Harold Zapata", "  Harold@Test.COM ", "Password123!");

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());

        Usuario guardado = captor.getValue();
        assertThat(guardado.getCorreo()).isEqualTo("harold@test.com");
        assertThat(guardado.getNombre()).isEqualTo("Harold Zapata");
        assertThat(guardado.getPasswordHash()).isNotEqualTo("Password123!");
        assertThat(passwordEncoder.matches("Password123!", guardado.getPasswordHash())).isTrue();
    }

    @Test
    void rechazaCorreoDuplicado() {
        when(usuarioRepository.existsByCorreoIgnoreCase("harold@test.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrar(
                "Harold",
                "harold@test.com",
                "Password123!"
        ))
                .isInstanceOf(CorreoDuplicadoException.class)
                .hasMessageContaining("Ya existe");
    }
}
