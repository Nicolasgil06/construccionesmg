package com.construccionesmg.controller;

import com.construccionesmg.model.Cotizacion;
import com.construccionesmg.model.Proyecto;
import com.construccionesmg.model.User;
import com.construccionesmg.service.CotizacionService;
import com.construccionesmg.service.ProyectoService;
import com.construccionesmg.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ProyectoService proyectoService;
    private final CotizacionService cotizacionService;
    private final UserService userService;

    public ClientController(ProyectoService proyectoService,
                          CotizacionService cotizacionService,
                          UserService userService) {
        this.proyectoService = proyectoService;
        this.cotizacionService = cotizacionService;
        this.userService = userService;
    }

    private User getCurrentUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName()).orElseThrow();
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        List<Proyecto> misProyectos = proyectoService.findByClienteId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("proyectos", misProyectos);
        model.addAttribute("cotizaciones", cotizacionService.findByClienteId(user.getId()));
        return "client/dashboard";
    }

    @GetMapping("/proyectos")
    public String proyectos(@RequestParam(value = "tipo", required = false) String tipo,
                            Authentication authentication,
                            Model model) {
        User user = getCurrentUser(authentication);
        List<Proyecto> proyectos;
        if (tipo == null || tipo.isBlank()) {
            proyectos = proyectoService.findByClienteId(user.getId());
        } else {
            proyectos = proyectoService.findByClienteIdAndTipo(user.getId(), tipo);
        }
        model.addAttribute("user", user);
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("tipos", Proyecto.TipoProyecto.values());
        model.addAttribute("tipoSeleccionado", tipo);
        return "client/proyectos";
    }

    @GetMapping("/proyectos/{id}")
    public String proyectoDetalle(@PathVariable String id, Model model) {
        Proyecto proyecto = proyectoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado"));
        model.addAttribute("proyecto", proyecto);
        return "client/proyecto_detalle";
    }

    @GetMapping("/cotizacion")
    public String cotizacionForm(Model model) {
        model.addAttribute("cotizacion", new Cotizacion());
        model.addAttribute("tipos", new String[]{"Remodelacion", "Nueva obra", "Ampliacion", "Otro"});
        return "client/cotizacion";
    }

    @PostMapping("/cotizacion")
    public String enviarCotizacion(@ModelAttribute("cotizacion") Cotizacion cotizacion,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = getCurrentUser(authentication);
            cotizacion.setClienteId(user.getId());
            cotizacion.setEstado(Cotizacion.EstadoCotizacion.PENDIENTE);
            cotizacionService.save(cotizacion);
            redirectAttributes.addFlashAttribute("message", "Cotizacion enviada correctamente. El contratista se pondra en contacto contigo.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al enviar cotizacion: " + e.getMessage());
        }
        return "redirect:/client/cotizacion";
    }

    @GetMapping("/cotizaciones")
    public String misCotizaciones(Authentication authentication, Model model) {
        User user = getCurrentUser(authentication);
        model.addAttribute("user", user);
        model.addAttribute("cotizaciones", cotizacionService.findByClienteId(user.getId()));
        return "client/cotizaciones";
    }
}
