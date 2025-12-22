# Login Testing Script for Student Result Management System (PowerShell)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Login Testing Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$BASE_URL = "http://localhost:8080/api"

# Function to test login
function Test-Login {
    param(
        [string]$email,
        [string]$password,
        [string]$expectedRole
    )
    
    Write-Host "Testing: $email" -ForegroundColor Yellow
    
    $body = @{
        email = $email
        password = $password
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$BASE_URL/auth/login" `
        -Method POST `
        -Headers @{"Content-Type" = "application/json"} `
        -Body $body `
        -ErrorAction SilentlyContinue
    
    if ($response.StatusCode -eq 200) {
        $content = $response.Content | ConvertFrom-Json
        Write-Host "✓ Login successful" -ForegroundColor Green
        Write-Host "  User ID: $($content.data.userId)" -ForegroundColor Green
        Write-Host "  Role: $($content.data.role)" -ForegroundColor Green
        Write-Host "  Name: $($content.data.name)" -ForegroundColor Green
        Write-Host "  Redirect: $($content.data.redirectPath)" -ForegroundColor Green
    } else {
        Write-Host "✗ Login failed (HTTP $($response.StatusCode))" -ForegroundColor Red
    }
    
    Write-Host ""
}

Write-Host "Make sure the backend is running on http://localhost:8080" -ForegroundColor Yellow
Write-Host ""

Write-Host "Testing ADMIN Login..." -ForegroundColor Cyan
Test-Login "admin@srms.com" "admin123" "ADMIN"

Write-Host "Testing TEACHER Login..." -ForegroundColor Cyan
Test-Login "sharma@teacher.com" "password123" "TEACHER"

Write-Host "Testing STUDENT Login..." -ForegroundColor Cyan
Test-Login "raj.kumar@student.com" "password123" "STUDENT"

Write-Host "Testing Invalid Login..." -ForegroundColor Cyan
Test-Login "invalid@email.com" "wrongpassword" "NONE"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Testing Complete!" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
