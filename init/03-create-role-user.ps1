param(
    [string]$BaseUrl = "http://localhost:8100"
)

Write-Host "[3] Creating USER role..."

$headers = @{ Authorization = "Bearer $Global:AdminToken" }

# USER permissions
$permissionIds = @(3,4,5,30,31,32,41,42)

$body = @{
    name = "USER"
    description = "User role"
    permissionIds = $permissionIds
} | ConvertTo-Json

$response = Invoke-RestMethod `
    -Uri "$BaseUrl/api/admin/roles" `
    -Headers $headers `
    -Method POST `
    -Body $body `
    -ContentType "application/json"

Set-Variable -Name UserRoleId -Value $response.data.id -Scope Global

Write-Host "USER role created with ID: $Global:UserRoleId"
