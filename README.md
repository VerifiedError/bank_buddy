# Bank Buddy

A comprehensive banking assistant plugin for RuneLite that helps organize and manage your bank efficiently.

## Features

- **Bank Organization**: Automatic bank organization and management tools
- **Item Value Display**: View item values directly in your bank interface  
- **Search Enhancements**: Improved bank search functionality
- **Welcome Messages**: Configurable welcome messages when logging in
- **Customizable Settings**: Full configuration options for all features

## Installation

### Plugin Hub Installation (Recommended)
1. Open RuneLite
2. Go to the Plugin Hub
3. Search for "Bank Buddy"
4. Install the plugin

### Manual Installation (Development)
1. Clone this repository
2. Build the plugin using Gradle:
   ```bash
   ./gradlew shadowJar
   ```
3. Load the plugin in RuneLite's external plugin manager

## Configuration

The plugin offers several configuration options:

- **Enable Welcome Message**: Show a welcome message when logging in
- **Enable Bank Organization**: Enable automatic bank organization features  
- **Enable Item Value Display**: Show item values in the bank interface
- **Enable Search Enhancements**: Enhance bank search functionality

Access these settings through the RuneLite configuration panel under "Bank Buddy".

## Development

### Requirements
- Java 11
- Gradle
- RuneLite development environment

### Building
```bash
./gradlew clean build
```

### Testing
```bash
./gradlew shadowJar
java -jar build/libs/bank-buddy-*-all.jar
```

## Project Structure

```
bank_buddy/
├── gradle/                    # Gradle wrapper files
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/bankbuddy/
│   │           ├── BankBuddyPlugin.java      # Main plugin class
│   │           └── BankBuddyConfig.java      # Configuration interface
│   └── test/
│       └── java/
│           └── com/bankbuddy/
│               └── BankBuddyPluginTest.java  # Test runner
├── build.gradle              # Build configuration
├── runelite-plugin.properties # Plugin metadata
└── settings.gradle           # Gradle settings
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any issues or have suggestions for improvements, please:

1. Check existing issues on GitHub
2. Create a new issue with detailed information
3. Include your RuneLite version and plugin version

## Changelog

### Version 1.0.0
- Initial release
- Basic bank organization features
- Item value display
- Search enhancements
- Configurable settings

## Credits

Created by VerifiedError for the RuneLite community.

---

**Note**: This plugin is designed to enhance your banking experience while staying within RuneLite and Old School RuneScape guidelines. Always ensure you're using approved plugins and following game rules.