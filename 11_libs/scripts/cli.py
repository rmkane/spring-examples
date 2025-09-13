#!/usr/bin/env python3
"""
Maven POM CLI - Comprehensive tool for analyzing and generating Maven project
dependencies.

This tool provides commands for analyzing POM files, generating dependency matrices,
and creating sample data for testing visualizations.
"""

import argparse
import glob
import json
import os
import re
import sys
import xml.etree.ElementTree as ET
from collections import defaultdict
from dataclasses import dataclass

# =============================================================================
# Type Definitions and Utilities
# =============================================================================


@dataclass
class GavInfo:
    """Maven GAV (Group, Artifact, Version) information."""

    group_id: str | None = None
    artifact_id: str | None = None
    version: str | None = None


@dataclass
class DependencyInfo:
    """Dependency information with GAV and scope."""

    gav: GavInfo
    scope: str | None = None


@dataclass
class ParentInfo:
    """Parent POM information."""

    gav: GavInfo
    relative_path: str | None = None


@dataclass
class PomInfo:
    """Complete POM information."""

    gav: GavInfo
    packaging: str | None = None
    name: str | None = None
    description: str | None = None
    parent: ParentInfo | None = None
    dependencies: list[DependencyInfo] | None = None
    managed_dependencies: list[DependencyInfo] | None = None
    properties: dict[str, str | int | float] | None = None

    def __post_init__(self):
        if self.dependencies is None:
            self.dependencies = []
        if self.managed_dependencies is None:
            self.managed_dependencies = []
        if self.properties is None:
            self.properties = {}


# =============================================================================
# XML Parsing Utilities
# =============================================================================


def _extract_namespace(root: ET.Element) -> str:
    """Extract namespace from XML root element."""
    tag = root.tag
    if "}" in tag:
        return tag.split("}")[0] + "}"
    return ""


def _get_text(element: ET.Element | None, namespace: str = "") -> str | None:
    """Get text content from XML element."""
    if element is not None and element.text:
        return element.text.strip()
    return None


def _parse_dependencies_from_element(
    element: ET.Element | None, namespace: str
) -> list[DependencyInfo]:
    """Parse dependencies from a dependencies or dependencyManagement element."""
    if element is None:
        return []

    dependencies = []
    for dep_elem in element.findall(f"{namespace}dependency"):
        group_id = _get_text(dep_elem.find(f"{namespace}groupId"), namespace)
        artifact_id = _get_text(dep_elem.find(f"{namespace}artifactId"), namespace)
        version = _get_text(dep_elem.find(f"{namespace}version"), namespace)
        scope = _get_text(dep_elem.find(f"{namespace}scope"), namespace)

        gav = GavInfo(group_id=group_id, artifact_id=artifact_id, version=version)
        dependency = DependencyInfo(gav=gav, scope=scope)
        dependencies.append(dependency)

    return dependencies


# =============================================================================
# POM Parsing Logic
# =============================================================================


def _parse_project_info(root: ET.Element, namespace: str) -> tuple:
    """Parse basic project information from POM root."""
    group_id = _get_text(root.find(f"{namespace}groupId"), namespace)
    artifact_id = _get_text(root.find(f"{namespace}artifactId"), namespace)
    version = _get_text(root.find(f"{namespace}version"), namespace)
    packaging = _get_text(root.find(f"{namespace}packaging"), namespace) or "jar"
    name = _get_text(root.find(f"{namespace}name"), namespace)
    description = _get_text(root.find(f"{namespace}description"), namespace)

    return group_id, artifact_id, version, packaging, name, description


def _parse_parent(root: ET.Element, namespace: str) -> ParentInfo | None:
    """Parse parent POM information."""
    parent_elem = root.find(f"{namespace}parent")
    if parent_elem is None:
        return None

    group_id = _get_text(parent_elem.find(f"{namespace}groupId"), namespace)
    artifact_id = _get_text(parent_elem.find(f"{namespace}artifactId"), namespace)
    version = _get_text(parent_elem.find(f"{namespace}version"), namespace)
    relative_path = _get_text(parent_elem.find(f"{namespace}relativePath"), namespace)

    gav = GavInfo(group_id=group_id, artifact_id=artifact_id, version=version)
    return ParentInfo(gav=gav, relative_path=relative_path)


