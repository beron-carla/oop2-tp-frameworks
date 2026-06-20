package beron.framework;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Mandatum {
    private List<Accion> acciones;
    private ConfigLoader configLoader;
    private String configPath;

    public Mandatum(ConfigLoader configLoader, String configPath) {
        this.configLoader = configLoader;
        this.configPath = configPath;
        this.acciones = cargarAcciones();
    }

    public final void run() {
        Scanner scanner = new Scanner(System.in);
        String input;

        do {
            mostrarMenu();
            System.out.println("> ");
            input = scanner.nextLine().trim();

        } while (procesarInput(input));
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n---Menú---");
        for (int i = 0; i < acciones.size(); i++) {
            Accion a = acciones.get(i);
            System.out.printf("%d. %s - %s%n", i + 1, a.nombreItemMenu(), a.descripcionItemMenu());
            System.out.println("0. Salir");
        }
    }

    private boolean procesarInput(String input) {
        if ("0".equals(input) || "Salir".equalsIgnoreCase(input)) {
            System.out.println("Saliendo...");
            return false;
        }
        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < acciones.size()) {
                acciones.get(idx).ejecutar();

            } else {
                System.out.println("Opcion inválida . ingresá entre 0 y " + acciones.size());

            }
        } catch (NumberFormatException e) {
            System.out.println("Ingresá un número válido");
        }
        return true;

    }

    private List<Accion> cargarAcciones() {
        this.acciones = new ArrayList<>();
        List<String> nombres = configLoader.cargarNombresClase(configPath);
        List<String> errores = new ArrayList<>();
        for (String nombre : nombres) {
            //reflection
            try {
                Class<?> clase = Class.forName(nombre.trim());
                Accion accion = (Accion) clase.getDeclaredConstructor().newInstance();
                acciones.add(accion);

            } catch (ClassNotFoundException e) {
                errores.add(nombre + "- clase no encontrada");
            } catch (NoSuchMethodException e) {
                errores.add(nombre + "- sin constructor sin argumentos");
            } catch (InvocationTargetException e) {
                errores.add(nombre + "- el constructor lanzó: " + e.getCause().getMessage());
            } catch (InstantiationException | IllegalAccessException e) {
                errores.add(nombre + "- " + e.getMessage());
            }

            if (acciones.isEmpty()) {
                throw new RuntimeException("No se pudo cargar ninguna accion. Errores: \n" +
                        String.join("\n", errores));

            }
            if (!errores.isEmpty()) {
                System.err.println("Se cargaron " + acciones.size() + "accion(es)." +
                        errores.size() + "fallaron: ");
                errores.forEach(e -> System.err.println("- " + e));
            }

        }
        return acciones;
    }
}



