package Servidor_Videojuego.Servidor_Videojuego.services;
import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IServicioUsuario {
    Usuario addUsuario(Usuario usuario);
    Usuario updateUsuario(Usuario usuario, int id);
    boolean deleteUsuario(int id);
    Optional<Usuario> buscarUsuario(Integer id, String nombre);
    List<Usuario> getUsuarios();
    List<Usuario> getUsuarios(Double estatura, Boolean esPremium);
    Optional<Usuario> getUsuario(Integer id);
    Optional<Usuario> buscarUserId(Integer id);
    boolean existeUsuarioConId(int id);
}
