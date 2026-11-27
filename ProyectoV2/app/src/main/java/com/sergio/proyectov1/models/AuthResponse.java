package com.sergio.proyectov1.models;

public class AuthResponse {

    private boolean success;
    private String message;
    private UserData usuario;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUsuario() {
        return usuario;
    }

    public void setUsuario(UserData usuario) {
        this.usuario = usuario;
    }

    public static class UserData {
        private int id_usuario;
        private String nombre;
        private String nombre_usuario;
        private String correo;
        private String rol;

        public int getId_usuario() {
            return id_usuario;
        }

        public void setId_usuario(int id_usuario) {
            this.id_usuario = id_usuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre_usuario() {
            return nombre_usuario;
        }

        public void setNombre_usuario(String nombre_usuario) {
            this.nombre_usuario = nombre_usuario;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getRol() {
            return rol != null ? rol : "usuario";
        }

        public void setRol(String rol) {
            this.rol = rol;
        }

        public boolean isAdmin() {
            return "admin".equalsIgnoreCase(getRol()) || (correo != null && correo.toLowerCase().contains("admin"));
        }
    }
}
