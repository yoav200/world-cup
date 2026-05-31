# FIFA World Cup Betting Application

A web application for managing and tracking bets on FIFA World Cup matches. Users can register, predict match scores, guess knockout stage qualifiers, and compete on a live leaderboard.

## Features

- **Match Betting** — predict scores for all group stage and knockout matches
- **Qualifier Betting** — guess which teams will advance through each knockout round, plus the overall winner
- **Points System** — earn points for correct winners, exact scores, and qualifier predictions
- **Live Ranking** — real-time leaderboard tracking all participants
- **Group Standings** — automatic group table calculations with tiebreaker support
- **Knockout Bracket** — visual bracket showing Round of 16 through Final
- **Admin Panel** — enter real match results, manage accounts
- **Email Notifications** — registration confirmation with token-based verification

## Tech Stack

- **Backend** — Spring Boot 3.5, Java 21, Spring Security, Spring Data JPA
- **Database** — PostgreSQL with Flyway migrations
- **Frontend** — AngularJS 1.x (single-page application)
- **API Docs** — SpringDoc OpenAPI (Swagger UI)
- **Build** — Maven

## Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+

## Configuration

Set the following environment variables before running:

| Variable              | Description                     |
|-----------------------|---------------------------------|
| `DATABASE_URL`        | PostgreSQL JDBC URL             |
| `DATABASE_USER`       | Database username               |
| `DATABASE_PASSWORD`   | Database password               |
| `EMAIL_SERVER_PASSWORD` | SMTP password for notifications |

## Installation & Run

```bash
mvn clean install
mvn spring-boot:run
```

The app starts on `http://localhost:8050` by default.

## Local Development (H2 Profile)

For quick local development without PostgreSQL, use the `local` profile which runs an H2 in-memory database:

```bash
set SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

This will:
- Start an H2 in-memory database with PostgreSQL compatibility mode
- Run H2-specific Flyway migrations from `src/main/resources/db/h2/`
- Populate the database with all 2026 World Cup data (48 teams, 104 matches, 168 bets)
- Enable the H2 console at `http://localhost:8050/h2-console` (JDBC URL: `jdbc:h2:mem:worldcup`, user: `sa`, no password)

### Test Accounts

| Email            | Password   | Role  |
|------------------|------------|-------|
| `admin@test.com` | `password` | Admin |
| `user@test.com`  | `password` | User  |
| `user2@test.com` | `password` | User  |

## Project Structure

```
src/main/java/com/ab/worldcup/
├── account/        # User accounts, authentication, roles
├── bet/            # Bet entities, types (match/qualifier), user bets
├── config/         # Security, web, and application configuration
├── email/          # Email sending service
├── events/         # Application startup event handlers
├── group/          # Group stage standings and tiebreaker logic
├── knockout/       # Knockout stage team resolution
├── match/          # Match, GroupMatch, KnockoutMatch entities
├── ranking/        # Points calculation and leaderboard
├── registration/   # User registration and email confirmation tokens
├── results/        # Match results, qualifiers, points config
├── team/           # Team entity and service
└── web/            # REST controllers and session management
```

## 2026 World Cup Data

Data sourced from [openfootball/worldcup.json](https://github.com/openfootball/worldcup.json/tree/master/2026).

### Tournament Structure

| Stage | Matches | Match Numbers | Dates |
|-------|---------|---------------|-------|
| Group stage | 72 | 1–72 | Jun 11 – Jun 27 |
| Round of 32 | 16 | 73–88 | Jun 28 – Jul 3 |
| Round of 16 | 8 | 89–96 | Jul 4 – Jul 7 |
| Quarter-finals | 4 | 97–100 | Jul 9 – Jul 11 |
| Semi-finals | 2 | 101–102 | Jul 14 – Jul 15 |
| Third place | 1 | — | Jul 18 |
| Final | 1 | — | Jul 19 |
| **Total** | **104** | | |

### Groups (A–L, 4 teams each, 48 teams total)

| Group | Teams |
|-------|-------|
| A | Mexico, South Africa, South Korea, Czech Republic |
| B | Canada, Bosnia & Herzegovina, Qatar, Switzerland |
| C | Brazil, Morocco, Haiti, Scotland |
| D | USA, Paraguay, Australia, Turkey |
| E | Germany, Curaçao, Ivory Coast, Ecuador |
| F | Netherlands, Japan, Sweden, Tunisia |
| G | Belgium, Egypt, Iran, New Zealand |
| H | Spain, Cape Verde, Saudi Arabia, Uruguay |
| I | France, Senegal, Iraq, Norway |
| J | Argentina, Algeria, Austria, Jordan |
| K | Portugal, DR Congo, Uzbekistan, Colombia |
| L | England, Croatia, Ghana, Panama |

### Knockout Bracket

**Round of 32** — 16 matches pairing group winners (1st), runners-up (2nd), and best 3rd-place teams:

| Match | Home | Away |
|-------|------|------|
| 73 | 2A | 2B |
| 74 | 1E | 3A/B/C/D/F |
| 75 | 1F | 2C |
| 76 | 1C | 2F |
| 77 | 1I | 3C/D/F/G/H |
| 78 | 2E | 2I |
| 79 | 1A | 3C/E/F/H/I |
| 80 | 1L | 3E/H/I/J/K |
| 81 | 1D | 3B/E/F/I/J |
| 82 | 1G | 3A/E/H/I/J |
| 83 | 2K | 2L |
| 84 | 1H | 2J |
| 85 | 1B | 3E/F/G/I/J |
| 86 | 1J | 2H |
| 87 | 1K | 3D/E/I/J/L |
| 88 | 2D | 2G |

**Round of 16** — Winners of R32 matches:

| Match | Home | Away |
|-------|------|------|
| 89 | W74 | W77 |
| 90 | W73 | W75 |
| 91 | W76 | W78 |
| 92 | W79 | W80 |
| 93 | W83 | W84 |
| 94 | W81 | W82 |
| 95 | W86 | W88 |
| 96 | W85 | W87 |

**Quarter-finals → Final:**

| Match | Home | Away |
|-------|------|------|
| QF 97 | W89 | W90 |
| QF 98 | W93 | W94 |
| QF 99 | W91 | W92 |
| QF 100 | W95 | W96 |
| SF 101 | W97 | W98 |
| SF 102 | W99 | W100 |
| 3rd place | L101 | L102 |
| Final | W101 | W102 |

## License

MIT License
