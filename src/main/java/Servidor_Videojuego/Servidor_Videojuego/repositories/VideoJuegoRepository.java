package Servidor_Videojuego.Servidor_Videojuego.repositories;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoJuegoRepository extends JpaRepository<Videojuego, Integer>, JpaSpecificationExecutor<Videojuego> {
    Optional<Videojuego> findById(Integer id);
    Optional<Videojuego> findByNombre(String nombre);
    List<Videojuego> findByPrecio(Double precio);
    List<Videojuego> findByMultijugador(Boolean multijugador);
    Optional<Videojuego> findByUsuario_IdAndId(int usuarioId, int id);
}
