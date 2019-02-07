# ContinuousIntegrationServer DD2480

### What is the project about?

ContinuousIntegrationServer is a small continuous integration CI server.

### How does it work?
The program is written in Java. The project uses the Gradle build system.
The server needs to be run on a computer that can accept incoming HTTP requests, or ngrok can also be used if the server has no public IP.
The server url/ngrok url must be added as a webhook on GitHub before GitHub starts sending event notifications to the server.
For each request (notification) sent by GitHub, the function ContinuousIntegrationServer.handle will be called, which will inspect the event and trigger the necessary components (like compiling and testing a new push).
For push events, the server also notifies the pusher by sending him/her an email. The notification system is unit tested by sending a well-known email to a well-known email address.


## Directory structure
`src/main/java/CI`: contains the source files of the project.

`src/test/java/CI`: contains the tests for the project.

`build.gradle`: Configuration file for gradle

`README.md`: this file.

`CONTRIBUTING.md`: Describes the commit message format

## Tests
Tests are written using JUnit.

Run all tests by executing `./gradlew test`

## Build

To build the project, run `./gradlew build`

## Run

To run the project, run `./gradlew run`

### How compilation has been implemented and unit-tested.
TODO
### How test execution has been implemented and unit-tested
Test execution is made in the class GitHandler with corresponding test code in GitHandlerTest. We used Java Runtime library to execute terminal commands. We wrote mostly bash commands as we save the build in a separate build folder. The commands execute Gradle commands in order to either build or test the branch.

For unit testing, JUnit is used. We try several scenarios of successful and unsuccessful branch building and testing. See documentation in the GitHandlerTest class for more information about test cases.

### How notification has been implemented and unit-tested.
Notification was made using JavaMail API and Gmail SMTP server. A gmail account called toptierci@gmail.com was created and used for sending notifications to the pusher. The server is also notified in its terminal about the push.

## Statement of contributions
### Kartal Bozdogan:
### Andreas Gylling:
Implemented request_push, pull_branch,start_tests in the GitHandler class and corresponding test cases in the GitHandlerTest class. Also created skeleton for the GitHandler class. Documentation and reviewing code.
### Gustaf Pihl:
### Philippa Örnell:
Implemented pull_branch, start_tests (pair programmed with Andreas) in the GitHandler class and corresponding test cases in the GitHandlerTest class. Documentation and reviewing code. Also assisted in solving the email notification.
### Marcus Östling:
