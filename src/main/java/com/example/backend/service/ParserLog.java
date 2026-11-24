package com.example.backend.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.example.backend.enums.Protocolos;
import com.example.backend.model.Bloqueos;

public class ParserLog {

    // Crea un formateador de fechas inmutable y constante para toda la clase.
// Define el patrón exacto con el que Windows Firewall guarda la fecha y hora (ej: "2026-11-23 19:28:00").
private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

// Método público que recibe una línea de texto cruda del log.
// Devuelve Optional<Bloqueos> para evitar retornar 'null' si la línea no sirve o no es un bloqueo.
public Optional<Bloqueos> parsearLinea(String linea) {

    // Filtro inicial: descarta la línea si es nula, si está vacía/solo espacios, 
    // o si empieza con '#' (Windows Firewall usa '#' para encabezados y comentarios al inicio del archivo).
    if (linea == null || linea.trim().isEmpty() || linea.startsWith("#")) {
        return Optional.empty();
    }

    // Elimina espacios sobrantes en los bordes (trim) y divide la línea en un arreglo de palabras.
    // La expresión regular "\\s+" corta por uno o más espacios en blanco consecutivos.
    String[] campos = linea.trim().split("\\s+");

    // Verifica que la línea tenga la estructura mínima esperada (fecha, hora, acción, protocolo, IPs y puertos).
    // Si tiene menos de 8 columnas, es una línea corrupta o incompleta y se descarta.
    if (campos.length < 8) {
        return Optional.empty();
    }

    // Extrae la tercera columna (índice 2), que indica si el paquete fue permitido (ALLOW) o bloqueado (DROP).
    String accion = campos[2]; 

    // Si la acción NO es "DROP" (ignorando mayúsculas/minúsculas), ignora el registro.
    // Solo nos interesa capturar tráfico bloqueado.
    //ACA PUEDO USARLO A FUTURO PARA VER QUE PERMITO Y QUE NO 
    if (!"DROP".equalsIgnoreCase(accion)) {
        return Optional.empty();
    }

    // Bloque try-catch para capturar errores de formato (ej. un puerto con letras o fecha mal formada)
    // sin que una línea corrupta detenga todo el monitoreo en tiempo real.
    try {
        // Une la columna 0 (fecha: "2026-11-23") y la columna 1 (hora: "19:28:00") con un espacio en el medio.
        String fechaStr = campos[0] + " " + campos[1];

        // Convierte ese texto en un objeto real LocalDateTime usando el patrón definido arriba.
        LocalDateTime fecha = LocalDateTime.parse(fechaStr, FORMATTER);

        // Extrae el protocolo (columna 3, ej: "TCP", "UDP", "ICMP") y lo pasa a mayúsculas.
        String protocolo = campos[3].toUpperCase();


        // Intenta hacer coincidir el texto con una constante de tu Enum Protocolos.
      

        // Extrae la IP de origen (columna 4) y la IP de destino (columna 5).
        String sourceIp = campos[4];
        String destinationIp = campos[5];

        // Extrae el puerto de origen (columna 6).
        // En protocolos sin puertos (como ICMP / ping), Windows escribe un guion "-".
        // Si es "-", le asigna 0; si no, convierte el texto a número entero (int).
        int sourcePort = "-".equals(campos[6]) ? 0 : Integer.parseInt(campos[6]);

        // Misma validación para el puerto de destino (columna 7).
        int destinationPort = "-".equals(campos[7]) ? 0 : Integer.parseInt(campos[7]);

        // La ruta/dirección del tráfico ("SEND" o "RECEIVE", columna 8) es opcional según la versión de Windows.
        // Si el arreglo tiene más de 8 elementos, la toma; de lo contrario, asigna "N/A".
        String path = campos.length > 8 ? campos[8] : "N/A";

        // Instancia el objeto Bloqueos con todos los datos ya tipados y limpios, 
        // y lo devuelve envuelto dentro de un Optional.
        return Optional.of(new Bloqueos(fecha, protocolo, sourceIp, destinationIp, sourcePort, destinationPort, accion, path));

    } catch (Exception e) {
        // Si algo falla en el parseo (ej. NumberFormatException), imprime el error en consola
        // y devuelve un Optional vacío para que el listener siga leyendo la siguiente línea.
        System.err.println("Error procesando línea: " + linea + " | " + e.getMessage());
        return Optional.empty();
    }
}

}
