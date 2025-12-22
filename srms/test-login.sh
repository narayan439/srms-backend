#!/bin/bash
# Login Testing Script for Student Result Management System

echo "========================================"
echo "Login Testing Script"
echo "========================================"
echo ""

BASE_URL="http://localhost:8080/api"

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test login
test_login() {
    local email=$1
    local password=$2
    local expected_role=$3
    
    echo -e "${YELLOW}Testing: $email${NC}"
    
    response=$(curl -s -X POST "$BASE_URL/auth/login" \
      -H "Content-Type: application/json" \
      -d "{\"email\":\"$email\",\"password\":\"$password\"}")
    
    echo "Response: $response"
    
    if echo "$response" | grep -q "\"success\":true"; then
        echo -e "${GREEN}✓ Login successful${NC}"
        
        # Extract redirect path
        redirect=$(echo "$response" | grep -o '"redirectPath":"[^"]*' | cut -d'"' -f4)
        echo -e "${GREEN}  Redirect: $redirect${NC}"
    else
        echo -e "${RED}✗ Login failed${NC}"
    fi
    
    echo ""
}

echo "Testing ADMIN Login..."
test_login "admin@srms.com" "admin123" "ADMIN"

echo "Testing TEACHER Login..."
test_login "sharma@teacher.com" "password123" "TEACHER"

echo "Testing STUDENT Login..."
test_login "raj.kumar@student.com" "password123" "STUDENT"

echo "Testing Invalid Login..."
test_login "invalid@email.com" "wrongpassword" "NONE"

echo "========================================"
echo "Testing Complete!"
echo "========================================"
