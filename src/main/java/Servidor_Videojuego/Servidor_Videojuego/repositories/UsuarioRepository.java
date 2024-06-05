package Servidor_Videojuego.Servidor_Videojuego.repositories;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByIdAndNombre(Integer id, String nombre);
    Optional<Usuario> findById(Integer id);
    List<Usuario> findByEstaturaAndEsPremium(Double estatura, Boolean esPremium);
    List<Usuario> findByEstatura(Double estatura);
    List<Usuario> findByEsPremium(Boolean esPremium);
}
