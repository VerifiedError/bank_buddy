# Bank Buddy

A comprehensive banking assistant plugin for RuneLite that helps organize and manage your bank efficiently.

## Features

- **🏦 Bank Value Calculation**: Real-time calculation and display of total bank value
- **📊 Bank Statistics**: Detailed statistics including total items, unique items, and bank space usage
- **💰 Valuable Item Detection**: Automatically identify and list your most valuable items
- **📂 Category Organization**: Organize items by categories (Combat, Skilling, Potions, etc.)
- **⚠️ Smart Notifications**: Configurable alerts for bank space warnings and value changes
- **🎨 Customizable Overlay**: Adjustable overlay with compact mode and color options
- **🔍 Enhanced Bank Menus**: Right-click bank for additional organization options
- **⚙️ Comprehensive Settings**: Fully configurable thresholds, colors, and display options

### Bank Buddy Overlay Features:
- Total bank value with formatted display
- Item count statistics (total and unique)
- Bank space utilization with color-coded warnings
- Compact mode for minimal screen usage
- Customizable colors and positioning

### Right-Click Bank Menu Options:
- **Bank Stats**: Quick overview of bank statistics
- **Calculate Value**: Force recalculation of bank value
- **Valuable Items**: List items above configured threshold
- **Category Values**: Show value breakdown by item categories

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
- **Java 11** (REQUIRED - Java 21+ will cause build issues with current Gradle setup)
- Gradle (automatically downloaded by wrapper)
- RuneLite development environment

**Important**: This plugin requires Java 11 to build and run properly. Using newer Java versions (17, 21, 24) will cause Gradle compatibility issues. Make sure to install and use Java 11 specifically for RuneLite plugin development.

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
- ✅ **Core Banking Features**: Real-time bank value calculation and item statistics
- ✅ **Smart Overlay System**: Customizable overlay with bank information display
- ✅ **Advanced Organization**: Item categorization and valuable item detection
- ✅ **Enhanced User Interface**: Right-click menu options and enhanced bank interactions
- ✅ **Comprehensive Configuration**: 15+ configurable settings across 4 categories
- ✅ **Intelligent Notifications**: Bank space warnings and value change alerts
- ✅ **Professional Code Structure**: Modular design with BankOrganizer utility class
- ✅ **RuneLite Integration**: Full integration with RuneLite API and item management

## Credits

Created by VerifiedError for the RuneLite community.

---

**Note**: This plugin is designed to enhance your banking experience while staying within RuneLite and Old School RuneScape guidelines. Always ensure you're using approved plugins and following game rules.