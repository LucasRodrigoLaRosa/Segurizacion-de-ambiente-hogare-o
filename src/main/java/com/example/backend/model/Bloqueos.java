package com.example.backend.model;
import java.time.LocalDateTime;



public class Bloqueos {
    private LocalDateTime fecha;
    private String protocolo;
    private String sourceIp;
    private String destinationIp;
    private int sourcePort;
    private int destinationPort;
    private String action;
    private String path;


    
    public Bloqueos() {
    }

    public Bloqueos(LocalDateTime fecha, String protocolo, String sourceIp, String destinationIp, int sourcePort,
            int destinationPort, String action, String path) {
        this.fecha = fecha;
        this.protocolo = protocolo;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.action = action;
        this.path = path;
    }
    
    public LocalDateTime getFecha() {
        return fecha;
    }
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
  
    public String getSourceIp() {
        return sourceIp;
    }
    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }
    public String getDestinationIp() {
        return destinationIp;
    }
    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }
    public int getSourcePort() {
        return sourcePort;
    }
    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }
    public int getDestinationPort() {
        return destinationPort;
    }
    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }


}
