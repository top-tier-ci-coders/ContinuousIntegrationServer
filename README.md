# ContinuousIntegrationServer DD2480

### What is the project about?

ContinuousIntegrationServer is a small continuous integration CI server.

### How does it work?
The program is written in Java. The project uses the Gradle buils system.
The server needs to be run on a computer that can accept incoming HTTP requests, or ngrok can also be used if the server has no public IP.
The server url/ngrok url must be added as a webhook on GitHub before GitHub starts sending event notifications to the server.
For each request (notification) sent by GitHub, the function ContinuousIntegrationServer.handle will be called, which will inspect the event and trigger the necessary components (like compiling and testing a new push).
For push events, the server also notifies the pusher by sending him/her an email. The notification system is unit tested by sending a well-known email to a well-known email address.


## Directory structure
`src/main/java/CI`: contains the source files of the project.

`src/tes/java/CI`: contains the tests for the project.

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

## Statement of contributions
### Kartal Bozdogan:
### Andreas Gylling:
### Gustaf Pihl:
### Philippa Örnell:
### Marcus Östling:
