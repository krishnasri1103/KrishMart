package com.krishna.krishmart.util;

import com.google.gson.Gson;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** JSON serialization helper for AJAX endpoints. */
public final class JsonUtil {
    private static final Gson GSON = new Gson();
    private JsonUtil() {}
    /** Writes a JSON response with the requested status code. */
    public static void write(HttpServletResponse response, int status, Object value) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(GSON.toJson(value));
    }
    /** Deserializes a JSON request body. */
    public static <T> T read(javax.servlet.http.HttpServletRequest request, Class<T> type) throws IOException {
        return GSON.fromJson(request.getReader(), type);
    }
}