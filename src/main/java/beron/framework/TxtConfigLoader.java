package beron.framework;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class TxtConfigLoader implements ConfigLoader{
    public static final String MENSAJE_ERROR_LECTURA_ARCH = "No se pudo leer el archivo de configuración: ";
    private static final String ACCIONES_KEY = "acciones";
   private static final String SEPARADOR = ";";
    public static final String MENSAJE_ERROR_ARCH_CLAVE = "El archivo de configuracion no conotiene la clave '";

    @Override
    public List<String> cargarNombresClase(String path) {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream(path)){
            props.load(input);
        }catch (Exception e){
            throw  new RuntimeException(MENSAJE_ERROR_LECTURA_ARCH + path + e);
        }
        String line = props.getProperty(ACCIONES_KEY);
        if (line == null || line.isBlank()){
            throw new RuntimeException(MENSAJE_ERROR_ARCH_CLAVE + ACCIONES_KEY + "'");

        }

        String[]nombres = line.split(SEPARADOR);
        return Arrays.stream(nombres)
                .map(String::trim)
                .filter(n -> !n.isEmpty())
                .toList();
    }
}
