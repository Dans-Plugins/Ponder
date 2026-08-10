# Ponder

## Description

Ponder is a Java/Kotlin library that contains a collection of modules to assist with the development of Minecraft plugins. It provides utilities for Bukkit/Spigot plugins (distance calculations, plugin helpers), a caching system, and a command abstraction layer.

## Installation

### Maven

Add the DansPlugins repository and the desired module(s) to your `pom.xml`:

```xml
<repository>
    <id>dansplugins.com</id>
    <url>https://repo.dansplugins.com/repository/maven-public/</url>
</repository>
```

```xml
<dependency>
    <groupId>com.dansplugins</groupId>
    <artifactId>ponder-bukkit</artifactId>
    <version>3.0.0-SNAPSHOT-8-8-2026</version>
</dependency>
<dependency>
    <groupId>com.dansplugins</groupId>
    <artifactId>ponder-cache</artifactId>
    <version>3.0.0-SNAPSHOT-8-8-2026</version>
</dependency>
<dependency>
    <groupId>com.dansplugins</groupId>
    <artifactId>ponder-commands</artifactId>
    <version>3.0.0-SNAPSHOT-8-8-2026</version>
</dependency>
```

### GitHub Packages (Maven)

Packages are also available via GitHub Packages. Add the repository to your `pom.xml`:

```xml
<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/Dans-Plugins/Ponder</url>
</repository>
```

Authenticate by adding your GitHub username and a personal access token with `read:packages` scope to `~/.m2/settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>
</settings>
```

### Gradle

Add the repository and dependency to your `build.gradle`:

```groovy
repositories {
    maven {
        url 'https://repo.dansplugins.com/repository/maven-public/'
    }
}

dependencies {
    implementation 'com.dansplugins:ponder-bukkit:3.0.0-SNAPSHOT-8-8-2026'
    implementation 'com.dansplugins:ponder-cache:3.0.0-SNAPSHOT-8-8-2026'
    implementation 'com.dansplugins:ponder-commands:3.0.0-SNAPSHOT-8-8-2026'
}
```

### GitHub Packages (Gradle)

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Dans-Plugins/Ponder")
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("GITHUB_ACTOR")
            password = project.findProperty("gpr.token") ?: System.getenv("GITHUB_TOKEN")
        }
    }
}
```

Add your credentials to `~/.gradle/gradle.properties`:

```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

### Example Projects

- [Example Spigot Plugin](https://github.com/Preponderous-Software/ExamplePonderPlugin)
- [Example Application](https://github.com/Preponderous-Software/ExamplePonderApplication)

## Usage

### Documentation

- [User Guide](USER_GUIDE.md) – Getting started and common usage scenarios
- [API Reference](COMMANDS.md) – Overview of the public API provided by each module
- [Configuration Guide](CONFIG.md) – Configuration options for `ponder-cache`

### Wiki & Additional Resources

- [Projects using Ponder](https://github.com/Preponderous-Software/Ponder/wiki/Projects)

## Support

You can find the support Discord server [here](https://discord.gg/xXtuAQ2).

### Experiencing a bug?

Please fill out a bug report [here](https://github.com/Dans-Plugins/Ponder/issues/new).

- [Known Bugs](https://github.com/Dans-Plugins/Ponder/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

## Contributing

- [CONTRIBUTING.md](CONTRIBUTING.md)
- [Notes for Developers](https://github.com/Preponderous-Software/Ponder/wiki)

## Testing

### Unit Tests

Linux:

```
./gradlew clean test
```

Windows:

```
.\gradlew.bat clean test
```

If you see `BUILD SUCCESSFUL`, the tests have passed.

## Development

### Manual Integration Testing

Ponder is a library rather than a plugin, so it is exercised on a server through a plugin that depends on it. For manual integration testing, build the project, install the JARs into the dependent plugin's build, and run that plugin on a local Spigot server. No test server or hot-reloading tooling is provided by this repository.

#### Building

```
./gradlew build
```

The compiled JARs are placed in each module's `build/libs/` directory.

## Authors and Acknowledgement

### Developers

| Name | Main Contributions |
|------|--------------------|
| [Daniel Stephenson](https://github.com/dmccoystephenson) | Creator |
| Callum | Methods in AbstractPluginCommand and ApplicationCommand; NMSAssistant; NMSVersion; ColorChecker; ColorConverter; EventHandlerRegistry improvements; UUIDChecker; ArgumentParser; ConfigurationFile |
| [Pasarus](https://github.com/Pasarus) | JsonWriterReader; UUIDChecker; Pair |
| Caibinus | BlockChecker |
| Deej | Javadocs setup |
| VoChiDanh | BlockChecker improvements; MaterialChecker |
| alyphen | Generified types; project restructure into modules; tests |

Have you made a contribution and don't see yourself above? Please add your name and open a PR!

## License

This project is licensed under the [MIT License](LICENSE).

You are free to use, modify, and distribute this software, provided that the original license and copyright notice are included in all copies or substantial portions of the software.

See the [LICENSE](LICENSE) file for the full text.

## Project Status

This project is in active development.

### Changelog

See [CHANGELOG.md](CHANGELOG.md) for a release-by-release summary of changes.

