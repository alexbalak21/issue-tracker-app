package app.security;

public enum OwnershipType {
    ALL,            // user must have the required permission
    SELF,           // user must own the resource (createdBy or assignedTo)
    ALL_OR_SELF     // user can have permission OR ownership
}
