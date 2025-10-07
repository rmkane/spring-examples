#!/bin/bash

# Script to sync Maven plugin versions from Spring Boot BOM
# Uses dynamic plugin discovery with regex pattern matching

set -e

SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(dirname "$SCRIPT_DIR")

# Configuration
FOO_DEPENDENCIES_POM="$PROJECT_ROOT/01_pom/foo-dependencies/pom.xml"
FOO_STARTER_PARENT_POM="$PROJECT_ROOT/01_pom/foo-starter-parent/pom.xml"

echo "🔄 Spring Boot Plugin Version Sync Tool"
echo "================================================"

# Function to get Spring Boot version from foo-dependencies/pom.xml
get_spring_boot_version() {
    local dependencies_pom="$FOO_DEPENDENCIES_POM"

    if [ ! -f "$dependencies_pom" ]; then
        echo "❌ Error: $dependencies_pom not found" >&2
        exit 1
    fi

    local version
    version=$(grep -o '<spring-boot\.version>[^<]*</spring-boot\.version>' "$dependencies_pom" | sed 's/<spring-boot\.version>\(.*\)<\/spring-boot\.version>/\1/')

    if [ -z "$version" ]; then
        echo "❌ Error: Could not find <spring-boot.version> in $dependencies_pom" >&2
        exit 1
    fi

    echo "$version"
}

# Function to download Spring Boot BOM if not cached
download_spring_boot_bom() {
    local version="$1"
    local bom_path="$HOME/.m2/repository/org/springframework/boot/spring-boot-dependencies/$version/spring-boot-dependencies-$version.pom"

    if [ ! -f "$bom_path" ]; then
        echo "📥 Downloading Spring Boot BOM $version..." >&2
        if ! mvn dependency:get -Dartifact=org.springframework.boot:spring-boot-dependencies:"$version":pom -q; then
            echo "❌ Failed to download Spring Boot BOM $version" >&2
            exit 1
        fi
    else
        echo "✅ Spring Boot BOM $version found in local repository" >&2
    fi

    # Verify the file exists after download
    if [ ! -f "$bom_path" ]; then
        echo "❌ Error: Spring Boot BOM not found at $bom_path" >&2
        exit 1
    fi

    echo "$bom_path"
}

# Function to create plugin version lookup from Spring Boot BOM
create_plugin_lookup() {
    local bom_path="$1"
    local lookup_file
    lookup_file=$(mktemp)

    echo "🔍 Creating plugin version lookup from Spring Boot BOM..." >&2

    # Extract all plugin version properties using regex pattern
    # Use a different approach to avoid subshell issues
    local temp_grep
    temp_grep=$(mktemp)
    grep -E "maven-[a-zA-Z0-9-]+-plugin\.version|spring-[a-zA-Z0-9-]+-plugin\.version" "$bom_path" > "$temp_grep"

    while IFS= read -r line; do
        # Extract plugin name and version using simpler approach
        local plugin_name
        plugin_name=$(echo "$line" | sed -n 's/.*<\([^/>]*\)\.version>.*/\1/p')
        local version
        version=$(echo "$line" | sed -n 's/.*\.version>\([^<]*\)<.*/\1/p')

        # Only process if it matches our plugin pattern
        if echo "$plugin_name" | grep -qE "^(maven|spring)-[a-zA-Z0-9-]+-plugin$"; then
            if [ -n "$plugin_name" ] && [ -n "$version" ]; then
                echo "${plugin_name}=${version}" >> "$lookup_file"
                echo "  ✅ Found ${plugin_name}: ${version}" >&2
            fi
        fi
    done < "$temp_grep"

    rm "$temp_grep"

    if [ ! -s "$lookup_file" ]; then
        echo "  No plugin versions found in Spring Boot BOM" >&2
        echo "  This might indicate the BOM structure has changed or the plugins are not listed in properties." >&2
    fi

    echo "$lookup_file"
}

# Function to get version from lookup table
get_version_from_lookup() {
    local lookup_file="$1"
    local plugin="$2"
    grep "^${plugin}=" "$lookup_file" | cut -d'=' -f2
}

