## Sscs-common
This is the common code library for sscs. Currently it contains:

- CCD domain objects
- Create, Update and Search for cases in CCD
- Idam OAuth and service authorization tokens
- Airlookup service

### Build

To build run

```bash
./gradlew publishToMavenLocal
```
This will create a jar file in the projects build libs directory with a version of DEV-SNAPSHOT.
If you want to then depend on this without publishing it you can add the following to your build.gradle file
in the dependencies section.

```gradle
compile group: 'com.github.hmcts', name: 'sscs-common', version: 'DEV-SNAPSHOT'
```
Once the changes have been merged into master a new verison of the library will be build on travis and 
published to bintray with a version number of X.X.{TRAVIS_BUULD_NUMBER}.

Travis build
https://travis-ci.org/hmcts/sscs-common

Bintray repo
https://dl.bintray.com/hmcts/hmcts-maven/uk/gov/hmcts/reform/sscs-common/ 

### Sync with sscs-ccd-definition
When you add fields to the sscs-common, you should also update the sscs-ccd-definition so that the
definition file in AAT matches the java model in sscs-common, thereby avoiding validation failures.
We recommend that the AAT CCD definition to be generated through master branch

### Usage

To use this you will need to have setup the following properties in your application.yaml.

Dependent projects must implement the Spring Annotation ```@EnableScheduling``` on the
class which is defined as the ```@SpringBootApplication```.

```$yaml
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
