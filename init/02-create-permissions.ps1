param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[2] Creating permissions..."

$headers = @{ Authorization = "Bearer $Global:AdminToken" }

$permissions = @(
    @{ name = "ticket.create"; description = "Create tickets, read & update own tickets" },
    @{ name = "ticket.read"; description = "Read/view tickets" },
    @{ name = "ticket.write"; description = "Create or update tickets" },
    @{ name = "ticket.assign"; description = "Assign tickets to users" },
    @{ name = "ticket.delete"; description = "Delete tickets" },
    @{ name = "ticket.manage"; description = "Manage tickets" },

    @{ name = "user.read"; description = "Read user information" },
    @{ name = "user.write"; description = "Create or update users" },
    @{ name = "user.delete"; description = "Delete users" },
    @{ name = "user.manage"; description = "Full user management access" },

    @{ name = "role.read"; description = "Read roles" },
    @{ name = "role.write"; description = "Create or update roles" },
    @{ name = "role.delete"; description = "Delete roles" },
    @{ name = "role.manage"; description = "Manage roles and their permissions" },

    @{ name = "permission.read"; description = "Read permissions" },
    @{ name = "permission.write"; description = "Create or update permissions" },
    @{ name = "permission.delete"; description = "Delete permissions" },
    @{ name = "permission.manage"; description = "Manage permissions" },

    @{ name = "status.manage"; description = "Manage ticket statuses" },
    @{ name = "status.read"; description = "Read/view ticket statuses" },
    @{ name = "status.write"; description = "Create or update statuses" },
    @{ name = "status.delete"; description = "Delete statuses" },
    @{ name = "status.transition"; description = "Change a ticket's status" },
    @{ name = "status.transition.reopen"; description = "Reopen resolved or closed tickets" },
    @{ name = "status.transition.close"; description = "Close tickets" },
    @{ name = "status.transition.cancel"; description = "Cancel tickets" },

    @{ name = "message.manage"; description = "Manage ticket messages" },
    @{ name = "message.create"; description = "Create ticket messages" },
    @{ name = "message.read"; description = "Read/view ticket messages" },
    @{ name = "message.write"; description = "Create or update messages" },
    @{ name = "message.delete"; description = "Delete messages" },
    @{ name = "message.transition"; description = "Change a ticket's status" },
    @{ name = "message.transition.reopen"; description = "Reopen resolved or closed tickets" },
    @{ name = "message.transition.close"; description = "Close tickets" },
    @{ name = "message.transition.cancel"; description = "Cancel tickets" },

    @{ name = "note.manage"; description = "Manage ticket notes" },
    @{ name = "note.read"; description = "Read/view ticket notes" },

    @{ name = "conversation.manage"; description = "Manage ticket conversations" },
    @{ name = "conversation.read"; description = "Read/view ticket conversations" },
    @{ name = "conversation.write"; description = "Create or update ticket conversations" },
    @{ name = "conversation.reply"; description = "Reply to ticket conversations" }
)

$index = 1
foreach ($perm in $permissions) {
    Write-Host "[$index] Creating $($perm.name)..."

    try {
        Invoke-RestMethod `
            -Uri "$BaseUrl/api/admin/permissions" `
            -Headers $headers `
            -Method POST `
            -Body ($perm | ConvertTo-Json) `
            -ContentType "application/json"

        Write-Host "Created $($perm.name)"
    }
    catch {
        Write-Host "Already exists or failed: $($perm.name)"
    }

    $index++
}
