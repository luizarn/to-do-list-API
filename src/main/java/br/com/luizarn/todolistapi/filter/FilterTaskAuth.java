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
            var authorization = request.getHeader("Authorization");
            var token = authorization.substring("Bearer".length()).trim();
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