def _parse_dependencies(root: ET.Element, namespace: str) -> list[DependencyInfo]:
    """Parse dependencies section."""
    deps_elem = root.find(f"{namespace}dependencies")
    return _parse_dependencies_from_element(deps_elem, namespace)


def _parse_dependency_management(
    root: ET.Element, namespace: str
) -> list[DependencyInfo]:
    """Parse dependencyManagement section."""
    dep_mgmt_elem = root.find(f"{namespace}dependencyManagement")
    if dep_mgmt_elem is None:
        return []

    deps_elem = dep_mgmt_elem.find(f"{namespace}dependencies")
    return _parse_dependencies_from_element(deps_elem, namespace)


def _parse_properties(root: ET.Element, namespace: str) -> dict[str, str | int | float]:
    """Parse properties section."""
    properties: dict[str, str | int | float] = {}
    props_elem = root.find(f"{namespace}properties")
    if props_elem is None:
        return properties

    for prop in props_elem:
        tag_name = prop.tag
        if namespace in tag_name:
            tag_name = tag_name.replace(namespace, "")
        if prop.text:
            # Try to convert to number if possible
            try:
                if "." in prop.text:
                    properties[tag_name] = float(prop.text)
                else:
                    properties[tag_name] = int(prop.text)
            except ValueError:
                properties[tag_name] = prop.text

    return properties


def validate_pom(pom: PomInfo, file_path: str) -> list[str]:
    """
    Validate a POM file and return list of issues found.

    Args:
        pom: Parsed POM information
        file_path: Path to the POM file

    Returns:
        List of validation issues (empty if no issues)
    """
    issues = []

    # Check for missing groupId
    if not pom.gav.group_id:
        issues.append(f"Missing groupId in {file_path}")

    # Check for missing artifactId
    if not pom.gav.artifact_id:
        issues.append(f"Missing artifactId in {file_path}")

    # Check for missing version (only warn for non-parent POMs)
    if not pom.gav.version and pom.packaging != "pom":
        issues.append(f"Missing version in {file_path} (non-parent POM)")

    # Check for dependencies without versions (should be managed by parent)
    if pom.dependencies:
        for dep in pom.dependencies:
            if not dep.gav.version and not pom.parent:
                issues.append(
                    f"Dependency {dep.gav.group_id}:{dep.gav.artifact_id} "
                    f"has no version and no parent in {file_path}"
                )

    # Check for circular dependencies (basic check)
    if pom.gav.group_id and pom.gav.artifact_id and pom.dependencies:
        for dep in pom.dependencies:
            if (
                dep.gav.group_id == pom.gav.group_id
                and dep.gav.artifact_id == pom.gav.artifact_id
            ):
                issues.append(
                    f"Circular dependency detected: "
                    f"{dep.gav.group_id}:{dep.gav.artifact_id} in {file_path}"
                )

    # Check for duplicate dependencies
    seen_deps = set()
    if pom.dependencies:
        for dep in pom.dependencies:
            dep_key = f"{dep.gav.group_id}:{dep.gav.artifact_id}"
            if dep_key in seen_deps:
                issues.append(f"Duplicate dependency {dep_key} in {file_path}")
            seen_deps.add(dep_key)

    # Check for properties with unresolved placeholders
    if pom.properties:
        for prop_name, prop_value in pom.properties.items():
            if isinstance(prop_value, str) and "${" in prop_value:
                # Check if the placeholder can be resolved
                resolved = PropertyResolver.resolve(prop_value, pom.properties)
                if resolved == prop_value:  # No resolution occurred
                    issues.append(
                        f"Unresolvable property placeholder {prop_value} in {file_path}"
                    )

    return issues


