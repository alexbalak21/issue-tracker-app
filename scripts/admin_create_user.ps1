# -----------------------------
# 1. LOGIN AS ADMIN
# -----------------------------
$loginResponse = Invoke-RestMethod `
  -Uri "http://localhost:8100/api/auth/login" `
  -Method POST `
  -ContentType "application/json" `
  -Body '{
      "email": "admin@example.com",
      "password": "admin123"
  }'

$adminAccessToken = $loginResponse.data.access_token

Write-Host "Admin Access Token:" $adminAccessToken
Write-Host "-----------------------------------"


# -----------------------------
# 2. LIST USERS
# -----------------------------
Write-Host "`nListing users..."
$users = Invoke-RestMethod `
  -Uri "http://localhost:8100/api/admin/users" `
  -Method GET `
  -Headers @{ 
      "Authorization" = "Bearer $adminAccessToken"
  }

$users


# -----------------------------
# 3. CREATE USER
# -----------------------------
$createUserBody = '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "roleIds": [1]
}'

Write-Host "`nCreating user..."
$createUserResponse = Invoke-RestMethod `
  -Uri "http://localhost:8100/api/admin/users" `
  -Method POST `
  -Headers @{ 
      "Authorization" = "Bearer $adminAccessToken"
      "Content-Type"  = "application/json"
  } `
  -Body $createUserBody

Write-Host "`nUser created:"
$createUserResponse
