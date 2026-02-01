param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[5] Creating SUPPORT role..." -ForegroundColor Cyan

$headers = @{ Authorization = "Bearer $Global:AdminToken" }

# SUPPORT permissions
$permissionIds = @(4,5,6,31,32,41,42)

$body = @{
    name = "SUPPORT"
    description = "Support agent role"
    permissionIds = $permissionIds
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "$BaseUrl/api/admin/roles" `
    -Headers $headers `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

Set-Variable -Name SupportRoleId -Value $response.data.id -Scope Global

Write-Host "SUPPORT role created with ID: $Global:SupportRoleId" -ForegroundColor Green
