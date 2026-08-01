package com.construccionesmg.controller;

import com.construccionesmg.model.Cotizacion;
import com.construccionesmg.model.Proyecto;
import com.construccionesmg.model.User;
import com.construccionesmg.service.CotizacionService;
import com.construccionesmg.service.ProyectoService;
import com.construccionesmg.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProyectoService proyectoService;
    private final CotizacionService cotizacionService;
    private final UserService userService;

    public AdminController(ProyectoService proyectoService,
                           CotizacionService cotizacionService,
                           UserService userService) {
        this.proyectoService = proyectoService;
        this.cotizacionService = cotizacionService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalProyectos", proyectoService.findAll().size());
        model.addAttribute("totalCotizaciones", cotizacionService.findAll().size());
        model.addAttribute("totalClientes", userService.findAllClients().size());
        return "admin/dashboard";
    }

    // Proyectos
    @GetMapping("/proyectos")
    public String listProyectos(@RequestParam(value = "tipo", required = false) String tipo, Model model) {
        List<Proyecto> proyectos;
        if (tipo == null || tipo.isBlank()) {
            proyectos = proyectoService.findAll();
        } else {
            proyectos = proyectoService.findByTipo(Proyecto.TipoProyecto.valueOf(tipo));
        }
        model.addAttribute("proyectos", proyectos);
        model.addAttribute("tipos", Proyecto.TipoProyecto.values());
        model.addAttribute("tipoSeleccionado", tipo);
        return "admin/proyectos";
    }

    @GetMapping("/proyectos/nuevo")
    public String newProyecto(Model model) {
        model.addAttribute("proyecto", new Proyecto());
        model.addAttribute("tipos", Proyecto.TipoProyecto.values());
        model.addAttribute("estados", Proyecto.EstadoProyecto.values());
        model.addAttribute("clientes", userService.findAllClients());
        return "admin/proyecto_form";
    }

    @GetMapping("/proyectos/editar/{id}")
    public String editProyecto(@PathVariable String id, Model model) {
        Optional<Proyecto> opt = proyectoService.findById(id);
        if (opt.isEmpty()) {
            return "redirect:/admin/proyectos";
        }
        model.addAttribute("proyecto", opt.get());
        model.addAttribute("tipos", Proyecto.TipoProyecto.values());
        model.addAttribute("estados", Proyecto.EstadoProyecto.values());
        model.addAttribute("clientes", userService.findAllClients());
        return "admin/proyecto_form";
    }

    @PostMapping("/proyectos/guardar")
    public String saveProyecto(@ModelAttribute("proyecto") Proyecto proyecto,
                               @RequestParam("especificacionesText") String especificacionesText,
                               RedirectAttributes redirectAttributes) {
        try {
            if (especificacionesText != null && !especificacionesText.isBlank()) {
                List<String> specs = Arrays.stream(especificacionesText.split("\\r?\\n"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList();
                proyecto.setEspecificaciones(specs);
            }
            proyectoService.save(proyecto);
            redirectAttributes.addFlashAttribute("message", "Proyecto guardado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar proyecto: " + e.getMessage());
        }
        return "redirect:/admin/proyectos";
    }

    @GetMapping("/proyectos/eliminar/{id}")
    public String deleteProyecto(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            proyectoService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Proyecto eliminado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar proyecto: " + e.getMessage());
        }
        return "redirect:/admin/proyectos";
    }

    // Cotizaciones
    @GetMapping("/cotizaciones")
    public String listCotizaciones(Model model) {
        model.addAttribute("cotizaciones", cotizacionService.findAll());
        return "admin/cotizaciones";
    }

    @PostMapping("/cotizaciones/responder/{id}")
    public String responderCotizacion(@PathVariable String id,
                                      @RequestParam("respuesta") String respuesta,
                                      RedirectAttributes redirectAttributes) {
        try {
            cotizacionService.responder(id, respuesta);
            redirectAttributes.addFlashAttribute("message", "Cotizacion respondida correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al responder: " + e.getMessage());
        }
        return "redirect:/admin/cotizaciones";
    }

    // Clientes
    @GetMapping("/clientes")
    public String listClientes(Model model) {
        model.addAttribute("clientes", userService.findAllClients());
        return "admin/clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String newCliente(Model model) {
        model.addAttribute("user", new User());
        return "admin/cliente_form";
    }

    @PostMapping("/clientes/guardar")
    public String saveCliente(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        try {
            userService.saveClient(user);
            redirectAttributes.addFlashAttribute("message", "Cliente registrado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar cliente: " + e.getMessage());
        }
        return "redirect:/admin/clientes";
    }
}
