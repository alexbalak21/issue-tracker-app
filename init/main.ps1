$BaseUrl = "http://localhost:8100"

Write-Host "=== Running Full Bootstrap ===" -ForegroundColor Cyan

.\01-login-admin.ps1 -BaseUrl $BaseUrl
.\02-create-permissions.ps1 -BaseUrl $BaseUrl
.\03-create-role-user.ps1 -BaseUrl $BaseUrl
.\04-create-user.ps1 -BaseUrl $BaseUrl
.\05-create-role-support.ps1 -BaseUrl $BaseUrl
.\06-create-support-user.ps1 -BaseUrl $BaseUrl

Write-Host "=== Bootstrap Completed ===" -ForegroundColor Green
