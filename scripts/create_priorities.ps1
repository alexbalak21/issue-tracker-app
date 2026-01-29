# Base URL
$baseUrl = "http://localhost:8100"

Write-Host "=== Creating Priorities ===" -ForegroundColor Cyan

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
# 2. Create Priorities
# -----------------------------
$priorities = @(
    @{ name = "Low"; level = 1; description = "Minor inconvenience, cosmetic issues" },
    @{ name = "Medium"; level = 2; description = "Normal issues affecting a single user" },
    @{ name = "High"; level = 3; description = "Major issue affecting work or multiple users" },
    @{ name = "Critical"; level = 4; description = "System down, security issue, production outage" }
)

$index = 1
foreach ($priority in $priorities) {
    Write-Host "`n[$index] Creating priority: $($priority.name)..."
    $priorityBody = $priority | ConvertTo-Json
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/admin/priorities" -Headers $adminHeaders -Method POST -Body $priorityBody -ContentType "application/json"
        Write-Host "Created:" ($response | ConvertTo-Json -Depth 5)
    } catch {
        Write-Host "Failed to create $($priority.name): $($_.Exception.Message)" -ForegroundColor Yellow
    }
    $index++
}

Write-Host "`n=== Priority Creation Completed ===" -ForegroundColor Green
