# Publishing Configuration

This document describes how to configure publishing for the Ponder project.

## Repositories

Packages are published to two Maven repositories:

1. **GitHub Packages** - `https://maven.pkg.github.com/Dans-Plugins/Ponder`
2. **DansPlugins Maven Repository** - `https://repo.dansplugins.com/repository/maven-releases/`

## GitHub Actions Secrets

To enable publishing to the DansPlugins Maven repository, the following secrets must be configured in the GitHub repository settings:

- `DANS_MAVEN_USERNAME` - Username for the DansPlugins Maven repository
- `DANS_MAVEN_PASSWORD` - Password/token for the DansPlugins Maven repository

These secrets are automatically used by the `publish-packages.yml` workflow.

## Local Publishing

For local publishing/testing, create a `repo.properties` file in the project root with the following content:

```properties
repo-username=YOUR_USERNAME
repo-password=YOUR_PASSWORD
```

**Note:** The `repo.properties` file is ignored by git and should never be committed to the repository.

## Publishing Process

Publishing is triggered automatically by:
- Pushes to the `main` branch
- Release creation
- Manual workflow dispatch

The workflow will:
1. Build all modules
2. Run tests
3. Publish to both GitHub Packages and DansPlugins Maven Repository

## Gradle Tasks

You can also manually publish using Gradle:

```bash
# Publish to all repositories
./gradlew publish

# Publish only to DansPlugins repository
./gradlew publishAllPublicationsToDansPluginsRepository

# Publish only to GitHub Packages
./gradlew publishAllPublicationsToGitHubPackagesRepository
```

## Credential Priority

Credentials are resolved in the following order:
1. Environment variables (`DANS_MAVEN_USERNAME` and `DANS_MAVEN_PASSWORD`)
2. Local `repo.properties` file

This allows local development to use the properties file while CI/CD uses environment variables from secrets.
