# Base URL
$baseUrl = "http://localhost:8100"

Write-Host "=== Creating Permissions ===" -ForegroundColor Cyan

# -----------------------------
# 1. Login as Admin
# -----------------------------
Write-Host "`n[1] Logging in as admin..."

$adminLoginBody = @{
    email = "admin@example.com"
    password = "admin123"
} | ConvertTo-Json

$adminLoginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $adminLoginBody -ContentType "application/json"
$adminAccessToken = $adminLoginResponse.data.access_token
Write-Host "Admin Access Token:" $adminAccessToken

$adminHeaders = @{ Authorization = "Bearer $adminAccessToken" }

# -----------------------------
# 2. Permission Definitions
# -----------------------------
$permissions = @(
    # Ticket Permissions
    @{ name = "ticket.create";    description = "Create tickets, read & update own tickets" },
    @{ name = "ticket.read";    description = "Read/view tickets" },
    @{ name = "ticket.write";   description = "Create or update tickets" },
    @{ name = "ticket.assign";  description = "Assign tickets to users" },
    @{ name = "ticket.delete";  description = "Delete tickets" },
    @{ name = "ticket.manage";  description = "Manage tickets" },

    # User Permissions
    @{ name = "user.read";      description = "Read user information" },
    @{ name = "user.write";     description = "Create or update users" },
    @{ name = "user.delete";    description = "Delete users" },
    @{ name = "user.manage";    description = "Full user management access" },

    # Role Permissions
    @{ name = "role.read";      description = "Read roles" },
    @{ name = "role.write";     description = "Create or update roles" },
    @{ name = "role.delete";    description = "Delete roles" },
    @{ name = "role.manage";    description = "Manage roles and their permissions" },

    # Permission Permissions
    @{ name = "permission.read";   description = "Read permissions" },
    @{ name = "permission.write";  description = "Create or update permissions" },
    @{ name = "permission.delete"; description = "Delete permissions" },
    @{ name = "permission.manage"; description = "Manage permissions" },

    # Status Permissions
    @{ name = "status.manage";                 description = "Manage ticket statuses" },
    @{ name = "status.read";                 description = "Read/view ticket statuses" },
    @{ name = "status.write";                description = "Create or update statuses" },
    @{ name = "status.delete";               description = "Delete statuses" },
    @{ name = "status.transition";           description = "Change a ticket's status" },
    @{ name = "status.transition.reopen";    description = "Reopen resolved or closed tickets" },
    @{ name = "status.transition.close";     description = "Close tickets" },
    @{ name = "status.transition.cancel";    description = "Cancel tickets" }


    # Message Permissions
    @{ name = "message.manage";               description = "Manage ticket messages" },
    @{ name = "message.create";               description = "Create ticket messages" },
    @{ name = "message.read";                 description = "Read/view ticket messages" },
    @{ name = "message.write";                description = "Create or update messages" },
    @{ name = "message.delete";               description = "Delete messages" },
    @{ name = "message.transition";           description = "Change a ticket's status" },
    @{ name = "message.transition.reopen";    description = "Reopen resolved or closed tickets" },
    @{ name = "message.transition.close";     description = "Close tickets" },
    @{ name = "message.transition.cancel";    description = "Cancel tickets" }

    # Notes Permissions
    @{ name = "note.manage";                  description = "Manage ticket notes" },
    @{ name = "note.read";                    description = "Read/view ticket notes" },

    # Conversation Permissions
    @{ name = "conversation.manage";         description = "Manage ticket conversations" },
    @{ name = "conversation.read";           description = "Read/view ticket conversations" },
    @{ name = "conversation.reply";           description = "Reply to ticket conversations" } 
)

# -----------------------------
# 3. Create Permissions
# -----------------------------
$index = 1
foreach ($perm in $permissions) {
    Write-Host "`n[$index] Creating permission: $($perm.name)..."

    $permBody = $perm | ConvertTo-Json

    try {
        $response = Invoke-RestMethod `
            -Uri "$baseUrl/api/admin/permissions" `
            -Headers $adminHeaders `
            -Method POST `
            -Body $permBody `
            -ContentType "application/json"

        Write-Host "Created:" ($response | ConvertTo-Json -Depth 5)
    }
    catch {
        Write-Host "Failed to create $($perm.name): $($_.Exception.Message)" -ForegroundColor Yellow
    }

    $index++
}

Write-Host "`n=== Permission Creation Completed ===" -ForegroundColor Green
