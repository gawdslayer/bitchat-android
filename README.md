# Bitchat Android Client

A decentralized, peer-to-peer messaging app that works over Bluetooth mesh networks. This is the Android implementation of the Bitchat protocol, designed to be fully compatible with the iOS/macOS version.

## Features

- **Decentralized Mesh Network**: Automatic peer discovery and multi-hop message relay over Bluetooth LE
- **End-to-End Encryption**: X25519 key exchange + AES-256-GCM for private messages
- **Channel-Based Chats**: Topic-based group messaging with optional password protection
- **Store & Forward**: Messages cached for offline peers and delivered when they reconnect
- **Privacy First**: No accounts, no phone numbers, no persistent identifiers
- **IRC-Style Commands**: Familiar `/join`, `/msg`, `/who` style interface
- **Cross-Platform Compatibility**: Works seamlessly with iOS/macOS Bitchat clients

## Protocol Compatibility

This Android client implements the same protocol as the iOS version:

- **BLE Service UUID**: `F47B5E2D-4A9E-4C5A-9B3F-8E1D2C3A4B5C`
- **BLE Characteristic UUID**: `A1B2C3D4-E5F6-4A5B-8C9D-0E1F2A3B4C5D`
- **Binary Protocol**: Same packet structure and encoding
- **Encryption**: X25519 + AES-256-GCM + Ed25519 signatures
- **Mesh Networking**: TTL-based routing with store-and-forward

## Setup

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK API 21+ (Android 5.0+)
- Bluetooth LE capable device
- Android device for testing (Bluetooth doesn't work in emulator)

### Building

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/bitchat-android.git
   cd bitchat-android
   ```

2. Open in Android Studio:
   ```bash
   android-studio .
   ```

3. Sync Gradle and build the project

4. Install on your device and test

## Usage

### Basic Commands

- `/j #channel` - Join or create a channel
- `/m @name message` - Send a private message
- `/w` - List online users
- `/channels` - Show all discovered channels
- `/block @name` - Block a peer from messaging you
- `/clear` - Clear chat messages
- `/pass [password]` - Set/change channel password (owner only)

### Getting Started

1. Launch Bitchat on your Android device
2. Set your nickname (or use the auto-generated one)
3. You'll automatically connect to nearby peers
4. Join a channel with `/j #general` or start chatting in public
5. Messages relay through the mesh network to reach distant peers

## Architecture

```
📱 UI Layer (Jetpack Compose)
    ↓
🔄 ViewModel Layer (MVVM)
    ↓
🔧 Service Layer (Encryption, BLE, Message Handling)
    ↓
📡 Transport Layer (Bluetooth LE + Binary Protocol)
```

## Security & Privacy

- **End-to-end encryption** for private messages
- **Channel encryption** with password-based key derivation
- **Digital signatures** for message authenticity
- **Forward secrecy** with ephemeral keys
- **No registration** required - completely anonymous
- **Ephemeral messages** by default

## Development

### Project Structure

```
app/
├── src/main/
│   ├── java/com/bitchat/
│   │   ├── ui/           # Jetpack Compose UI components
│   │   ├── viewmodel/    # MVVM ViewModels
│   │   ├── service/      # BLE, encryption, message services
│   │   ├── protocol/     # Binary protocol implementation
│   │   ├── crypto/       # Encryption and key management
│   │   └── util/         # Utility classes
│   └── res/              # Resources
└── build.gradle          # Dependencies and build config
```

### Key Dependencies

- **Jetpack Compose**: Modern UI toolkit
- **Android BLE APIs**: Bluetooth Low Energy communication
- **Bouncy Castle**: Cryptography library
- **Coroutines**: Asynchronous programming
- **Room**: Local message storage
- **Hilt**: Dependency injection

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is released into the public domain. See the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Original iOS/macOS implementation by [jackjackbits](https://github.com/jackjackbits/bitchat)
- Protocol design and security architecture from the Bitchat whitepaper
- Android BLE community for technical guidance 