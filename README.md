## Sscs-common

This is the common code for sscs. Currently it contains the following

- Code to connect to CCD.
- Code to get the Idam OAuth and service authorization tokens.

###Build

To build run

```bash
./gradlew clean build
```
This will create a jar file in the projects build libs directory with a version of DEV-SNAPSHOT.
If you want to then depend on this without publishing it you can add the following to your build.gradle file
in the dependencies section.

```gradle
compile files('{PROJECT_DIR}/sscs-common/build/libs/sscs-common-0.0.DEV-SNAPSHOT.jar')
```
Once the changes have been merged into master a new verison of the library will be build on travis and 
published to bintray with a version number of X.X.{TRAVIS_BUULD_NUMBER}.

Travis build
https://travis-ci.org/hmcts/sscs-common

Bintray repo
https://dl.bintray.com/hmcts/hmcts-maven/uk/gov/hmcts/reform/sscs-common/ 


###Usage

To use this you will need to have setup the following properties in your application.yaml.

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
