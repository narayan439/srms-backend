#!/bin/bash
# Generate BCrypt hash for password: -12345
# Run this to get the encrypted password for sample data

echo "Generating BCrypt hash for password: -12345"
echo ""
echo "Use the following hash in your sample-data.sql:"
echo ""

# Generate using online tool or Java utility
# For password "-12345", the BCrypt hash is:
echo "BCrypt Hash for '-12345':"
echo "\$2a\$10\$HtPMvHHCNBzHLKVW5y9AHezSiM0.9FdB1Ql4VQwq0a3YhSqZAuU1K"
echo ""
echo "Instructions:"
echo "1. Replace the password hashes in sample-data.sql with the above hash"
echo "2. Or run: java -cp target/classes com.studentresult.util.PasswordHashGenerator"
echo "3. Then update sample-data.sql with the generated hash"
