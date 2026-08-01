package com.construccionesmg.repository;

import com.construccionesmg.model.Proyecto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProyectoRepository extends MongoRepository<Proyecto, String> {
    List<Proyecto> findByTipo(Proyecto.TipoProyecto tipo);
    List<Proyecto> findByClienteId(String clienteId);
    List<Proyecto> findByTipoAndClienteId(Proyecto.TipoProyecto tipo, String clienteId);
}
