package com.construccionesmg.service;

import com.construccionesmg.model.Cotizacion;
import com.construccionesmg.repository.CotizacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;

    public CotizacionService(CotizacionRepository cotizacionRepository) {
        this.cotizacionRepository = cotizacionRepository;
    }

    public List<Cotizacion> findAll() {
        return cotizacionRepository.findAllByOrderByFechaSolicitudDesc();
    }

    public List<Cotizacion> findByClienteId(String clienteId) {
        return cotizacionRepository.findByClienteIdOrderByFechaSolicitudDesc(clienteId);
    }

    public Optional<Cotizacion> findById(String id) {
        return cotizacionRepository.findById(id);
    }

    public Cotizacion save(Cotizacion cotizacion) {
        return cotizacionRepository.save(cotizacion);
    }

    public void deleteById(String id) {
        cotizacionRepository.deleteById(id);
    }

    public Cotizacion responder(String id, String respuesta, String pdfPath) {
        Cotizacion c = cotizacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cotizacion no encontrada"));
        c.setRespuestaAdmin(respuesta);
        c.setEstado(Cotizacion.EstadoCotizacion.RESPONDIDA);
        if (pdfPath != null) {
            c.setPdfPath(pdfPath);
        }
        return cotizacionRepository.save(c);
    }
}
