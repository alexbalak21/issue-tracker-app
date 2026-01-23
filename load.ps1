# Load .env file into persistent Windows environment variables (User scope)

Get-Content .env | ForEach-Object {
    if ($_ -match "^\s*#") { return }   # skip comments
    if ($_ -match "^\s*$") { return }   # skip empty lines

    $name, $value = $_ -split "=", 2

    # Trim whitespace
    $name = $name.Trim()
    $value = $value.Trim()

    # Persist the variable for the current user
    [Environment]::SetEnvironmentVariable($name, $value, "User")

    Write-Host "Set $name=$value"
}

Write-Host "All .env variables saved permanently (User environment)."
Write-Host "Restart your terminal or PC to apply them globally."
