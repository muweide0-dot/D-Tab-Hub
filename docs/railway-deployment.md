# Railway deployment

NexusTiers runs on Railway as one always-on service. The service serves the Minecraft website, the `/api` endpoints, and the Discord bot from the same Node process.

## Required Railway variables

Set these in the Railway service's Variables tab:

- `DATABASE_URL` — PostgreSQL connection string
- `DISCORD_BOT_TOKEN` — Discord bot token
- `DISCORD_GUILD_ID` — Discord server ID

Railway provides `PORT` automatically. Do not hardcode it.

## Optional role and channel variables

The bot can use role IDs when names are not unique:

- `DISCORD_TESTER_ROLE_ID`
- `MEMBER_ROLE_ID`
- `BOOSTER_ROLE_ID`
- `RESTRICTED_ROLE_ID`
- `MANAGER_ROLE_ID`
- `ADMIN_ROLE_ID`
- `MOD_ROLE_ID`
- `HELPER_ROLE_ID`

Ticket category and panel overrides:

- `SUPPORT_CATEGORY_ID`
- `REPORTS_CATEGORY_ID`
- `STAFF_REPORTS_CATEGORY_ID`
- `TICKET_PANEL_CHANNEL_ID`

## Build and runtime

Railway uses `nixpacks.toml` to:

1. Typecheck shared libraries.
2. Build the Minecraft frontend into `artifacts/minecraft-queue/dist/public`.
3. Bundle the API server and Discord gateway.
4. Start `artifacts/api-server/dist/index.mjs`.

The health check is `GET /api/healthz`.

Use an always-on Railway service for this project. The Discord gateway must stay connected continuously; a sleep-on-idle deployment will disconnect the bot.