# Maven Dependency Matrix Visualizer

A comprehensive CLI tool for analyzing and visualizing Maven project dependencies in an interactive web interface.

## Files

- **`cli.py`** - Main CLI script with analyze and generate commands
- **`visualizer.html`** - Interactive web-based visualization tool
- **`sample-matrix.json`** - Sample dependency matrix for testing

## Usage

### 1. Analyze POM Files

```bash
# Analyze current directory (recursive search)
python3 cli.py analyze

# Use specific glob patterns
python3 cli.py analyze --pattern "**/pom.xml"                    # Recursive search (default)
python3 cli.py analyze --pattern "*/pom.xml"                     # One level deep
python3 cli.py analyze --pattern "pom.xml"                       # Current directory only
python3 cli.py analyze --pattern "/path/to/project/**/pom.xml"   # Specific project

# Custom output location
python3 cli.py analyze --pattern "../**/pom.xml" --output-dir results --output deps.json

# Verbose mode to see POM parsing details
python3 cli.py analyze --pattern "../**/pom.xml" --verbose

# Validate POM files for common issues
python3 cli.py analyze --pattern "../**/pom.xml" --validate
```

### 2. Generate Sample Data

```bash
# Generate sample matrix from current project
python3 cli.py generate

# Generate with specific pattern
python3 cli.py generate --pattern "*/pom.xml"

# Custom output location
python3 cli.py generate --pattern "../**/pom.xml" --output-dir samples --output test-matrix.json
```

### 3. Visualize the Matrix

1. Open `visualizer.html` in your web browser
2. Upload the generated JSON file (e.g., `matrix.json`)
3. Use the interactive filters to explore dependencies:
   - **Group Filter**: Filter by dependency group (e.g., `org.springframework.boot`)
   - **Artifact Filter**: Filter by specific artifacts
   - **Version Filter**: Filter by version patterns
   - **Project Filter**: Search for specific projects

## Features

### CLI Tool (`cli.py`)

- ✅ **Multi-dimensional Analysis**: Group → Artifact → Version → Projects
- ✅ **Property Resolution**: Resolves Maven property placeholders
- ✅ **Intelligent Sorting**: Semantic version sorting with special handling
- ✅ **Flexible Output**: Customizable output directory and filename
- ✅ **Glob Pattern Support**: Flexible file discovery using glob patterns
- ✅ **Verbose Mode**: Detailed POM parsing information
- ✅ **POM Validation**: Built-in validation with comprehensive error reporting
- ✅ **Robust Error Handling**: Graceful handling of malformed XML and file issues

### Validation Checks

The validation system checks for:

- **Missing Coordinates**: `groupId`, `artifactId`, or `version` (for non-parent POMs)
- **Unmanaged Dependencies**: Dependencies without versions and no parent POM
- **Circular Dependencies**: Self-referencing dependencies
- **Duplicate Dependencies**: Multiple declarations of the same dependency
- **Unresolved Properties**: Property placeholders that cannot be resolved
- **XML Parsing Errors**: Malformed XML, file access issues, invalid POM structure

### Visualizer (`visualizer.html`)

- ✅ **Interactive Filters**: Multi-level filtering system
- ✅ **Responsive Design**: Works on desktop and mobile
- ✅ **Visual Hierarchy**: Clear 4D structure representation
- ✅ **Statistics Dashboard**: Overview of dependency metrics
- ✅ **Drag & Drop**: Easy file upload interface
- ✅ **Real-time Search**: Instant filtering as you type

## Visualization Approach

The 4D matrix (Group → Artifact → Version → Projects) is visualized using:

1. **Hierarchical Cards**: Each dependency group is a card containing artifacts
2. **Version Badges**: Color-coded version indicators:
   - 🟢 **Inherited**: Dependencies without explicit versions
   - 🟡 **Property**: Maven property placeholders
   - 🔵 **Semantic**: Standard version numbers
3. **Project Tags**: Projects using each dependency version
4. **Interactive Filters**: Drill down through the dimensions

## Example Output Structure

```json
{
  "org.springframework.boot": {
    "spring-boot-starter-web": {
      "inherited": ["web", "rest", "security"],
      "3.5.5": ["common"]
    }
  }
}
```

## Command Line Options

```bash
python3 cli.py [COMMAND] [OPTIONS]

Commands:
  analyze                     Analyze POM files and generate dependency matrix
  generate                    Generate sample matrix data for testing

Global Options:
  --pattern, -p PATTERN       Glob pattern for finding POM files (default: **/pom.xml)
  --output-dir, -od OUTPUT_DIR Output directory for generated files
  --output, -o OUTPUT         Output JSON file name
  --verbose, -v               Enable verbose mode
  --help, -h                  Show help message

Analyze Command Options:
  --validate                  Validate POM files and report issues
```

## Browser Compatibility

The visualizer works in all modern browsers:

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## Sample Data

Run `python3 cli.py generate` to create a sample matrix from the current project for testing the visualizer.
