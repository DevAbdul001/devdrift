
# DevDrift

**DevDrift** is a terminal-based coding session tracker built with Java and MariaDB.

It helps you record coding sessions, track when they started and ended, and keep notes about what you worked on.

## Features

* Start a coding session
* End a coding session
* Record session notes
* Track session duration
* Persist sessions in MariaDB
* Interactive terminal CLI
* JDBC-based database access

## Tech Stack

* **Java 25**
* **Maven**
* **MariaDB**
* **JDBC**
* **JUnit 6**

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       ├── cli/
│   │       ├── database/
│   │       ├── sessions/
│   │       ├── utils/
│   │       └── Main.java
│   └── resources/
│       └── db/
│           └── migrations/
└── test/
    └── java/
```

The application follows a simple layered structure:

```
CLI
 ↓
SessionService
 ↓
SessionRepo
 ↓
DBConnection
 ↓
MariaDB
```

## Requirements

* Java 21+
* Maven
* MariaDB

## Configuration

DevDrift uses environment variables for database configuration:

```
export DEVDRIFT_URL="jdbc:mariadb://localhost:3306/devdrift"
export DEVDRIFT_USER="your_username"
export DEVDRIFT_PASSWORD="your_password"
```

Create the database before running the application:

```
CREATE DATABASE devdrift;
```

Then run the initial migration:

```
mysql -u your_username -p devdrift < src/main/resources/db/migrations/V1__initial_schema.sql
```

## Running DevDrift

During development:

```
mvn exec:java -Dexec.mainClass="com.Main" -Dexec.classpathScope=runtime
```

Or, if you've configured the local `devdrift` command:

```
devdrift
```

## CLI

Once the application starts, use:

```
start [notes]
end <session-id>
status
help
exit
```

Example:

```
DevDrift > start Working on Tiko

Session started.

DevDrift > status

Elapsed: 00:42:17

DevDrift > end 1

Session ended.
```

## Testing

Run the test suite with:

```
mvn test
```

## Roadmap

DevDrift is being developed incrementally.

Planned improvements include:

* Session pause/resume
* Improved session statistics
* Historical session views
* Better terminal UI
* Automated database migrations
* Docker support

## Why DevDrift?

DevDrift started as a small project to practice building a Java application from the ground up using JDBC, relational database design, Maven, testing, and a terminal interface.

The goal is to keep the application simple while gradually introducing more advanced backend concepts.

---

**Status:** MVP in development

