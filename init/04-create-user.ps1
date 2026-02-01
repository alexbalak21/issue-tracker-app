param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[4] Creating USER account..."

$headers = @{ Authorization = "Bearer $Global:AdminToken" }

$body = @{
    name = "Gordon Freeman"
    email = "gordon.freeman@example.com"
    password = "password1234"
    roleIds = @($Global:UserRoleId)
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "$BaseUrl/api/admin/users" `
    -Headers $headers `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

Write-Host "User created successfully."
