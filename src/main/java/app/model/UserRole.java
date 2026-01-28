package app.model;

/**
 * Enum representing a user's role in the system, used for security authorities.
 * Stored as STRING in JPA with default USER.
 */
public enum UserRole {
    /**
     * Administrator role with full access
     */
    ADMIN,

    /**
     * Manager role with elevated access
     */
    MANAGER,
    
    /**
     * Regular user role with standard access
     */
    USER,
    
    /**
     * Agent role with support/agent level access
     */
    AGENT
}
