package com.example.backend.Firewall;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EjecutorDeReglas {
    
    public static boolean habilitarIp(String ipDestino, int puerto, String protocolo) {
        String ruleName = "Auto_Allow_" + ipDestino + "_" + puerto;
        String command = String.format(
            "netsh advfirewall firewall add rule name=\"%s\" dir=out action=allow remoteip=%s remoteport=%d protocol=%s",
            ruleName, ipDestino, puerto, protocolo
        );

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                System.out.println("Regla aplicada con éxito: " + ruleName);
                return true;
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                System.err.println("Falló netsh (¿Ejecutaste como Administrador?): " + reader.readLine());
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error ejecutando netsh: " + e.getMessage());
            return false;
        }
    }

}
