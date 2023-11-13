package br.com.luizarn.todolistapi.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.luizarn.todolistapi.user.ISessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private ISessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();
        if (servletPath.startsWith("/tasks/")) {
            var method = request.getMethod();
            if ("OPTIONS".equalsIgnoreCase(method)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
                return;
             }
             

            var authorization = request.getHeader("Authorization");
            System.out.println("authorization: " + authorization);
            var token = authorization.replace("Bearer ", "").trim();
            System.out.println("Token: " + token);
            // var token = authorization.substring("Bearer".length()).trim();

            // System.out.println(token);

            var session = this.sessionRepository.findFirstByToken(token);
            if (session == null || session.isEmpty()) {
                response.sendError(401);
            } else {
                request.setAttribute("userId", session.get(0).getUser().getId());
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}