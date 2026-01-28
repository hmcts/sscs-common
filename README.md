# SSCS - Common Library

A shared library for SSCS projects, providing utilities for interacting with CCD, Idam, and other services.

## Table of Contents
- [Background](#background)
- [Build](#build)
- [Usage](#usage)
- [Utility](#utility)
- [Azure DevOps Artifacts Integration](#Azure-DevOps-Artifacts-Integration)

## Background

The SSCS Common Library is part of the SSCS ecosystem, offering reusable components for managing cases, authentication, and service integrations. It includes CCD domain objects, case operations, and token generation utilities.

## Build

To build the library locally:

```bash
./gradlew publishToMavenLocal
```

This will create a JAR file in the project's `build/libs` directory with the version `DEV-SNAPSHOT`. To use this locally without publishing, add the following to your `build.gradle` file:

```gradle
dependencies {
    compile group: 'com.github.hmcts', name: 'sscs-common', version: 'DEV-SNAPSHOT'
}
```

## Usage

To use this library, configure the following properties in your `application.yaml`:

```yaml
idam:
  s2s-auth:
    totp_secret: ${IDAM.S2S-AUTH.TOTP_SECRET:AAAAAAAAAAAAAAAC}
    microservice: ${IDAM.S2S-AUTH.MICROSERVICE:sscs}
    url: ${IDAM.S2S-AUTH:http://localhost:4502}
  oauth2:
    user:
      email: ${IDAM_SSCS_SYSTEMUPDATE_USER:SSCS_SYSTEM_UPDATE}
      password: ${IDAM_SSCS_SYSTEMUPDATE_PASSWORD:SSCS_SYSTEM_UPDATE}
    client:
      id: ${IDAM_OAUTH2_CLIENT_ID:sscs}
      secret: ${IDAM_OAUTH2_CLIENT_SECRET:QM5RQQ53LZFOSIXJ}
    url: ${IDAM_URL:http://localhost:4501}
    redirectUrl: ${IDAM_SSCS_URL:https://localhost:9000/poc}
core_case_data:
  api:
    url: ${CORE_CASE_DATA_URL:http://localhost:4452}
  caseTypeId: Benefit
  jurisdictionId: SSCS
```

Dependent projects must implement the Spring annotation `@EnableScheduling` on the class defined as the `@SpringBootApplication`.

## Utility

### Panel Category Map Generation

The `PanelCategoryMapParser` utility reads data from the `JOHTier_PanelMemberComposition_1.2.csv` file, converts it into JSON, and overwrites `panel-category-map.json`. Do not manually edit `panel-category-map.json` as changes will be overwritten during regeneration.

To generate the panel category map:

```bash
./gradlew generatePanelCategoryMap
```

File locations:
- `src/main/resources/reference-data/JOHTier_PanelMemberComposition_1.2.csv`
- `src/main/resources/reference-data/panel-category-map.json`

## Azure DevOps Artifacts Integration

This library is hosted on Azure DevOps Artifacts and can be used in your project by adding the following to your `build.gradle` file:

 ```gradle
repositories {
  maven {
    url = uri('https://pkgs.dev.azure.com/hmcts/Artifacts/_packaging/hmcts-lib/maven/v1')
  }
}

 dependencies {
   implementation 'com.github.hmcts:sscs-common:LATEST_TAG'
 }
 ```

## Updating dependent repositories

Each update to this library must be reflected in all dependent projects regardless of the change. The current dependants are:

* sscs-tribunals-case-api
* sscs-case-loader
* sscs-ccd-case-migration
* sscs-cron-trigger

Any dependant not directly required as part of the change can be updated at the point that sscs-common is released and the required dependents are updated.

Prior to releasing a new version of this library, changes can be tested in dependents across non-prod environments using the build tag that the Jenkins build produces (update the sscs-common version in the dependant build.gradle file). 

When ready to release, sscs-common should be released first and all dependants updated with the actual release version