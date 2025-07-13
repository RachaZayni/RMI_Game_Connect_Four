# 🟡🔴 Connect Four Multiplayer Game (Java RMI)

A Java-based Connect Four game that supports **multiplayer over network** using **RMI (Remote Method Invocation)**. Players can:
- Register and log in with a unique ID
- Start new multiplayer sessions
- Join shared games
- Play against an AI opponent (computer)

## 🖥️ Features
- Multiplayer gameplay using Java RMI
- GUI built with Java Swing
- Player registration and authentication
- Online/offline player tracking
- Game session management and invitation system
- AI computer player mode
- Real-time game state broadcast

## 📂 Project Structure

```
GAME/
├── ConnectWindow.java
├── ConnectFourGUI.java
├── GameSession.java
├── IPlayer.java
├── IPlayerWrapper.java
├── IServer.java
├── IdGenerator.java
├── Player.java
├── RegistrationWindow.java
├── Registry.java
├── Server_Impl.java
├── StartWindow.java
```

## 🚀 How to Run

Follow these steps to run the project:

### 1. Compile all files
You can compile everything in `GAME/` using:

```bash
javac GAME/*.java
```

### 2. Start the RMI Registry
Open a terminal and run:

```bash
java GAME.Registry
```

> 📝 This will start the RMI registry on port `2000`.

### 3. Start the Server
In another terminal:

```bash
java GAME.Server_Impl
```

This will bind the server object to `rmi://127.0.0.1:2000/GameServer`.

### 4. Launch the Client
Finally, run the main window (GUI):

```bash
java GAME.StartWindow
```

## 🧑‍💻 Author

- **Racha Zayni**

## 📋 Requirements

- Java 8 or higher
- No external dependencies (standard Java RMI and Swing)

## 📌 Note

- Make sure to run the registry and server **before launching the client GUI**.
- All communication is handled over **localhost (127.0.0.1)** by default.

## 📄 License

This project is provided for educational purposes and does not include a specific license.

---
