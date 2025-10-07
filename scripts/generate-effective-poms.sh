#!/bin/bash

# Script to generate effective POMs for all POMs in 01_pom/
# Saves them as target/effective-pom.xml in each respective directory

set -e

SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

echo "🔍 Generating effective POMs for all modules in 01_pom/..."

# Change to the 01_pom directory
cd "$PROJECT_ROOT/01_pom"

# Function to generate effective POM for a given module
generate_effective_pom() {
    local module_name="$1"
    local module_path="$2"

    echo "📄 Generating effective POM for: $module_name"

    # Change to the module directory
    cd "$module_path"

    # Generate effective POM and save to target/effective-pom.xml
    mvn help:effective-pom -Doutput=target/effective-pom.xml -q

    # Check if the file was created successfully
    if [ -f "target/effective-pom.xml" ]; then
        echo "✅ Generated: $module_path/target/effective-pom.xml"
    else
        echo "❌ Failed to generate: $module_path/target/effective-pom.xml"
    fi

    # Go back to 01_pom directory
    cd ..
}

# Generate effective POM for the root foo-pom
echo "📄 Generating effective POM for: foo-pom (root)"
mvn help:effective-pom -Doutput=target/effective-pom.xml -q
if [ -f "target/effective-pom.xml" ]; then
    echo "✅ Generated: 01_pom/target/effective-pom.xml"
else
    echo "❌ Failed to generate: 01_pom/target/effective-pom.xml"
fi

# Generate effective POMs for all sub-modules
generate_effective_pom "foo-dependencies" "foo-dependencies"
generate_effective_pom "foo-starter-parent" "foo-starter-parent"

echo ""
echo "🎉 Effective POM generation complete!"
echo ""
echo "Generated files:"
echo "  📄 $PROJECT_ROOT/01_pom/target/effective-pom.xml"
echo "  📄 $PROJECT_ROOT/01_pom/foo-dependencies/target/effective-pom.xml"
echo "  📄 $PROJECT_ROOT/01_pom/foo-starter-parent/target/effective-pom.xml"
echo ""
echo "You can now examine these files to see the resolved properties and configurations."
