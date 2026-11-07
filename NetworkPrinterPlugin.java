package com.example.caisse;

import android.util.Log;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CapacitorPlugin(name = "NetworkPrinter")
public class NetworkPrinterPlugin extends Plugin {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PluginMethod
    public void print(PluginCall call) {
        String host = call.getString("host");
        Integer port = call.getInt("port", 9100);
        JSArray values = call.getArray("data");
        if (host == null || host.trim().isEmpty() || port == null || port < 1 || port > 65535 || values == null) {
            call.reject("Paramètres d’impression réseau invalides");
            return;
        }
        executor.execute(() -> {
            try {
                byte[] bytes = new byte[values.length()];
                for (int i = 0; i < values.length(); i++) bytes[i] = (byte) (values.getInt(i) & 0xFF);
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress(host.trim(), port), 6000);
                    socket.setSoTimeout(6000);
                    OutputStream out = socket.getOutputStream();
                    out.write(bytes);
                    out.flush();
                    socket.shutdownOutput();
                }
                call.resolve();
            } catch (Exception e) {
                call.reject("Échec impression réseau : " + e.getMessage(), e);
            }
        });
    }
}
