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
Compilation is handled by GitHandler.build_branch
Since we use gradle in our project, it merely executes the command "gradlew build -x test" and compares the result to 0 to see whether it succeeded.
Compilation is unit tested in GitHandlerTest.testBuildBranch
The test tries to pull and build the branches "nobuild" and "testsfail". Both of these branches are dummy branches that have been created specifically for unit tests.
The branch "nobuild" has compilation errors, whereas the branch "testsfail" builds properly, but fails its own tests.
### How test execution has been implemented and unit-tested
Test execution is made in the class GitHandler with corresponding test code in GitHandlerTest. We used Java Runtime library to execute terminal commands. We wrote mostly bash commands as we save the build in a separate build folder. The commands execute Gradle commands in order to either build or test the branch.

For unit testing, JUnit is used. We try several scenarios of successful and unsuccessful branch building and testing. See documentation in the GitHandlerTest class for more information about test cases.

### How notification has been implemented and unit-tested.
Notification was made using JavaMail API and Gmail SMTP server. A gmail account called toptierci was created and used for sending notifications to the pusher. The server is also notified in its terminal about the push.
The function that send the email ("send_nofitication") is tested in GitHandlerTest.testSendNotificationSuccess and GitHandlerTest.testSendNotificationFail. The former sends a specific email to a specific email address and makes sure that the email was sent. The latter tries to send an email to a malformed address and makes sure that it fails.

### The Build List URL
To see a list of all builds so far, visit [http://27e4b890.ngrok.io/list](http://27e4b890.ngrok.io/list)

## Statement of contributions
### Kartal Bozdogan:
Implement the handle function and create test cases. Implement send_notification and its test cases. Various bug fixes. Expanded some of the test cases. Various modifications to the code to enable more accurate and in-depth testing. Documentation and code review.
### Andreas Gylling:
Implemented request_push, pull_branch,start_tests in the GitHandler class and corresponding test cases in the GitHandlerTest class. Also created skeleton for the GitHandler class. Documentation and reviewing code.
### Gustaf Pihl:
Implemented the classes GitEvent, GitEventTest, BuildLogger and BuildLoggerTest. The GitEvent class parses and stores relevant information from the json payload sent by git. The BuildLogger class handles logging of builds. Documentation and code review.
### Philippa Örnell:
Implemented pull_branch, start_tests (pair programmed with Andreas) in the GitHandler class and corresponding test cases in the GitHandlerTest class. Documentation and reviewing code. Also assisted in solving the email notification.
### Marcus Östling:
