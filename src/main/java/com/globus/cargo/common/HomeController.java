package com.globus.cargo.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home(HttpServletRequest request) {
        String base = buildBaseUrl(request);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "Bienvenido a la API de Usuarios (memoria temporal)");
        body.put("baseUrl", base);
        body.put("comoUsar", "Registra usuarios con POST /users enviando JSON {id,name,email}. Lista usuarios con GET /users.");

        body.put("endpoints", List.of(
                Map.of("method", "POST", "url", base + "/users", "bodyEjemplo", Map.of(
                        "id", 1,
                        "name", "Juan Pérez",
                        "email", "juan.perez@example.com"
                )),
                Map.of("method", "GET", "url", base + "/users")
        ));

        body.put("ejemplos", Map.of(
                "PowerShell_GET", "Invoke-RestMethod -Uri " + base + "/users",
                "PowerShell_POST", "Invoke-RestMethod -Method Post -Uri " + base + "/users -ContentType 'application/json' -Body '{\"id\":1,\"name\":\"Juan Pérez\",\"email\":\"juan.perez@example.com\"}'",
                "curl_GET", "curl \"" + base + "/users\"",
                "curl_POST", "curl -X POST \"" + base + "/users\" -H 'Content-Type: application/json' -d '{\"id\":1,\"name\":\"Juan Pérez\",\"email\":\"juan.perez@example.com\"}'"
        ));

        return body;
    }

    private String buildBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String host = request.getServerName();
        int port = request.getServerPort();
        String context = request.getContextPath();
        boolean isDefaultPort = ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
        String portPart = isDefaultPort ? "" : ":" + port;
        return scheme + "://" + host + portPart + (context == null ? "" : context);
    }
}

