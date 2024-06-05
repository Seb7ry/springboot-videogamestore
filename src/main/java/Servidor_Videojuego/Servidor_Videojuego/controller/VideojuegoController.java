package Servidor_Videojuego.Servidor_Videojuego.controller;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.services.ErrorMessage;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioUsuario;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioVideojuego;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/videojuegos")
@CrossOrigin(origins = "http://localhost:4200")
public class VideojuegoController {

    @Autowired
    private IServicioVideojuego servicioVideojuego;

    @RequestMapping(value = "/healthcheck")
    public String healthCheck() {
        return "Service status fine!";
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<Videojuego> crearYAsociarVideojuego(
            @PathVariable int usuarioId,
            @RequestBody Videojuego videojuego) {
        try {
            Videojuego nuevoVideojuego = servicioVideojuego.addUserToVideojuego(usuarioId, videojuego);
            return new ResponseEntity<>(nuevoVideojuego, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{usuarioId}/{id}")
    public ResponseEntity<Videojuego> updateVideojuego(@PathVariable int usuarioId, @PathVariable int id, @RequestBody Videojuego videojuego) {
        Videojuego updatedVideojuego = servicioVideojuego.updateVideojuego(videojuego, usuarioId, id);
        return ResponseEntity.ok(updatedVideojuego);
    }

    @DeleteMapping("/{usuarioId}/{id}")
    public ResponseEntity<?> deleteVideojuego(@PathVariable int usuarioId, @PathVariable int id) {
        boolean isDeleted = servicioVideojuego.deleteVideojuego(usuarioId, id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getVideojuegosDeUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "multijugador", required = false) Boolean multijugador) {

        List<Videojuego> videojuegos = servicioVideojuego.getVideojuegosDeUsuario(usuarioId).stream()
                .filter(v -> (id == null || v.getId() == id) &&
                        (nombre == null || v.getNombre().equalsIgnoreCase(nombre)) &&
                        (precio == null || v.getPrecio() == precio) &&
                        (multijugador == null || v.isMultijugador() == multijugador))
                .collect(Collectors.toList());

        if (videojuegos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(videojuegos);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> getVideojuegosDeUsuario(@PathVariable int usuarioId) {
        try {
            List<Videojuego> videojuegos = servicioVideojuego.getVideojuegosDeUsuario(usuarioId);
            return ResponseEntity.ok(videojuegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{usuarioId}/{id}")
    public ResponseEntity<Videojuego> getVideojuegoByUsuarioIdAndVideojuegoId(
            @PathVariable int usuarioId,
            @PathVariable int id) {
        Optional<Videojuego> videojuego = servicioVideojuego.getVideojuegoByUsuarioIdAndVideojuegoId(usuarioId, id);
        return videojuego.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Videojuego>> buscarVideojuegos(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "multijugador", required = false) Boolean multijugador) {

        List<Videojuego> videojuegos = servicioVideojuego.buscarVideojuegos(id, nombre, precio, multijugador);

        if (videojuegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }
}
