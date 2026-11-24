package com.example.backend.watcher;

import com.example.backend.model.Bloqueos;
import com.example.backend.service.ParserLog;

import java.util.Optional;

import org.apache.commons.io.input.TailerListenerAdapter;

public class LogTailerListener extends TailerListenerAdapter {

    private final ParserLog parserService = new ParserLog();

    @Override
    public void handle(String line) {
        Optional<Bloqueos> bloqueos =parserService.parsearLinea(line);

        if( bloqueos.isPresent()){
            Bloqueos bloqueo = bloqueos.get();
            System.out.println("🚨 BLOQUEO DETECTADO: " + bloqueo.getProtocolo() 
                + " | Origen: " + bloqueo.getSourceIp() + ":" + bloqueo.getSourcePort()
                + " -> Destino: " + bloqueo.getDestinationIp() + ":" + bloqueo.getDestinationPort());



        }
    }

    @Override
    public void handle(Exception ex) {
        System.err.println("Error en el monitor de archivo: " + ex.getMessage());
    }
}