package edu.semillerocicd.service;

import edu.semillerocicd.domain.Usuario;
import edu.semillerocicd.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(String nombre, String correo, String password) {
        String correoNormalizado = normalizarCorreo(correo);

        if (usuarioRepository.existsByCorreoIgnoreCase(correoNormalizado)) {
            throw new CorreoDuplicadoException("Ya existe un usuario con ese correo.");
        }

        Usuario usuario = new Usuario(
                nombre.trim(),
                correoNormalizado,
                passwordEncoder.encode(password)
        );

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public void solicitarRecuperacion(String correo) {
        // Para la demo no enviamos un correo real.
        // La consulta simula la validación interna, pero hacia el usuario
        // siempre se responde de forma genérica para no revelar cuentas existentes.
        usuarioRepository.findByCorreoIgnoreCase(normalizarCorreo(correo));
    }

    private String normalizarCorreo(String correo) {
        return correo == null ? "" : correo.trim().toLowerCase(Locale.ROOT);
    }
}
