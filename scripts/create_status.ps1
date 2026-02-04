# Base URL
$baseUrl = "http://localhost:8100"

Write-Host "=== Creating Statuses ===" -ForegroundColor Cyan

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
# 2. Create Statuses
# -----------------------------
$statuses = @(
    @{ name = "Open";        description = "Ticket has been created and is awaiting action"; color = "blue" },
    @{ name = "In Progress"; description = "Work has started and is actively being handled"; color = "violet" },
    @{ name = "Waiting";     description = "Ticket is paused, pending user input or information"; color = "yellow" },
    @{ name = "On Hold";     description = "Ticket is temporarily on hold"; color = "orange" },
    @{ name = "Resolved";    description = "Issue has been resolved"; color = "green" },
    @{ name = "Closed";      description = "Ticket is closed"; color = "gray" },
    @{ name = "Canceled";    description = "Ticket has been canceled"; color = "brown" }
)

$index = 1
foreach ($status in $statuses) {
    Write-Host "`n[$index] Creating status: $($status.name)..."
    $statusBody = $status | ConvertTo-Json

    try {
        $response = Invoke-RestMethod `
            -Uri "$baseUrl/api/status" `
            -Headers $adminHeaders `
            -Method POST `
            -Body $statusBody `
            -ContentType "application/json"

        Write-Host "Created:" ($response | ConvertTo-Json -Depth 5)
    }
    catch {
        Write-Host "Failed to create $($status.name): $($_.Exception.Message)" -ForegroundColor Yellow
    }

    $index++
}

Write-Host "`n=== Status Creation Completed ===" -ForegroundColor Green
