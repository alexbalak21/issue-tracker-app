package app.security;

import app.model.Ticket;
import app.service.TicketService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Aspect
@Component
public class PermissionAspect {

    private final TicketService ticketService;

    public PermissionAspect(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Around("@annotation(requiresPermission)")
    public Object check(ProceedingJoinPoint pjp,
                        RequiresPermission requiresPermission) throws Throwable {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof JwtAuthenticationToken jwt)) {
            throw new AccessDeniedException("Not authenticated");
        }

        Long userId = jwt.getUserId();
        List<String> userPermissions = jwt.getPermissions();

        // Required permissions
        String[] requiredPermissions = requiresPermission.value();

        boolean hasPermission = Arrays.stream(requiredPermissions)
                .anyMatch(userPermissions::contains);

        // Read @Ownership annotation if present
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        Ownership ownershipAnn = sig.getMethod().getAnnotation(Ownership.class);

        OwnershipType ownership = ownershipAnn != null
                ? ownershipAnn.value()
                : OwnershipType.ALL;

        // ----------------------------------------------------
        // CASE 1 — ALL: must have permission
        // ----------------------------------------------------
        if (ownership == OwnershipType.ALL) {
            if (!hasPermission) {
                throw new AccessDeniedException("Missing permission");
            }
            return pjp.proceed();
        }

        // ----------------------------------------------------
        // Extract ticketId if present
        // ----------------------------------------------------
        Long ticketId = extractTicketId(pjp.getArgs());

        // LIST endpoint (no ticketId)
        if (ticketId == null) {
            if (ownership == OwnershipType.SELF || ownership == OwnershipType.ALL_OR_SELF) {
                return ticketService.getTicketsByCreatedByOrAssignedTo(userId);
            }
        }

        // SINGLE ticket endpoint
        Ticket ticket = ticketService.getTicketById(ticketId).orElse(null);

        if (ticket == null) {
            throw new AccessDeniedException("Ticket not found");
        }

        boolean ownsTicket =
                ticket.getCreatedBy().equals(userId) ||
                (ticket.getAssignedTo() != null && ticket.getAssignedTo().equals(userId));

        // ----------------------------------------------------
        // CASE 2 — SELF: must own the ticket
        // ----------------------------------------------------
        if (ownership == OwnershipType.SELF) {
            if (!ownsTicket) {
                throw new AccessDeniedException("Not your ticket");
            }
            return pjp.proceed();
        }

        // ----------------------------------------------------
        // CASE 3 — ALL_OR_SELF: permission OR ownership
        // ----------------------------------------------------
        if (ownership == OwnershipType.ALL_OR_SELF) {
            if (hasPermission || ownsTicket) {
                return pjp.proceed();
            }
            throw new AccessDeniedException("Missing permission or ownership");
        }

        throw new AccessDeniedException("Access denied");
    }

    // Extract ticketId from method arguments
    private Long extractTicketId(Object[] args) {
        if (args == null) return null;

        for (Object arg : args) {
            if (arg instanceof Long id) {
                return id;
            }
        }
        return null;
    }
}
