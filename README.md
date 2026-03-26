# ParamLiner

A lightweight, zero-dependency Java utility for parsing command-line style strings into parameters, with intelligent handling of quoted strings.

## Features

- **Simple Parsing**: Splits strings into parameters while preserving quoted text as single tokens.
- **Configurable Flags**: Supports various parsing modes (condense whitespace, ignore quotes, trim quoted params).
- **Low Overhead**: No external dependencies, minimal memory footprint.
- **Thread-Safe**: Immutable instances are thread-safe; mutable via setters.

## Usage

### Basic Example

```java
ParamLiner parser = new ParamLiner();
String[] params = parser.parse("command param1 \"param2 with spaces\" param3");
// Output: ["command", "param1", "param2 with spaces", "param3"]
```

### With Flags

```java
ParamLiner parser = new ParamLiner(ParamLiner.CONDENSE_ALL | ParamLiner.TRIM_ALL_ANSWERS);
String[] params = parser.parse("command   \"  param2 with spaces  \"   param3");
// Output: ["command", "param2 with spaces", "param3"]
```

### Available Flags

- `CONDENSE_ALL`: Condense all whitespace before parsing.
- `IGNORE_QUOTES`: Treat quotes as regular characters.
- `TRIM_ALL_ANSWERS`: Trim whitespace from quoted parameters.

## Installation

### Prerequisites

- [Java JDK 17](https://www.oracle.com/java/technologies/downloads/) or higher
- [Apache Maven](https://maven.apache.org/download.cgi) (optional, but recommended)

### Clone and Build with Maven

The easiest way to get started is to clone the repository and build it using Maven.

```bash
git clone https://github.com/zarterstein/ParamLiner.git
cd ParamLiner
mvn clean install
```

### Direct Use

Since `ParamLiner` has no external dependencies, you can also simply copy `ParamLiner.java` directly into your project's source tree.

## Building and Testing

### Build with Maven

```bash
mvn clean compile
```

### Run Unit Tests (Maven)

```bash
mvn test
```

### Run Console Tests

```bash
mvn compile exec:java -Dexec.mainClass="com.zarterstein.ParamLiner.ParamLinerTestConsole" -Dexec.classpathScope="test"
```

### Alternative: Standard Java Compilation

If you prefer not to use Maven:

#### Compile

```bash
javac src/main/java/com/zarterstein/ParamLiner/ParamLiner.java -d target/classes
```

#### Run Console Tests

```bash
javac -cp target/classes src/test/java/com/zarterstein/ParamLiner/ParamLinerTestConsole.java -d target/test-classes
java -cp target/classes;target/test-classes com.zarterstein.ParamLiner.ParamLinerTestConsole
```

## API Reference

See Javadocs in the source code for detailed method documentation.

## Contributing

Contributions welcome! Please submit issues or pull requests on GitHub.

## License

[MIT License](LICENSE) 

## Credits

- Java code: Human-written
- Javadocs and README: Generated with LLM assistance
