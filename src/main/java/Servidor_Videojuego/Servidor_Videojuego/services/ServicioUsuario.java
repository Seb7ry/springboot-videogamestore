package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class
ServicioUsuario implements IServicioUsuario {

    private IServicioVideojuego servicioVideojuego;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ServicioUsuario(IServicioVideojuego servicioVideojuego) {
        this.servicioVideojuego = servicioVideojuego;
    }

    @Override
    public Usuario addUsuario(Usuario usuario) {
        if (usuarioRepository.existsById(usuario.getId())) {
            throw new IllegalArgumentException("Un usuario con el mismo ID ya existe");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUsuario(Usuario usuario, int id) {
        if (usuario.getId() != id) {
            throw new IllegalArgumentException("El ID del usuario no coincide con el ID en la ruta");
        }
        return usuarioRepository.findById(id)
                .map(existingUsuario -> {
                    // Copiar propiedades de 'usuario' a 'existingUsuario', excepto los videojuegos si no están definidos
                    existingUsuario.setNombre(usuario.getNombre());
                    existingUsuario.setEstatura(usuario.getEstatura());
                    existingUsuario.setFechaNacimiento(usuario.getFechaNacimiento());
                    existingUsuario.setEsPremium(usuario.isEsPremium());
                    if (usuario.getVideojuegos() != null && !usuario.getVideojuegos().isEmpty()) {
                        existingUsuario.setVideojuegos(usuario.getVideojuegos());
                    }
                    return usuarioRepository.save(existingUsuario);
                }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    public boolean deleteUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Usuario> buscarUsuario(Integer id, String nombre) {
        return usuarioRepository.findByIdAndNombre(id, nombre);
    }

    @Override
    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public List<Usuario> getUsuarios(Double estatura, Boolean esPremium) {
        if (estatura != null && esPremium != null) {
            return usuarioRepository.findByEstaturaAndEsPremium(estatura, esPremium);
        } else if (estatura != null) {
            return usuarioRepository.findByEstatura(estatura);
        } else if (esPremium != null) {
            return usuarioRepository.findByEsPremium(esPremium);
        } else {
            return usuarioRepository.findAll();
        }
    }

    @Override
    public Optional<Usuario> getUsuario(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> buscarUserId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public boolean existeUsuarioConId(int id) {
        return usuarioRepository.existsById(id);
    }
}


