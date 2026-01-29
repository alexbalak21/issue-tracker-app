# Base URL
$baseUrl = "http://localhost:8100"

Write-Host "=== Creating Role and User ===" -ForegroundColor Cyan

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
# 2. Create USER Role
# -----------------------------
Write-Host "`n[2] Creating USER role..."

$createRoleBody = @{
    name = "USER"
    description = "User role"
    permissionIds = @(1, 2)
} | ConvertTo-Json

$roleResponse = Invoke-RestMethod -Uri "$baseUrl/api/admin/roles" -Headers $adminHeaders -Method POST -Body $createRoleBody -ContentType "application/json"
Write-Host "Role Response:" ($roleResponse | ConvertTo-Json -Depth 5)

# -----------------------------
# 3. Create User
# -----------------------------
Write-Host "`n[3] Creating user..."

$createUserBody = @{
    name = "Gordon Freeman"
    email = "gordon.freeman@example.com"
    password = "password1234"
    roleIds = @(1, 2)
} | ConvertTo-Json

$userResponse = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Headers $adminHeaders -Method POST -Body $createUserBody -ContentType "application/json"
Write-Host "User Response:" ($userResponse | ConvertTo-Json -Depth 5)

Write-Host "`n=== Role and User Creation Completed ===" -ForegroundColor Green