def parse_pom(file_path: str) -> PomInfo:
    """
    Parse a pom.xml file and return a PomInfo dataclass.

    Args:
        file_path: Path to the pom.xml file

    Returns:
        PomInfo object with parsed data

    Raises:
        FileNotFoundError: If the file doesn't exist
        ET.ParseError: If the XML is malformed
        PermissionError: If file cannot be read
        Exception: For other parsing errors
    """
    try:
        # Check if file exists
        if not os.path.exists(file_path):
            raise FileNotFoundError(f"POM file not found: {file_path}")

        # Check if file is readable
        if not os.access(file_path, os.R_OK):
            raise PermissionError(f"Cannot read POM file: {file_path}")

        # Parse XML
        tree = ET.parse(file_path)
        root = tree.getroot()

        # Validate it's actually a POM file
        if root.tag.endswith("project"):
            namespace = _extract_namespace(root)
        else:
            raise ValueError(f"Not a valid Maven POM file: {file_path}")

        # Parse all sections
        group_id, artifact_id, version, packaging, name, description = (
            _parse_project_info(root, namespace)
        )
        parent = _parse_parent(root, namespace)
        dependencies = _parse_dependencies(root, namespace)
        managed_dependencies = _parse_dependency_management(root, namespace)
        properties = _parse_properties(root, namespace)

        # Create project GAV
        gav = GavInfo(group_id=group_id, artifact_id=artifact_id, version=version)

        return PomInfo(
            gav=gav,
            packaging=packaging,
            name=name,
            description=description,
            parent=parent,
            dependencies=dependencies,
            managed_dependencies=managed_dependencies,
            properties=properties,
        )

    except ET.ParseError as e:
        raise ET.ParseError(f"Malformed XML in {file_path}: {e}")
    except Exception as e:
        raise Exception(f"Failed to parse POM file {file_path}: {e}")


# =============================================================================
# Property Resolution Utilities
# =============================================================================


class PropertyResolver:
    """Utility class for resolving Maven property placeholders."""

    @staticmethod
    def resolve(value: str, properties: dict[str, str | int | float]) -> str:
        """
        Resolve property placeholders in a string.

        Args:
            value: String that may contain ${property} placeholders
            properties: Dictionary of properties to resolve against

        Returns:
            String with resolved properties
        """
        if not isinstance(value, str) or "${" not in value:
            return value

        result = value
        pattern = r"\$\{([^}]+)\}"

        for match in re.finditer(pattern, result):
            prop_name = match.group(1)
            if prop_name in properties:
                prop_value = str(properties[prop_name])
                result = result.replace(match.group(0), prop_value)

        return result


# =============================================================================
# Formatting and Output Utilities
# =============================================================================


class PomFormatter:
    """Utility class for formatting POM information."""

    @staticmethod
    def format_gav(gav: GavInfo) -> str:
        """Format GAV as a string."""
        parts = []
        if gav.group_id:
            parts.append(f"groupId: {gav.group_id}")
        if gav.artifact_id:
            parts.append(f"artifactId: {gav.artifact_id}")
        if gav.version:
            parts.append(f"version: {gav.version}")
        return ", ".join(parts)

    @staticmethod
    def format_dependency(dep: DependencyInfo) -> str:
        """Format dependency as a string."""
        gav_str = PomFormatter.format_gav(dep.gav)
        if dep.scope and dep.scope != "compile":
            return f"{gav_str} (scope: {dep.scope})"
        return gav_str


def print_pom(pom: PomInfo, file_path: str, verbose: bool = False) -> None:
    """Print POM information in a formatted way."""
    print(f"\n📄 {file_path}")
    print(f"   {PomFormatter.format_gav(pom.gav)}")

    if pom.packaging and pom.packaging != "jar":
        print(f"   packaging: {pom.packaging}")

    if pom.name:
        print(f"   name: {pom.name}")

    if pom.description:
        print(f"   description: {pom.description}")

    if pom.parent:
        print(f"   parent: {PomFormatter.format_gav(pom.parent.gav)}")

    if verbose:
        if pom.dependencies:
            print(f"   dependencies ({len(pom.dependencies)}):")
            for dep in pom.dependencies:
                print(f"     - {PomFormatter.format_dependency(dep)}")

        if pom.managed_dependencies:
            print(f"   managed dependencies ({len(pom.managed_dependencies)}):")
            for dep in pom.managed_dependencies:
                print(f"     - {PomFormatter.format_dependency(dep)}")

        if pom.properties:
            print(f"   properties ({len(pom.properties)}):")
            for key, value in pom.properties.items():
                print(f"     {key}: {value}")


# =============================================================================
# File Discovery and Processing
# =============================================================================


