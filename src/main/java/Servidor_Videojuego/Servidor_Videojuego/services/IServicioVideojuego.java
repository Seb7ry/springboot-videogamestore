package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;

import java.util.List;
import java.util.Optional;

public interface IServicioVideojuego {
    Videojuego addUserToVideojuego(int usuarioId, Videojuego videojuego);
    Videojuego updateVideojuego(Videojuego videojuego, int usuarioId, int id);
    boolean deleteVideojuego(int usuarioId, int id);
    List<Videojuego> getVideojuego();
    List<Videojuego> getVideojuegosDeUsuario(int usuarioId);
    Optional<Videojuego> getVideojuegoByUsuarioIdAndVideojuegoId(int usuarioId, int id);
    List<Videojuego> buscarVideojuegos(Integer id, String nombre, Double precio, Boolean multijugador);
}

