# Disaster Volunteer Matching App Backend 🌍🤝

This repository contains the **backend** for the Disaster Volunteer Matching App. The app connects volunteers with disaster relief efforts. It fetches real-time disaster data and uses user profiles to generate personalized recommendations with the help of a Large Language Model (LLM).

The **frontend** for this project is located in a separate repository: [Disaster Volunteer Frontend]([https://github.com/yourusername/disaster-volunteer-frontend](https://github.com/LipikaBaniya89/SEUI-FrontEnd.git)).

---

## Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- 
---

## Features
- **Real-time disaster data**: Fetches up-to-date disaster information from external APIs.
- **User profiles**: Volunteers can input skills, location, and availability.
- **Impact-based recommendations**: LLM generates personalized suggestions for tasks.
- **Program ranking**: Shows most and least impactful programs for each volunteer.
- **Secure authentication**: JWT / OAuth2 based login and registration.

---

## Tech Stack
- **Frontend:** .NET (Blazor or Razor Pages) — see frontend repo
- **Backend:** Java Spring Boot with Maven
- **Database:** PostgreSQL / MySQL / H2 (for development)
- **LLM Integration:** OpenAI API / GPT-5
- **External APIs:** Disaster data providers (e.g., FEMA, ReliefWeb)
- **Authentication:** JWT / OAuth2

---

## Installation

### Backend (Spring Boot)
1. Clone this repository:
use  mvn spring-boot:run to run once on the drive

### Frontend
1. Since frontend is hosted in different repo you need to clone https://github.com/LipikaBaniya89/SEUI-FrontEnd.git
use dotnet run to run once on the drive