def find_pom_files(pattern: str = "**/pom.xml") -> list[str]:
    """
    Find POM files using glob patterns.

    Args:
        pattern: Glob pattern for finding POM files. Examples:
            - "**/pom.xml" (default): Find all pom.xml files recursively
            - "*/pom.xml": Find pom.xml files one level deep
            - "pom.xml": Find pom.xml in current directory only
            - "**/target/pom.xml": Find pom.xml files in target directories
            - "/path/to/project/**/pom.xml": Find pom.xml files in specific project

    Returns:
        List of absolute paths to POM files
    """
    # Handle absolute vs relative patterns
    if os.path.isabs(pattern):
        # Absolute pattern - use as-is
        pom_files = glob.glob(pattern, recursive=True)
    else:
        # Relative pattern - search from current directory
        pom_files = glob.glob(pattern, recursive=True)

    # Convert to absolute paths and sort for consistent output
    pom_files = [os.path.abspath(pom_file) for pom_file in pom_files]
    pom_files.sort()

    return pom_files


def process_pom(file_path: str, verbose: bool = False) -> PomInfo:
    """
    Process a single POM file: parse and print.

    Args:
        file_path: Path to the POM file
        verbose: Whether to show verbose output

    Returns:
        PomInfo object

    Raises:
        Exception: If parsing fails
    """
    try:
        pom = parse_pom(file_path)
        print_pom(pom, file_path, verbose)
        return pom
    except Exception as e:
        if verbose:
            print(f"❌ Failed to process {file_path}: {e}")
        raise


# =============================================================================
# Data Processing and Analysis
# =============================================================================


def make_group_dict():
    """Factory function for creating nested defaultdict structure."""
    return defaultdict(lambda: defaultdict(lambda: defaultdict(set)))


def _sort_version_keys(versions: list[str]) -> list[str]:
    """
    Sort version keys intelligently.

    Args:
        versions: List of version strings to sort

    Returns:
        Sorted list with 'inherited' first, then properties, then semantic versions
    """

    def version_key(version):
        if version == "inherited":
            return (0, version)
        elif version.startswith("${") and version.endswith("}"):
            return (1, version)
        else:
            # For semantic versions, try to parse and sort numerically
            try:
                # Simple semantic version parsing (major.minor.patch)
                parts = version.split(".")
                if len(parts) >= 3:
                    major = int(parts[0]) if parts[0].isdigit() else 0
                    minor = int(parts[1]) if parts[1].isdigit() else 0
                    patch = int(parts[2]) if parts[2].isdigit() else 0
                    return (
                        2,
                        -major,
                        -minor,
                        -patch,
                        version,
                    )  # Negative for descending order
            except (ValueError, IndexError):
                pass
            return (2, version)

    return sorted(versions, key=version_key)


def create_dict(pom_list: list[PomInfo]) -> dict:
    """
    Create a nested dictionary structure from a list of PomInfo objects.

    Args:
        pom_list: List of PomInfo objects

    Returns:
        Nested dictionary: {group_id: {artifact_id: {version: {project_names}}}}
    """
    matrix = make_group_dict()

    for pom in pom_list:
        project_name = pom.gav.artifact_id or "unknown"

        # Process regular dependencies
        if pom.dependencies:
            for dep in pom.dependencies:
                if not dep.gav.group_id or not dep.gav.artifact_id:
                    continue

                group_id = dep.gav.group_id
                artifact_id = dep.gav.artifact_id

                # Resolve version using properties
                if dep.gav.version:
                    resolved_version = PropertyResolver.resolve(
                        dep.gav.version, pom.properties or {}
                    )
                else:
                    resolved_version = "inherited"

                matrix[group_id][artifact_id][resolved_version].add(project_name)

        # Process managed dependencies
        if pom.managed_dependencies:
            for dep in pom.managed_dependencies:
                if not dep.gav.group_id or not dep.gav.artifact_id:
                    continue

                group_id = dep.gav.group_id
                artifact_id = dep.gav.artifact_id

                # Resolve version using properties
                if dep.gav.version:
                    resolved_version = PropertyResolver.resolve(
                        dep.gav.version, pom.properties or {}
                    )
                else:
                    resolved_version = "inherited"

                matrix[group_id][artifact_id][resolved_version].add(project_name)

    return matrix


# =============================================================================
# Serialization and I/O Utilities
# =============================================================================


