package com.Valverde.facturacion.almacen.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Encoder {
    public static String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
    public boolean decode(String decode, String numero) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(decode, numero);
    }
}
