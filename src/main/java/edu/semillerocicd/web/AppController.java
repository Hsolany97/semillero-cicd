package edu.semillerocicd.web;

import edu.semillerocicd.service.CorreoDuplicadoException;
import edu.semillerocicd.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AppController {

    private final UsuarioService usuarioService;

    public AppController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroForm());
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(
            @Valid @ModelAttribute("registroForm") RegistroForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (form.getPassword() != null
                && form.getConfirmarPassword() != null
                && !form.getPassword().equals(form.getConfirmarPassword())) {
            bindingResult.rejectValue(
                    "confirmarPassword",
                    "password.mismatch",
                    "Las contraseñas no coinciden"
            );
        }

        if (bindingResult.hasErrors()) {
            return "registro";
        }

        try {
            usuarioService.registrar(form.getNombre(), form.getCorreo(), form.getPassword());
        } catch (CorreoDuplicadoException ex) {
            bindingResult.rejectValue("correo", "correo.duplicado", ex.getMessage());
            return "registro";
        }

        redirectAttributes.addFlashAttribute("mensajeExito", "Usuario registrado correctamente");
        return "redirect:/registro";
    }

    @GetMapping("/recuperar")
    public String recuperar(Model model) {
        if (!model.containsAttribute("recuperacionForm")) {
            model.addAttribute("recuperacionForm", new RecuperacionForm());
        }
        return "recuperar";
    }

    @PostMapping("/recuperar")
    public String recuperarPassword(
            @Valid @ModelAttribute("recuperacionForm") RecuperacionForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "recuperar";
        }

        usuarioService.solicitarRecuperacion(form.getCorreo());
        redirectAttributes.addFlashAttribute(
                "mensajeExito",
                "Si el correo está registrado, se enviaron las instrucciones de recuperación"
        );
        return "redirect:/recuperar";
    }
}
