param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[1] Logging in as admin..."

$body = @{
    email = "admin@example.com"
    password = "admin123"
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "$BaseUrl/api/auth/login" `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

$token = $response.data.access_token

# Export token to global scope
Set-Variable -Name AdminToken -Value $token -Scope Global

Write-Host "Admin token acquired."
