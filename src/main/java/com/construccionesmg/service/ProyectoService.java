package com.construccionesmg.service;

import com.construccionesmg.model.Proyecto;
import com.construccionesmg.repository.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }

    public List<Proyecto> findAll() {
        return proyectoRepository.findAll();
    }

    public Optional<Proyecto> findById(String id) {
        return proyectoRepository.findById(id);
    }

    public List<Proyecto> findByTipo(Proyecto.TipoProyecto tipo) {
        return proyectoRepository.findByTipo(tipo);
    }

    public List<Proyecto> findByClienteId(String clienteId) {
        return proyectoRepository.findByClienteId(clienteId);
    }

    public List<Proyecto> findByTipoAndClienteId(Proyecto.TipoProyecto tipo, String clienteId) {
        return proyectoRepository.findByTipoAndClienteId(tipo, clienteId);
    }

    public Proyecto save(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    public void deleteById(String id) {
        proyectoRepository.deleteById(id);
    }

    public List<Proyecto> findByClienteIdAndTipo(String clienteId, String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return findByClienteId(clienteId);
        }
        return findByTipoAndClienteId(Proyecto.TipoProyecto.valueOf(tipo), clienteId);
    }
}
