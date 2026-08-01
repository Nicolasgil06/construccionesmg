package com.construccionesmg.repository;

import com.construccionesmg.model.Cotizacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CotizacionRepository extends MongoRepository<Cotizacion, String> {
    List<Cotizacion> findByClienteIdOrderByFechaSolicitudDesc(String clienteId);
    List<Cotizacion> findAllByOrderByFechaSolicitudDesc();
}