def prepare_for_serialization(obj) -> dict | list:
    """
    Prepare data structure for JSON serialization.

    Args:
        obj: Object to prepare (defaultdict, set, or other)

    Returns:
        JSON-serializable object
    """
    if isinstance(obj, defaultdict):
        result = {}
        for key, value in obj.items():
            if isinstance(value, defaultdict):
                result[key] = prepare_for_serialization(value)
            elif isinstance(value, set):
                result[key] = sorted(list(value))
            else:
                result[key] = prepare_for_serialization(value)

        # Sort keys alphabetically
        return dict(sorted(result.items()))

    elif isinstance(obj, set):
        return sorted(list(obj))

    elif isinstance(obj, dict):
        result = {}
        for key, value in obj.items():
            if isinstance(value, defaultdict):
                result[key] = prepare_for_serialization(value)
            elif isinstance(value, set):
                result[key] = sorted(list(value))
            else:
                result[key] = prepare_for_serialization(value)

        # Sort keys alphabetically
        return dict(sorted(result.items()))

    else:
        return obj


def write_json(data: dict | list, file_path: str) -> None:
    """
    Write data to JSON file with proper error handling.

    Args:
        data: Data to write
        file_path: Path to output file
    """
    try:
        # Ensure output directory exists
        os.makedirs(os.path.dirname(file_path), exist_ok=True)

        # Prepare data for serialization
        serializable_data = prepare_for_serialization(data)

        # Write JSON file
        with open(file_path, "w") as f:
            json.dump(serializable_data, f, indent=2, default=str)

        print(f"Successfully wrote data to {file_path}")

    except Exception as e:
        print(f"Error writing to {file_path}: {e}")
        raise


# =============================================================================
# CLI Commands
# =============================================================================


def _process_pom_files(
    args, show_help_on_no_files: bool = True
) -> tuple[list[str], list[PomInfo]]:
    """
    Common POM file processing logic.

    Args:
        args: Command arguments
        show_help_on_no_files: Whether to show help when no files found

    Returns:
        Tuple of (pom_files, pom_list)
    """
    pom_files = find_pom_files(args.pattern)

    if not pom_files:
        print(f"⚠️  No POM files found matching pattern: {args.pattern}")
        if show_help_on_no_files:
            print("💡 Try different patterns like:")
            print("   - **/pom.xml (recursive search)")
            print("   - */pom.xml (one level deep)")
            print("   - pom.xml (current directory only)")
            print("   - /path/to/project/**/pom.xml (specific project)")
        return [], []

    print(f"📋 Found {len(pom_files)} POM files")

    if args.verbose:
        print("🔊 Verbose mode enabled")
        print("📄 POM files found:")
        for pom_file in pom_files:
            print(f"   - {pom_file}")

    # Process POM files with error handling
    pom_list = []
    failed_files = []

    for pom_file in pom_files:
        try:
            pom = process_pom(pom_file, args.verbose)
            pom_list.append(pom)
        except Exception as e:
            failed_files.append((pom_file, str(e)))
            if args.verbose:
                print(f"❌ Skipping {pom_file}: {e}")

    if failed_files:
        print(f"⚠️  Failed to parse {len(failed_files)} POM files:")
        for pom_file, error in failed_files:
            print(f"   - {pom_file}: {error}")

    if not pom_list:
        print("❌ No POM files could be parsed successfully")
        return [], []

    return pom_files, pom_list


def _validate_poms_if_requested(
    args, pom_files: list[str], pom_list: list[PomInfo]
) -> None:
    """Validate POM files if requested."""
    if not hasattr(args, "validate") or not args.validate:
        return

    print("🔍 Validating POM files...")
    all_issues = []
    for pom_file, pom in zip(pom_files, pom_list):
        issues = validate_pom(pom, pom_file)
        all_issues.extend(issues)

    if all_issues:
        print(f"⚠️  Found {len(all_issues)} validation issues:")
        for issue in all_issues:
            print(f"   - {issue}")
    else:
        print("✅ All POM files passed validation")


def _generate_and_save_matrix(
    args, pom_list: list[PomInfo], success_message: str
) -> None:
    """Generate matrix and save to file."""
    matrix = create_dict(pom_list)
    output_path = os.path.join(args.output_dir, args.output)
    write_json(matrix, output_path)
    print(success_message.format(output_path=output_path, groups=len(matrix)))