# Function to update plugin versions in foo-starter-parent using lookup table
update_plugin_versions_with_lookup() {
    local starter_parent_pom="$FOO_STARTER_PARENT_POM"
    local lookup_file="$1"

    if [ ! -f "$starter_parent_pom" ]; then
        echo "❌ Error: $starter_parent_pom not found" >&2
        exit 1
    fi

    echo "🔄 Updating plugin versions in foo-starter-parent using lookup table..." >&2

    # Create a backup
    cp "$starter_parent_pom" "$starter_parent_pom.backup"
    echo "📋 Backup created: $starter_parent_pom.backup" >&2

    local temp_pom
    temp_pom=$(mktemp)
    local updated_count=0

    # Process the POM line by line
    while IFS= read -r line; do
        # Check if this line contains a plugin version property matching our pattern
        if echo "$line" | grep -qE "maven-[a-zA-Z0-9-]+-plugin\.version|spring-[a-zA-Z0-9-]+-plugin\.version"; then
            # Extract plugin name using simpler approach
            local plugin_name
            plugin_name=$(echo "$line" | sed -n 's/.*<\([^/>]*\)\.version>.*/\1/p')

            # Only process if it matches our plugin pattern
            if echo "$plugin_name" | grep -qE "^(maven|spring)-[a-zA-Z0-9-]+-plugin$"; then
                # Check if this plugin exists in our lookup table
                if grep -q "^${plugin_name}=" "$lookup_file"; then
                    # Get version from lookup
                    local new_version
                    new_version=$(get_version_from_lookup "$lookup_file" "$plugin_name")
                    if [ -n "$new_version" ]; then
                        # Extract current version from the line
                        local current_version
                        current_version=$(echo "$line" | sed -n 's/.*\.version>\([^<]*\)<.*/\1/p')

                        # Only update if versions are different
                        if [[ "$current_version" != "$new_version" ]]; then
                            echo "        <${plugin_name}.version>${new_version}</${plugin_name}.version>" >> "$temp_pom"
                            echo "  🔄 Updated ${plugin_name}: ${current_version} → ${new_version}" >&2
                            updated_count=$((updated_count + 1))
                        else
                            echo "$line" >> "$temp_pom"
                            echo "  ✅ ${plugin_name}: already up-to-date (${current_version})" >&2
                        fi
                    else
                        # Keep original if not found in lookup
                        echo "$line" >> "$temp_pom"
                        echo "  ⚠️  Keeping original ${plugin_name} (not found in Spring Boot BOM)" >&2
                    fi
                else
                    # Not a plugin found in Spring Boot BOM, keep original
                    echo "$line" >> "$temp_pom"
                fi
            else
                # Not a plugin version property, keep original
                echo "$line" >> "$temp_pom"
            fi
        else
            # Not a plugin version line, keep original
            echo "$line" >> "$temp_pom"
        fi
    done < "$starter_parent_pom"

    # Replace the original file with the updated temporary file
    mv "$temp_pom" "$starter_parent_pom"

    echo "✅ Plugin versions updated successfully (${updated_count} plugins updated)" >&2
    echo "📊 Changes made:" >&2
    echo "==================" >&2
    diff -u "$starter_parent_pom.backup" "$starter_parent_pom" >&2 || true # Show diff, ignore exit code 1 for differences
    echo "" >&2
    echo "💡 Tip: Run 'diff -u $starter_parent_pom.backup $starter_parent_pom' to see all changes" >&2
}

# --- Main Execution ---
main() {
    echo "🔍 Extracting Spring Boot version..." >&2
    SPRING_BOOT_VERSION=$(get_spring_boot_version)
    echo "✅ Found Spring Boot version: $SPRING_BOOT_VERSION" >&2

    # Download BOM if not present
    BOM_PATH=$(download_spring_boot_bom "$SPRING_BOOT_VERSION")

    # Create lookup table
    LOOKUP_FILE=$(create_plugin_lookup "$BOM_PATH")

    # Show what we found
    echo "✅ Plugin versions found in Spring Boot BOM:" >&2
    if [ -s "$LOOKUP_FILE" ]; then
        while IFS='=' read -r plugin version; do
            echo "  ${plugin}: ${version}" >&2
        done < "$LOOKUP_FILE"
    else
        echo "  No plugin versions found in Spring Boot BOM" >&2
        echo "  This might indicate the BOM structure has changed or the plugins are not listed in properties." >&2
    fi
    echo "" >&2

    # Update foo-starter-parent using lookup
    update_plugin_versions_with_lookup "$LOOKUP_FILE"

    # Clean up temporary file
    rm "$LOOKUP_FILE"

    echo "" >&2
    echo "🎉 Plugin version sync completed successfully!" >&2
    echo "💡 Next steps:" >&2
    echo "  1. Review the changes in $FOO_STARTER_PARENT_POM" >&2
    echo "  2. Test the build: mvn clean compile -pl 01_pom" >&2
    echo "  3. If everything works, remove the backup: rm $FOO_STARTER_PARENT_POM.backup" >&2
    echo "  4. If there are issues, restore: mv $FOO_STARTER_PARENT_POM.backup $FOO_STARTER_PARENT_POM" >&2
}

main "$@"
