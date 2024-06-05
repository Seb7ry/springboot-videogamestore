package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.repositories.VideoJuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioVideojuego implements IServicioVideojuego {

    @Autowired
    private final VideoJuegoRepository videoJuegoRepository;

    @Autowired
    private final IServicioUsuario servicioUsuario;

    @Override
    public Videojuego addUserToVideojuego(int usuarioId, Videojuego videojuego) {
        Usuario usuario = servicioUsuario.buscarUserId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        if (videoJuegoRepository.existsById(videojuego.getId())) {
            throw new IllegalArgumentException("Un videojuego con el mismo ID ya existe");
        }

        videojuego.setUsuario(usuario);
        return videoJuegoRepository.save(videojuego);
    }

    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int usuarioId, int id) {
        return videoJuegoRepository.findById(id)
                .map(existingVideojuego -> {
                    if (existingVideojuego.getUsuario().getId() != usuarioId) {
                        throw new IllegalArgumentException("Videojuego no pertenece al usuario con ID: " + usuarioId);
                    }
                    existingVideojuego.setNombre(videojuego.getNombre());
                    existingVideojuego.setPrecio(videojuego.getPrecio());
                    existingVideojuego.setMultijugador(videojuego.isMultijugador());
                    existingVideojuego.setFechaLanzamiento(videojuego.getFechaLanzamiento());
                    return videoJuegoRepository.save(existingVideojuego);
                })
                .orElseThrow(() -> new RuntimeException("Videojuego no encontrado con ID: " + id));
    }

    @Override
    public boolean deleteVideojuego(int usuarioId, int id) {
        Optional<Videojuego> videojuegoOptional = videoJuegoRepository.findByUsuario_IdAndId(usuarioId, id);
        if (videojuegoOptional.isPresent()) {
            videoJuegoRepository.delete(videojuegoOptional.get());
            return true;
        }
        return false;
    }

    @Override
    public List<Videojuego> getVideojuego() {
        return videoJuegoRepository.findAll();
    }

    @Override
    public List<Videojuego> getVideojuegosDeUsuario(int usuarioId) {
        Usuario usuario = servicioUsuario.buscarUserId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        return usuario.getVideojuegos();
    }

    @Override
    public Optional<Videojuego> getVideojuegoByUsuarioIdAndVideojuegoId(int usuarioId, int id) {
        return videoJuegoRepository.findByUsuario_IdAndId(usuarioId, id); // Cambiado aquí
    }

    @Override
    public List<Videojuego> buscarVideojuegos(Integer id, String nombre, Double precio, Boolean multijugador) {
        return videoJuegoRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (nombre != null) {
                predicates.add(cb.equal(root.get("nombre"), nombre));
            }
            if (precio != null) {
                predicates.add(cb.equal(root.get("precio"), precio));
            }
            if (multijugador != null) {
                predicates.add(cb.equal(root.get("multijugador"), multijugador));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
    }
}