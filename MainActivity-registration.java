// Ajouter l’import et l’enregistrement dans MainActivity.java du projet Capacitor.
import com.example.caisse.NetworkPrinterPlugin;

@Override
public void onCreate(android.os.Bundle savedInstanceState) {
    registerPlugin(NetworkPrinterPlugin.class);
    super.onCreate(savedInstanceState);
}
