package app.security;

import app.model.Ticket;
import app.model.Conversation;
import app.service.TicketService;
import app.service.ConversationService;
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
    private final ConversationService conversationService;

    public PermissionAspect(TicketService ticketService,
                            ConversationService conversationService) {
        this.ticketService = ticketService;
        this.conversationService = conversationService;
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

        String[] requiredPermissions = requiresPermission.value();

        boolean hasPermission = Arrays.stream(requiredPermissions)
                .anyMatch(userPermissions::contains);

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
        // Extract ID (ticketId or conversationId)
        // ----------------------------------------------------
        Long id = extractId(pjp.getArgs());

        if (id == null) {
            // LIST endpoints
            if (ownership == OwnershipType.SELF || ownership == OwnershipType.ALL_OR_SELF) {
                return ticketService.getTicketsByCreatedByOrAssignedTo(userId);
            }
        }

        // ----------------------------------------------------
        // Try to load a Ticket first
        // ----------------------------------------------------
        Ticket ticket = ticketService.getTicketById(id).orElse(null);

        if (ticket != null) {
            boolean ownsTicket =
                    ticket.getCreatedBy().equals(userId) ||
                    (ticket.getAssignedTo() != null && ticket.getAssignedTo().equals(userId));

            if (ownership == OwnershipType.SELF && !ownsTicket)
                throw new AccessDeniedException("Not your ticket");

            if (ownership == OwnershipType.ALL_OR_SELF && !(hasPermission || ownsTicket))
                throw new AccessDeniedException("Missing permission or ownership");

            return pjp.proceed();
        }

        // ----------------------------------------------------
        // If not a ticket, try Conversation
        // ----------------------------------------------------
        Conversation conv = conversationService.getConversationById(id);

        if (conv != null) {
            Long ticketOwner = conv.getTicket().getCreatedBy();

            boolean ownsConversation = ticketOwner.equals(userId);

            if (ownership == OwnershipType.SELF && !ownsConversation)
                throw new AccessDeniedException("Not your conversation");

            if (ownership == OwnershipType.ALL_OR_SELF && !(hasPermission || ownsConversation))
                throw new AccessDeniedException("Missing permission or ownership");

            return pjp.proceed();
        }

        throw new AccessDeniedException("Resource not found");
    }

    private Long extractId(Object[] args) {
        if (args == null) return null;

        for (Object arg : args) {
            if (arg instanceof Long l) return l;
            if (arg instanceof Integer i) return i.longValue();
        }
        return null;
    }
}
