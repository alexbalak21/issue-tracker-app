package app.security;

import java.util.Arrays;
import java.util.List;

import app.model.Ticket;
import app.service.TicketService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PermissionAspect {

    private final TicketService ticketService;

    public PermissionAspect(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint pjp, RequiresPermission requiresPermission) throws Throwable {

        String[] requiredPermissions = requiresPermission.value();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            List<String> userPermissions = jwtAuth.getPermissions();
            Long currentUserId = jwtAuth.getUserId();

            // 1. Admin / manager → allowed normally
            boolean allowed = Arrays.stream(requiredPermissions)
                    .anyMatch(userPermissions::contains);

            if (allowed) {
                return pjp.proceed();
            }

            // 2. Ownership rules for ticket.read
            if (Arrays.asList(requiredPermissions).contains("ticket.read")) {
                String method = pjp.getSignature().getName();

                if (method.equals("getTicketById")) {
                    Long ticketId = (Long) pjp.getArgs()[0];
                    Ticket t = ticketService.getTicketById(ticketId).orElse(null);

                    if (t != null &&
                        (t.getCreatedBy().equals(currentUserId) ||
                         (t.getAssignedTo() != null && t.getAssignedTo().equals(currentUserId)))) {
                        return pjp.proceed();
                    }
                }

                if (method.equals("getAllTickets")) {
                    return ticketService.getTicketsByCreatedByOrAssignedTo(currentUserId);
                }
            }

            // 3. Ownership rules for ticket.write
            if (Arrays.asList(requiredPermissions).contains("ticket.write")) {
                String method = pjp.getSignature().getName();

                if (method.equals("updateTicket")) {
                    Long ticketId = (Long) pjp.getArgs()[0];
                    Ticket t = ticketService.getTicketById(ticketId).orElse(null);

                    if (t != null && t.getCreatedBy().equals(currentUserId)) {
                        return pjp.proceed();
                    }
                }
            }
        }

        throw new AccessDeniedException("Missing permission or ownership");
    }
}
