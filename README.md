# Multi-Client Chat Application in Java

A terminal-based multi-client chat application built with **core Java** — no frameworks, no external libraries. Clients connect to a central server over **TCP sockets** and can broadcast messages to all connected users in real time.

## Concepts Demonstrated

- **TCP socket programming** — `ServerSocket` and `Socket`
- **Multithreading** — one thread per client using `Runnable`
- **OOP** — encapsulation, single responsibility, clean class separation
- **Synchronized broadcasting** — thread-safe message delivery to all clients

## Project Structure

```
chat-app/
├── src/main/java/chat/
│   ├── Server.java         # Accepts connections, manages client list
│   ├── ClientHandler.java  # Handles one client per thread
│   └── Client.java         # Terminal client with background read thread
└── README.md
```

## Requirements

- Java 11 or higher
- No external dependencies

## How to Run

Open **three terminal windows**.

### Step 1 — Compile

```bash
cd chat-app
javac -d out src/main/java/chat/*.java
```

### Step 2 — Start the server (Terminal 1)

```bash
java -cp out chat.Server
```

Output:
```
Chat server started on port 12345
```

### Step 3 — Connect client 1 (Terminal 2)

```bash
java -cp out chat.Client
```

Enter a username when prompted (e.g. `alice`).

### Step 4 — Connect client 2 (Terminal 3)

```bash
java -cp out chat.Client
```

Enter a username (e.g. `bob`).

Now type messages in either terminal — they appear instantly on all connected clients.

## Example Session

**Terminal 2 (alice):**
```
Connecting to chat server at localhost:12345...
Connected! Type /quit to exit.

Enter your username:
alice
[Server] alice joined the chat!
[Server] bob joined the chat!
hello bob!
[bob]: hey alice!
```

**Terminal 3 (bob):**
```
Connecting to chat server at localhost:12345...
Connected! Type /quit to exit.

Enter your username:
bob
[Server] bob joined the chat!
[alice]: hello bob!
hey alice!
```

## Commands

| Command | Description |
|---------|-------------|
| (any text) | Broadcast message to all clients |
| `/quit` | Disconnect from the server |
