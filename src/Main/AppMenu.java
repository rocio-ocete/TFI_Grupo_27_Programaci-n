package main;

import entities.Usuario;
import entities.CredencialAcceso;
import Service.UsuarioService;
import Service.CredencialAccesoService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    private final Scanner sc = new Scanner(System.in);
    private final UsuarioService usuarioService = new UsuarioService();
    private final CredencialAccesoService credService = new CredencialAccesoService();

    public void iniciar() {

        String op;

        do {
            System.out.println("\n===== MENU PRINCIPAL =====");
            System.out.println("1) Usuarios");
            System.out.println("2) Credenciales");
            System.out.println("0) Salir");
            System.out.print("Elija opción: ");

            op = sc.nextLine().trim();

            switch (op) {
                case "1" -> menuUsuarios();
                case "2" -> menuCredenciales();
                case "0" -> System.out.println("¡Saliendo!");
                default -> System.out.println("Opción inválida.");
            }

        } while (!op.equals("0"));
    }

    // ============================
    //           USUARIOS
    // ============================

    private void menuUsuarios() {
        String op;

        do {
            System.out.println("\n--- MENU USUARIOS ---");
            System.out.println("1) Crear usuario");
            System.out.println("2) Buscar por ID");
            System.out.println("3) Listar todos");
            System.out.println("4) Actualizar");
            System.out.println("5) Eliminar");
            System.out.println("6) Buscar por Username");
            System.out.println("0) Volver");
            System.out.print("Opción: ");

            op = sc.nextLine();

            try {
                switch (op) {
                    case "1" -> crearUsuario();
                    case "2" -> leerUsuario();
                    case "3" -> listarUsuarios();
                    case "4" -> actualizarUsuario();
                    case "5" -> eliminarUsuario();
                    case "6" -> buscarPorUsername();
                    case "0" -> {}
                    default -> System.out.println("Opción inválida.");
                }

            } catch (Exception e) {
                System.out.println("⚠ Error: " + e.getMessage());
            }

        } while (!op.equals("0"));
    }

    private void crearUsuario() throws Exception {

        Usuario u = new Usuario();

        System.out.print("Username: ");
        u.setUsername(sc.nextLine());

        System.out.print("Email: ");
        u.setEmail(sc.nextLine());

        u.setActivo(true);
        u.setFechaRegistro(LocalDateTime.now());
        u.setEliminado(false);

        // credencial asociada
        CredencialAcceso c = new CredencialAcceso();
        System.out.print("Hash password: ");
        c.setHashPassword(sc.nextLine());
        c.setSalt("salt-" + System.currentTimeMillis());
        c.setUltimoCambio(LocalDateTime.now());
        c.setRequiereReset(false);
        c.setEliminado(false);

        u.setCredencial(c);

        usuarioService.insertar(u);

        System.out.println("✔ Usuario creado con éxito");
    }

    private void leerUsuario() throws Exception {
        System.out.print("ID: ");
        long id = Long.parseLong(sc.nextLine());

        Usuario u = usuarioService.getById(id);

        if (u == null)
            System.out.println("No existe ese usuario.");
        else
            System.out.println(u);
    }

    private void listarUsuarios() throws Exception {
        List<Usuario> lista = usuarioService.getAll();
        lista.forEach(System.out::println);
    }

    private void actualizarUsuario() throws Exception {

        System.out.print("ID del usuario a actualizar: ");
        long id = Long.parseLong(sc.nextLine());

        Usuario u = usuarioService.getById(id);

        if (u == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        System.out.print("Nuevo email: ");
        u.setEmail(sc.nextLine());

        usuarioService.actualizar(u);

        System.out.println("✔ Usuario actualizado.");
    }

    private void eliminarUsuario() throws Exception {

        System.out.print("ID del usuario: ");
        long id = Long.parseLong(sc.nextLine());

        if (usuarioService.eliminar(id))
            System.out.println("✔ Eliminado.");
        else
            System.out.println("No existe ese ID.");
    }

    private void buscarPorUsername() throws Exception {

        System.out.print("Username: ");
        String username = sc.nextLine();

        List<Usuario> all = usuarioService.getAll();

        all.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("No encontrado.")
                );
    }

    // ============================
    //        CREDENCIALES
    // ============================

    private void menuCredenciales() {
    String op;

    do {
        System.out.println("\n--- MENU CREDENCIALES ---");
        System.out.println("1) Crear credencial");
        System.out.println("2) Buscar por ID");
        System.out.println("3) Listar todas");
        System.out.println("4) Actualizar");
        System.out.println("5) Eliminar");
        System.out.println("0) Volver");
        System.out.print("Opción: ");

        op = sc.nextLine();

        try {
            switch (op) {
                case "1" -> crearCredencial();
                case "2" -> leerCredencial();
                case "3" -> listarCredenciales();
                case "4" -> actualizarCredencial();
                case "5" -> eliminarCredencial();
                case "0" -> {}
                default -> System.out.println("Opción inválida.");
            }
        } catch (Exception e) {
            System.out.println("⚠ Error: " + e.getMessage());
        }

    } while (!op.equals("0"));
}

// =====================
// Métodos CRUD de credenciales
// =====================

private void crearCredencial() throws Exception {
    CredencialAcceso c = new CredencialAcceso();

    System.out.print("Hash password: ");
    c.setHashPassword(sc.nextLine());

    c.setSalt("salt-" + System.currentTimeMillis());
    c.setUltimoCambio(LocalDateTime.now());
    c.setRequiereReset(false);
    c.setEliminado(false);

    credService.insertar(c);

    System.out.println("✔ Credencial creada con éxito, ID: " + c.getId());
}

private void leerCredencial() throws Exception {
    System.out.print("ID: ");
    long id = Long.parseLong(sc.nextLine());

    CredencialAcceso c = credService.getById(id);

    if (c == null)
        System.out.println("No existe esa credencial.");
    else
        System.out.println(c);
}

private void listarCredenciales() throws Exception {
    List<CredencialAcceso> lista = credService.getAll();
    lista.forEach(System.out::println);
}

private void actualizarCredencial() throws Exception {
    System.out.print("ID de la credencial a actualizar: ");
    long id = Long.parseLong(sc.nextLine());

    CredencialAcceso c = credService.getById(id);

    if (c == null) {
        System.out.println("Credencial no encontrada.");
        return;
    }

    System.out.print("Nuevo hash password: ");
    c.setHashPassword(sc.nextLine());
    c.setUltimoCambio(LocalDateTime.now());

    credService.actualizar(c);

    System.out.println("✔ Credencial actualizada.");
}

private void eliminarCredencial() throws Exception {
    System.out.print("ID de la credencial: ");
    long id = Long.parseLong(sc.nextLine());

    if (credService.eliminar(id))
        System.out.println("✔ Credencial eliminada.");
    else
        System.out.println("No existe esa ID.");
    }
}

