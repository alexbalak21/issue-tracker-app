# Base URL
$baseUrl = "http://localhost:8100"

Write-Host "=== Testing API ===" -ForegroundColor Cyan

# -----------------------------
# 1. Register User
# -----------------------------
Write-Host "`n[1] Registering user..."

$registerBody = @{
    name = "test_user"
    email = "testuser@example.com"
    password = "password123"
} | ConvertTo-Json

try {
    $registerResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/register" -Method POST -Body $registerBody -ContentType "application/json"
    Write-Host "Register Response:" ($registerResponse | ConvertTo-Json -Depth 5)
} catch {
    Write-Host "User may already exist. Continuing..." -ForegroundColor Yellow
}

# -----------------------------
# 2. Login
# -----------------------------
Write-Host "`n[2] Logging in..."

$loginBody = @{
    email = "testuser@example.com"
    password = "password123"
} | ConvertTo-Json

$loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"

$accessToken = $loginResponse.data.access_token
$refreshToken = $loginResponse.data.refresh_token

Write-Host "Access Token:" $accessToken
Write-Host "Refresh Token:" $refreshToken

# -----------------------------
# 3. Get Current User
# -----------------------------
Write-Host "`n[3] Fetching current user..."

$headers = @{ Authorization = "Bearer $accessToken" }

$userResponse = Invoke-RestMethod -Uri "$baseUrl/api/user" -Headers $headers -Method GET
Write-Host "User Response:" ($userResponse | ConvertTo-Json -Depth 5)

# -----------------------------
# 4. Refresh Token
# -----------------------------
Write-Host "`n[4] Refreshing token..."

$refreshBody = @{
    refresh_token = $refreshToken
} | ConvertTo-Json

$refreshResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/refresh" -Method POST -Body $refreshBody -ContentType "application/json"

$newAccessToken = $refreshResponse.data.access_token
$newRefreshToken = $refreshResponse.data.refresh_token

Write-Host "New Access Token:" $newAccessToken
Write-Host "New Refresh Token:" $newRefreshToken

# -----------------------------
# 5. Get Current User with new token
# -----------------------------
Write-Host "`n[5] Fetching user with refreshed token..."

$newHeaders = @{ Authorization = "Bearer $newAccessToken" }

$newUserResponse = Invoke-RestMethod -Uri "$baseUrl/api/user" -Headers $newHeaders -Method GET
Write-Host "User Response (Refreshed Token):" ($newUserResponse | ConvertTo-Json -Depth 5)

Write-Host "`n=== API Test Completed ===" -ForegroundColor Green
