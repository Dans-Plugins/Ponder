# Ponder

## Description
This library contains a collection of modules that assist with the development of console applications and Minecraft plugins. 

## Projects

A [list of projects](https://github.com/Preponderous-Software/Ponder/wiki/Projects) is available on the wiki.

## Support

For support, you can join our [support discord server](https://discord.gg/G6wQxfcBMt).

### Experiencing a bug?

Please [fill out a bug report here](https://github.com/Preponderous-Software/Ponder/issues?q=is%3Aissue+is%3Aopen+label%3Abug).

## Example Application

An example of an application created with Ponder can be
found [here](https://github.com/Preponderous-Software/ExamplePonderApplication).

## Example Spigot Plugin

An example of a Minecraft plugin created with Ponder can be
found [here](https://github.com/Preponderous-Software/ExamplePonderPlugin).

## Maven

### repository

```
        <repository>
            <id>dansplugins.com</id>
            <url>https://repo.dansplugins.com/repository/maven-public/</url>
        </repository>
```

### GitHub Packages

Packages are also available via GitHub Packages. To use them, add the following repository to your `pom.xml`:

```xml
        <repository>
            <id>github</id>
            <url>https://maven.pkg.github.com/Dans-Plugins/Ponder</url>
        </repository>
```

You'll need to authenticate with GitHub Packages. Add your GitHub username and a personal access token with `read:packages` scope to your `~/.m2/settings.xml`:

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

### ponder-bukkit

```

        <dependency>
            <groupId>com.dansplugins</groupId>
            <artifactId>ponder-bukkit</artifactId>
            <version>2.0.0</version>
        </dependency>
```

### ponder-cache

```

        <dependency>
            <groupId>com.dansplugins</groupId>
            <artifactId>ponder-cache</artifactId>
            <version>2.0.0</version>
        </dependency>
```

### ponder-commands

```

        <dependency>
            <groupId>com.dansplugins</groupId>
            <artifactId>ponder-commands</artifactId>
            <version>2.0.0</version>
        </dependency>
```

## Gradle

### repository

```groovy
repositories {
    maven {
        url 'https://repo.dansplugins.com/repository/maven-public/'
    }
}
```

### GitHub Packages

Packages are also available via GitHub Packages. To use them with Gradle:

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

### Dependencies

```groovy
dependencies {
    implementation 'com.dansplugins:ponder-bukkit:2.0.0'
    implementation 'com.dansplugins:ponder-cache:2.0.0'
    implementation 'com.dansplugins:ponder-commands:2.0.0'
}
```

## Authors and acknowledgement

Name | Main Contributions
------------ | -------------
[Daniel Stephenson](https://github.com/dmccoystephenson) | Creator
Callum | wrote some methods that were used in AbstractPluginCommand and ApplicationCommand; contributed NMSAssistant and NMSVersion; improved ColorChecker; contributed ColorConverter; improved EventHandlerRegistry; improved UUIDChecker; improved ArgumentParser; contributed ConfigurationFile
[Pasarus](https://github.com/Pasarus) | wrote code that was used in JsonWriterReader and UUIDChecker; contributed Pair
Caibinus | wrote code that was used in BlockChecker
Deej | Set up the javadocs
VoChiDanh | Improved the BlockChecker and added the MaterialChecker
alyphen | Generified types of maps, lists and sets across the project, massively overhauled the project and split it into three modules, introduced tests

Have you made a contribution and don't see yourself above? Please add your name and open a PR!

## Project Status

This project is in active development.

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

**Why MIT?**  
The MIT License is short, permissive, and widely understood. It allows anyone to use, modify, distribute, and even commercially exploit the software with minimal restrictions, as long as they include the original license and copyright notice.  
We chose MIT to **maximize adoption and flexibility**, making it easy for individuals and organizations to integrate this project into their own work without legal complexity.

