# SeaBattle ⚓
<p align="center">
  <img src="https://img.shields.io/badge/status-active-success.svg" />
  <img src="https://img.shields.io/badge/platform-Android-green.svg" />
  <img src="https://img.shields.io/badge/Kotlin-100%25-purple.svg" />
</p>

---

## 🎮 About

**SeaBattle** is a native Android application that brings the classic Battleship game online with real-time multiplayer, competitive rankings, and social features. Built as a Master's thesis project at Universidad Pontificia de Salamanca, it demonstrates modern mobile architecture and Firebase integration.

---

## 📸 Screenshots
| Login | Main | Ranking | User Info |
|:-----:|:-----:|:------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/25229070-c909-4fad-b063-bc6bbe409857" width="180"/> | <img src="https://github.com/user-attachments/assets/ccb1fd72-a3e0-4a9f-bbc1-8796f637165b" width="180"/> | <img src="https://github.com/user-attachments/assets/5a94e906-a58e-462d-add5-899e2ebc8f8a" width="180"/> | <img src="https://github.com/user-attachments/assets/175881df-317e-4859-b3d0-531746e32d02" width="180"/>

| Waiting Player | Battle Player 1 |Battle Player 2 | GameOver |
|:-----:|:-----:|:------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/7948f349-cfc4-48bb-ae10-7541cbd5dcf8" width="180"/> | <img src="https://github.com/user-attachments/assets/1fceacc4-db72-4bf7-b074-f20d5a5bcb00" width="180"/> | <img src="https://github.com/user-attachments/assets/1bafff7c-2dac-4d25-b1df-1340759c72fb" width="180"/> | <img src="https://github.com/user-attachments/assets/08c95e61-4a2f-4304-98f4-037fdaa67ce4" width="180"/>


## ✨ Key Features

- **Real-time Multiplayer** - Synchronized online matches with instant feedback
- **ELO Ranking System** - Competitive scoring with global leaderboard
- **Social Gaming** - Send invitations, browse available games, track match history
- **Smart Connectivity** - Presence detection, reconnection support, AFK timeout
- **Multiple Auth Methods** - Email/password or Google Sign-In
- **Modern UI** - Built entirely with Jetpack Compose

---

## 🏗️ Technical Stack

### Frontend
- Kotlin + Jetpack Compose
- MVVM + Clean Architecture
- Koin dependency injection

### Backend
- Firebase Authentication
- Cloud Firestore (main database)
- Realtime Database (presence detection)
- Cloud Functions (game logic & scoring)

---

## 🎯 Game Flow

1. **Create/Join** - Start a new game or browse available matches
2. **Confirm** - Both players ready up
3. **Battle** - Take turns firing at opponent's 10x10 grid
4. **Victory** - Sink all enemy ships to win and gain ELO points

---

## 🔮 Future Enhancements

- Matchmaking algorithm
- AI opponent mode
- In-game chat
- Friends system
- Tournament mode

---

## 📱 Requirements

- Android 12.0+ (API 31)
- Internet connection
- Firebase project for deployment

---