def cmd_analyze(args) -> None:
    """Analyze POM files and generate dependency matrix."""
    try:
        print(f"🔍 Searching with pattern: {args.pattern}")
        print(f"📁 Output directory: {args.output_dir}")
        print(f"📄 Output file: {args.output}")

        pom_files, pom_list = _process_pom_files(args)
        if not pom_list:
            sys.exit(1)

        _validate_poms_if_requested(args, pom_files, pom_list)
        _generate_and_save_matrix(
            args,
            pom_list,
            "✅ Successfully created dependency matrix with {groups} groups\n"
            "📄 Matrix saved to: {output_path}",
        )

    except Exception as e:
        print(f"❌ Error: {e}", file=sys.stderr)
        sys.exit(1)


def cmd_generate(args) -> None:
    """Generate fake sample matrix data for testing the visualizer."""
    try:
        print("🔍 Generating fake sample matrix data...")
        print(f"📁 Output directory: {args.output_dir}")
        print(f"📄 Output file: {args.output}")

        # Create minimal fake sample data
        sample_matrix = {
            "org.springframework.boot": {
                "spring-boot-starter-web": {
                    "3.5.5": ["web", "rest"],
                    "inherited": ["basic"],
                },
                "spring-boot-starter-security": {"3.5.5": ["security"]},
            },
            "org.projectlombok": {"lombok": {"inherited": ["security", "web"]}},
            "com.h2database": {"h2": {"inherited": ["security"]}},
            "commons-io": {"commons-io": {"2.20.0": ["common"], "inherited": ["web"]}},
        }

        # Write fake sample data to file
        output_path = os.path.join(args.output_dir, args.output)
        write_json(sample_matrix, output_path)

        print(f"✅ Sample matrix generated: {output_path}")
        print(f"📊 Contains {len(sample_matrix)} groups")
        print("💡 This is fake data for testing the visualizer")

    except Exception as e:
        print(f"❌ Error: {e}", file=sys.stderr)
        sys.exit(1)


# =============================================================================
# Main CLI Setup
# =============================================================================


def create_parser() -> argparse.ArgumentParser:
    """Create the main argument parser with subcommands."""
    parser = argparse.ArgumentParser(
        description="Maven POM CLI - Comprehensive tool for analyzing and generating "
        "Maven project dependencies.",
        epilog="This tool provides commands for analyzing POM files, "
        "generating dependency matrices, and creating sample data for testing "
        "visualizations.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )

    # Subcommands
    subparsers = parser.add_subparsers(dest="command", help="Available commands")

    # Analyze command
    analyze_parser = subparsers.add_parser(
        "analyze", help="Analyze POM files and generate dependency matrix"
    )
    analyze_parser.add_argument(
        "--pattern",
        "-p",
        default="**/pom.xml",
        help="Glob pattern for finding POM files (default: **/pom.xml). Examples: "
        "**/pom.xml (recursive), */pom.xml (one level), pom.xml (current dir only), "
        "/path/to/project/**/pom.xml (specific project)",
        type=str,
    )
    analyze_parser.add_argument(
        "--output-dir",
        "-od",
        default="output",
        help="Output directory for generated files (default: output)",
        type=str,
    )
    analyze_parser.add_argument(
        "--output",
        "-o",
        default="matrix.json",
        help="Output JSON file name (default: matrix.json)",
        type=str,
    )
    analyze_parser.add_argument(
        "--verbose",
        "-v",
        action="store_true",
        help="Enable verbose mode to show POM parsing output",
    )
    analyze_parser.add_argument(
        "--validate", action="store_true", help="Validate POM files and report issues"
    )

    # Generate command
    generate_parser = subparsers.add_parser(
        "generate", help="Generate fake sample matrix data for testing the visualizer"
    )
    generate_parser.add_argument(
        "--output-dir",
        "-od",
        default="output",
        help="Output directory for generated files (default: output)",
        type=str,
    )
    generate_parser.add_argument(
        "--output",
        "-o",
        default="sample-matrix.json",
        help="Output JSON file name (default: sample-matrix.json)",
        type=str,
    )

    return parser


def main() -> None:
    """Main entry point."""
    parser = create_parser()
    args = parser.parse_args()

    # If no command specified, default to analyze
    if not args.command:
        args.command = "analyze"

    # Execute the appropriate command
    if args.command == "analyze":
        cmd_analyze(args)
    elif args.command == "generate":
        cmd_generate(args)
    else:
        parser.print_help()
        sys.exit(1)


if __name__ == "__main__":
    main()
