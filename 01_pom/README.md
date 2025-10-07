# Spring Boot Parent POM Emulation

This directory contains a custom parent POM structure that emulates Spring Boot's approach while maintaining full control over build configurations. This is the **industry standard** approach used by enterprise projects.

## 🏗️ Architecture Overview

```none
01_pom/
├── pom.xml                    # Root aggregator (foo-pom)
├── foo-dependencies/          # Dependency management (BOM-like)
│   └── pom.xml
└── foo-starter-parent/        # Build configuration (starter-parent-like)
    └── pom.xml
```

### **foo-pom** (Root Aggregator)

- Defines the `revision` property for CI-friendly versions
- Aggregates all sub-modules
- Runs `flatten-maven-plugin` to create self-contained POMs

### **foo-dependencies** (BOM Equivalent)

- Imports `spring-boot-dependencies` BOM
- Defines custom dependency versions
- Provides dependency management for all child modules
- **Does NOT** define plugin versions (that's handled by starter-parent)

### **foo-starter-parent** (Starter Parent Equivalent)

- Inherits from `foo-dependencies`
- Defines all Maven plugin versions and configurations
- Provides `pluginManagement` for consistent builds
- **This is where application modules inherit from**

## 🎯 Why This Approach is Best Practice

### ✅ **Advantages Over Spring Boot Parent**

1. **Full Control**: You control exactly which plugin versions are used
2. **Predictable Builds**: No surprises from Spring Boot's plugin changes
3. **Enterprise Compliance**: You can enforce your company's standards
4. **Customization**: You can add plugins Spring Boot doesn't provide
5. **Version Management**: You control when to upgrade plugin versions

### ❌ **Why NOT to Use Spring Boot Parent Directly**

1. **Loss of Control**: Hard to override Spring Boot's configurations
2. **Plugin Conflicts**: Spring Boot's versions might conflict with your needs
3. **Enterprise Requirements**: Most companies have their own build standards
4. **Customization Limits**: Difficult to add custom plugins or configurations

## 🏢 Industry Examples

Major enterprise projects use this exact approach:

- **Netflix**: Custom parent POMs with Spring Boot BOM imports
- **Uber**: Custom build configurations with Spring Boot BOM
- **Airbnb**: Custom parent POMs for their microservices
- **Most Fortune 500 companies**: Custom parent POMs for compliance and control

## 🔄 Version Management Strategy

### **Spring Boot Version Updates**

When you bump Spring Boot version (e.g., `3.5.6` → `3.6.0`):

1. **Update Spring Boot version** in `foo-dependencies/pom.xml`
2. **Sync Maven plugin versions** in `foo-starter-parent/pom.xml` to match Spring Boot BOM
3. **Test the build** to ensure compatibility

### **Plugin Version Sync**

You need to manually sync these plugin versions in `foo-starter-parent/pom.xml`:

```xml
<properties>
    <!-- Spring managed plugins -->
    <maven-compiler-plugin.version>3.14.0</maven-compiler-plugin.version>
    <maven-enforcer-plugin.version>3.5.0</maven-enforcer-plugin.version>
    <maven-failsafe-plugin.version>3.5.4</maven-failsafe-plugin.version>
    <maven-jar-plugin.version>3.4.2</maven-jar-plugin.version>
    <maven-resources-plugin.version>3.3.1</maven-resources-plugin.version>
    <maven-shade-plugin.version>3.6.1</maven-shade-plugin.version>
    <maven-surefire-plugin.version>3.5.4</maven-surefire-plugin.version>
    <spring-boot-maven-plugin.version>${spring-boot.version}</spring-boot-maven-plugin.version>
</properties>
```

**Why this manual sync is necessary:**

- The `flatten-maven-plugin` with `resolveCiFriendliesOnly` mode doesn't process BOM imports
- The source POM (installed to repository) needs resolved versions
- This gives you full control over which versions are used

## 🛠️ Build Process

### **Flatten Plugin Configuration**

The `flatten-maven-plugin` is configured to:

- Run only on POM modules (`foo-pom`, `foo-dependencies`, `foo-starter-parent`)
- Use `resolveCiFriendliesOnly` mode for CI-friendly versions
- Create self-contained POMs in `target/pom.xml`

### **Inheritance Chain**

```none
Application Module
    ↓ inherits from
foo-starter-parent
    ↓ inherits from
foo-dependencies
    ↓ imports
spring-boot-dependencies (BOM)
```

## 🔍 Debugging Tools

Use the provided script to analyze effective POMs:

```bash
./scripts/generate-effective-poms.sh
```

This generates `target/effective-pom.xml` files showing:

- Resolved properties and versions
- Complete plugin configurations
- Dependency management details

## 📋 Maintenance Checklist

When updating Spring Boot version:

- [ ] Update `spring-boot.version` in `foo-dependencies/pom.xml`
- [ ] Check Spring Boot BOM for new plugin versions
- [ ] Update plugin versions in `foo-starter-parent/pom.xml`
- [ ] Test build with `mvn clean compile`
- [ ] Run effective POM script to verify resolutions
- [ ] Update application modules if needed

## 🎉 Benefits Summary

This approach provides:

1. **Enterprise-Grade Control**: Full control over build configuration
2. **Spring Boot Compatibility**: All Spring Boot features work seamlessly
3. **CI-Friendly Versions**: Support for `${revision}` property
4. **Self-Contained POMs**: Flattened POMs for distribution
5. **Industry Standard**: Used by major enterprise projects
6. **Maintainable**: Clear separation of concerns
7. **Flexible**: Easy to customize and extend

## 🚀 Getting Started

1. **Inherit from `foo-starter-parent`** in your application modules
2. **Add dependencies** without versions (managed by BOM)
3. **Customize plugins** as needed in your module's POM
4. **Use the debugging script** when troubleshooting

This architecture gives you the best of both worlds: Spring Boot's dependency management with enterprise-grade build control.
