package Servidor_Videojuego.Servidor_Videojuego.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorMessage {
    private String code;
    private List<Map<String, String>> mensajes;
}

