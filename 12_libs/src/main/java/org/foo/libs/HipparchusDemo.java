package org.foo.libs;

import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.geometry.euclidean.twod.Vector2D;
import org.hipparchus.stat.descriptive.DescriptiveStatistics;
import org.hipparchus.stat.descriptive.rank.Percentile;
import org.hipparchus.util.FastMath;

import java.util.Arrays;

/**
 * Demonstration of Hipparchus mathematical library functionality.
 * This class showcases various mathematical operations including:
 * - Statistical analysis
 * - 3D geometry operations
 * - Mathematical functions and utilities
 */
public class HipparchusDemo {

    public static void main(String[] args) {
        System.out.println("=== Hipparchus Mathematical Library Demo ===\n");

        demonstrateStatistics();
        demonstrateGeometry();
        demonstrateMathematicalFunctions();

        System.out.println("\n=== Demo Complete ===");
    }

    /**
     * Demonstrates statistical analysis capabilities.
     */
    private static void demonstrateStatistics() {
        System.out.println("1. STATISTICAL ANALYSIS DEMONSTRATION");
        System.out.println("=====================================");

        // Sample data
        double[] data = {1.2, 2.3, 3.4, 4.5, 5.6, 6.7, 7.8, 8.9, 9.0, 10.1};

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double value : data) {
            stats.addValue(value);
        }

        System.out.println("Sample data: " + Arrays.toString(data));
        System.out.printf("Mean: %.4f%n", stats.getMean());
        System.out.printf("Standard Deviation: %.4f%n", stats.getStandardDeviation());
        System.out.printf("Variance: %.4f%n", stats.getVariance());
        System.out.printf("Min: %.4f%n", stats.getMin());
        System.out.printf("Max: %.4f%n", stats.getMax());
        System.out.printf("Median: %.4f%n", stats.getPercentile(50));

        // Percentiles
        Percentile percentile = new Percentile();
        System.out.printf("25th Percentile: %.4f%n", percentile.evaluate(data, 25));
        System.out.printf("75th Percentile: %.4f%n", percentile.evaluate(data, 75));

        System.out.println();
    }

    /**
     * Demonstrates 3D geometry operations.
     */
    private static void demonstrateGeometry() {
        System.out.println("2. 3D GEOMETRY DEMONSTRATION");
        System.out.println("============================");

        // Create 3D vectors
        Vector3D v1 = new Vector3D(1, 2, 3);
        Vector3D v2 = new Vector3D(4, 5, 6);

        System.out.println("Vector v1: " + v1);
        System.out.println("Vector v2: " + v2);

        // Vector operations
        Vector3D sum = v1.add(v2);
        System.out.println("v1 + v2 = " + sum);

        Vector3D cross = v1.crossProduct(v2);
        System.out.println("v1 × v2 (cross product) = " + cross);

        double dot = v1.dotProduct(v2);
        System.out.println("v1 · v2 (dot product) = " + dot);

        double norm = v1.getNorm();
        System.out.println("||v1|| (magnitude) = " + norm);

        Vector3D normalized = v1.normalize();
        System.out.println("v1 normalized = " + normalized);

        // 2D vectors
        Vector2D v2d1 = new Vector2D(3, 4);
        Vector2D v2d2 = new Vector2D(1, 2);

        System.out.println("\n2D Vector v1: " + v2d1);
        System.out.println("2D Vector v2: " + v2d2);

        double angle = Vector2D.angle(v2d1, v2d2);
        System.out.printf("Angle between vectors: %.4f radians (%.2f degrees)%n",
                         angle, FastMath.toDegrees(angle));

        System.out.println();
    }

    /**
     * Demonstrates mathematical functions and utilities.
     */
    private static void demonstrateMathematicalFunctions() {
        System.out.println("3. MATHEMATICAL FUNCTIONS DEMONSTRATION");
        System.out.println("=======================================");

        // Trigonometric functions
        double angle = FastMath.PI / 4; // 45 degrees
        System.out.printf("Angle: %.4f radians (%.1f degrees)%n", angle, FastMath.toDegrees(angle));
        System.out.printf("sin(%.4f) = %.6f%n", angle, FastMath.sin(angle));
        System.out.printf("cos(%.4f) = %.6f%n", angle, FastMath.cos(angle));
        System.out.printf("tan(%.4f) = %.6f%n", angle, FastMath.tan(angle));

        // Exponential and logarithmic functions
        double x = 2.5;
        System.out.printf("\nExponential functions for x = %.1f:%n", x);
        System.out.printf("e^x = %.6f%n", FastMath.exp(x));
        System.out.printf("2^x = %.6f%n", FastMath.pow(2, x));
        System.out.printf("ln(x) = %.6f%n", FastMath.log(x));
        System.out.printf("log10(x) = %.6f%n", FastMath.log10(x));

        // Power and root functions
        double base = 8.0;
        double exponent = 1.0/3.0; // Cube root
        System.out.printf("\nPower functions:%n");
        System.out.printf("8^(1/3) = %.6f (cube root of 8)%n", FastMath.pow(base, exponent));
        System.out.printf("sqrt(16) = %.6f%n", FastMath.sqrt(16));
        System.out.printf("cbrt(27) = %.6f%n", FastMath.cbrt(27));

        // Special mathematical constants
        System.out.printf("\nMathematical constants:%n");
        System.out.printf("π = %.10f%n", FastMath.PI);
        System.out.printf("e = %.10f%n", FastMath.E);

        // Rounding and absolute value
        double[] testValues = {-3.7, 2.3, -1.5, 4.8};
        System.out.printf("\nRounding operations:%n");
        for (double value : testValues) {
            System.out.printf("|%.1f| = %.1f, floor(%.1f) = %.0f, ceil(%.1f) = %.0f%n",
                             value, FastMath.abs(value), value, FastMath.floor(value),
                             value, FastMath.ceil(value));
        }

        System.out.println();
    }
}
