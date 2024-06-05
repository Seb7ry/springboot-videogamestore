package Servidor_Videojuego.Servidor_Videojuego.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Videojuego {

    @Id
    private int id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private double precio;

    @Column(name = "multijugador")
    private boolean multijugador;

    @Column(name = "fecha_lanzamiento")
    private LocalDateTime fechaLanzamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @JsonGetter("usuario_id")
    public int getUsuarioId() {
        return usuario != null ? usuario.getId() : null;
    }
}
