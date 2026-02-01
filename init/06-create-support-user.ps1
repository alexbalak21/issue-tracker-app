param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[6] Creating SUPPORT user..." -ForegroundColor Cyan

$headers = @{ Authorization = "Bearer $Global:AdminToken" }

$body = @{
    name = "Alyx Vance"
    email = "alyx.vance@example.com"
    password = "password1234"
    roleIds = @($Global:SupportRoleId)
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "$BaseUrl/api/admin/users" `
    -Headers $headers `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

Write-Host "SUPPORT user created successfully." -ForegroundColor Green
