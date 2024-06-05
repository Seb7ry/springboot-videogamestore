package Servidor_Videojuego.Servidor_Videojuego.controller;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.services.ErrorMessage;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")

@CrossOrigin(origins = {"http://127.0.0.1:8000", "http://localhost:4200/"})
public class UsuarioController {

    @Autowired
    private IServicioUsuario servicioUsuario;

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "Service status fine!";
    }

    @PostMapping    
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = servicioUsuario.addUsuario(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@RequestBody Usuario usuario, @PathVariable int id) {
        Usuario updatedUsuario = servicioUsuario.updateUsuario(usuario, id);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        boolean isDeleted = servicioUsuario.deleteUsuario(id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsuarios(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "estatura", required = false) Double estatura,
            @RequestParam(value = "esPremium", required = false) Boolean esPremium) {

        if (id != null || nombre != null) {
            Optional<Usuario> resultado = servicioUsuario.getUsuario(id);
            return resultado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else if (estatura != null || esPremium != null) {
            List<Usuario> usuarios = servicioUsuario.getUsuarios(estatura, esPremium);
            return ResponseEntity.ok(usuarios);
        } else {
            List<Usuario> usuarios = servicioUsuario.getUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Integer id){
        if(id != null){
            Optional<Usuario> resultado = servicioUsuario.buscarUserId(id);
            return resultado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            List<Usuario> usuarios = servicioUsuario.getUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }

    private String formatMessage(BindingResult result){
        List<Map<String, String>> errores = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .mensajes(errores)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}