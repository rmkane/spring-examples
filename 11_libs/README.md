# Example 11: Hipparchus Mathematical Library Demo

This project demonstrates the capabilities of the Hipparchus mathematical library through a standalone Java application. Unlike the other Spring Boot examples in this repository, this is a pure Java application focused on mathematical computations.

## Overview

Hipparchus is a comprehensive mathematical library for Java that provides:

- Linear algebra operations
- Statistical analysis
- Function analysis and optimization
- 3D geometry operations
- Numerical methods

## Features Demonstrated

### 1. Linear Algebra

- Matrix operations (transpose, multiplication)
- Vector operations (dot product, cross product)
- Eigenvalue decomposition
- Matrix-vector operations

### 2. Statistical Analysis

- Descriptive statistics (mean, standard deviation, variance)
- Percentiles and quartiles
- Data analysis capabilities

### 3. Function Analysis

- Polynomial function evaluation
- Root finding using Brent's method
- Function analysis and solving

### 4. 3D Geometry

- 3D vector operations
- Cross and dot products
- Vector normalization
- Angle calculations

### 5. Optimization

- Univariate function optimization
- Minimum finding using Brent's method
- Optimization verification

## Dependencies

- **Hipparchus Core**: Core mathematical functions
- **Hipparchus Geometry**: 2D and 3D geometry operations
- **Hipparchus Optimization**: Optimization algorithms
- **Hipparchus Statistics**: Statistical analysis
- **Hipparchus Linear**: Linear algebra operations
- **Lombok**: Reduces boilerplate code

## Building and Running

### Using Maven

```bash
# Compile the project
mvn compile

# Run the application
mvn exec:java

# Package the application
mvn package

# Run the packaged JAR
java -jar target/hipparchus-demo.jar
```

### Using Make

```bash
# Compile and run
make run

# Package the application
make package

# Run the packaged JAR
make run-jar

# Clean build artifacts
make clean
```

## Project Structure

```none
11_libs/
├── pom.xml                                    # Maven configuration
├── Makefile                                   # Build automation
├── README.md                                  # This file
└── src/main/java/org/example/libs/
    └── HipparchusDemo.java                   # Main demonstration class
```

## Sample Output

The application will demonstrate various mathematical operations and display results such as:

- Matrix operations and transformations
- Statistical analysis of sample data
- Polynomial function evaluation and root finding
- 3D vector operations and geometric calculations
- Optimization results and verification

## Key Differences from Spring Boot Examples

1. **No Spring Framework**: This is a pure Java application
2. **Standalone Execution**: Runs as a simple Java application
3. **Mathematical Focus**: Demonstrates mathematical library capabilities
4. **Executable JAR**: Creates a self-contained executable JAR file
5. **No Web Components**: No REST APIs or web interfaces

## Use Cases

This example is useful for:

- Learning mathematical library capabilities
- Understanding numerical computation in Java
- Exploring optimization algorithms
- Working with linear algebra and statistics
- Developing mathematical applications

## Further Exploration

To extend this demo, you could:

- Add more complex mathematical functions
- Implement custom optimization problems
- Add data visualization capabilities
- Integrate with data sources for statistical analysis
- Create interactive mathematical tools
