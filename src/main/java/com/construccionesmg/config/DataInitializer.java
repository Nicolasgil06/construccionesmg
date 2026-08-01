package com.construccionesmg.config;

import com.construccionesmg.model.Cotizacion;
import com.construccionesmg.model.Proyecto;
import com.construccionesmg.model.User;
import com.construccionesmg.repository.CotizacionRepository;
import com.construccionesmg.repository.ProyectoRepository;
import com.construccionesmg.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProyectoRepository proyectoRepository;
    private final CotizacionRepository cotizacionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           ProyectoRepository proyectoRepository,
                           CotizacionRepository cotizacionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.proyectoRepository = proyectoRepository;
        this.cotizacionRepository = cotizacionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Usuario admin
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ROLE_ADMIN);
            admin.setNombre("Constructora MG");
            admin.setEmail("contacto@construccionesmg.com");
            admin.setTelefono("+57 300 123 4567");
            userRepository.save(admin);
        }

        // Usuario cliente de prueba
        User demoClient = null;
        if (userRepository.findByUsername("cliente").isEmpty()) {
            demoClient = new User();
            demoClient.setUsername("cliente");
            demoClient.setPassword(passwordEncoder.encode("cliente123"));
            demoClient.setRole(User.Role.ROLE_CLIENT);
            demoClient.setNombre("Juan Perez");
            demoClient.setEmail("juan.perez@email.com");
            demoClient.setTelefono("+57 310 987 6543");
            userRepository.save(demoClient);
        } else {
            demoClient = userRepository.findByUsername("cliente").orElse(null);
        }

        // Proyectos de ejemplo
        if (proyectoRepository.count() == 0) {
            Proyecto p1 = new Proyecto();
            p1.setNombre("Remodelacion integral de apartamento");
            p1.setTipo(Proyecto.TipoProyecto.REMODELACION);
            p1.setDescripcion("Remodelacion completa de apartamento de 120 m2 incluyendo cocina, banos y pisos.");
            p1.setUbicacion("Bogota, Colombia");
            p1.setFechaInicio(LocalDate.of(2024, 3, 1));
            p1.setFechaFin(LocalDate.of(2024, 6, 15));
            p1.setEspecificaciones(Arrays.asList(
                    "Demolicion de tabiques existentes",
                    "Instalacion de cocina integral",
                    "Remodelacion de 2 banos",
                    "Cambio de pisos en areas sociales",
                    "Pintura general"
            ));
            p1.setImagenUrl("https://images.unsplash.com/photo-1503387762-592deb58ef4e?auto=format&fit=crop&w=800&q=80");
            p1.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
            if (demoClient != null) p1.setClienteId(demoClient.getId());
            proyectoRepository.save(p1);

            Proyecto p2 = new Proyecto();
            p2.setNombre("Construccion de casa campestre");
            p2.setTipo(Proyecto.TipoProyecto.NUEVA_OBRA);
            p2.setDescripcion("Construccion desde cero de casa campestre de 300 m2 en zona rural.");
            p2.setUbicacion("Cajica, Colombia");
            p2.setFechaInicio(LocalDate.of(2023, 8, 10));
            p2.setFechaFin(LocalDate.of(2024, 5, 20));
            p2.setEspecificaciones(Arrays.asList(
                    "Cimentacion y estructura en concreto",
                    "Mamposteria en ladrillo",
                    "Instalaciones electricas y hidraulicas",
                    "Acabados rusticos en madera",
                    "Paisajismo exterior"
            ));
            p2.setImagenUrl("https://images.unsplash.com/photo-1518780664697-55e379ad5f6d?auto=format&fit=crop&w=800&q=80");
            p2.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
            proyectoRepository.save(p2);

            Proyecto p3 = new Proyecto();
            p3.setNombre("Renovacion de fachada comercial");
            p3.setTipo(Proyecto.TipoProyecto.REMODELACION);
            p3.setDescripcion("Renovacion de fachada principal de local comercial con nueva imagen corporativa.");
            p3.setUbicacion("Medellin, Colombia");
            p3.setFechaInicio(LocalDate.of(2024, 9, 1));
            p3.setFechaFin(LocalDate.of(2024, 10, 30));
            p3.setEspecificaciones(Arrays.asList(
                    "Diseño arquitectonico de fachada",
                    "Estructura metalica para toldo",
                    "Aplicacion de pintura anticorrosiva",
                    "Instalacion de iluminacion LED"
            ));
            p3.setImagenUrl("https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?auto=format&fit=crop&w=800&q=80");
            p3.setEstado(Proyecto.EstadoProyecto.FINALIZADO);
            if (demoClient != null) p3.setClienteId(demoClient.getId());
            proyectoRepository.save(p3);
        }
    }
}
