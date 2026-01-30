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
    @{ name = "Open";        type = "workflow" },
    @{ name = "In Progress"; type = "workflow" },
    @{ name = "Waiting";     type = "workflow" },
    @{ name = "On Hold";     type = "workflow" },
    @{ name = "Resolved";    type = "workflow" },
    @{ name = "Closed";      type = "workflow" },
    @{ name = "Canceled";    type = "workflow" }
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
