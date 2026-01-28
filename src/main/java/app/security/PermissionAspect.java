package app.security;

import java.util.List;

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

    @Around("@annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint pjp, RequiresPermission requiresPermission) throws Throwable {

        String required = requiresPermission.value();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            List<String> userPermissions = jwtAuth.getPermissions();

            if (userPermissions.contains(required)) {
                return pjp.proceed();
            }
        }

        throw new AccessDeniedException("Missing permission: " + required);
    }
}
