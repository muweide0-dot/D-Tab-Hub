# NexusTiers Tagger

Client-side Fabric mod for Minecraft **1.21.11**. It mirrors the useful part of
TierTagger, but reads verified ranks from the NexusTiers tierlist:

- `[HT1] PlayerName` above players
- `[HT1] PlayerName` in the player list
- asynchronous requests with a local cache, so rendering never waits on the API
- no credentials or API keys required

## Build

```bash
gradle build
```

The built jar is written to `build/libs/`.

## Configuration

After the first launch, edit:

`config/nexus-tier-tagger.json`

Example:

```json
{
  "enabled": true,
  "showInNametags": true,
  "showInPlayerList": true,
  "kit": "dtab",
  "apiUrl": "https://nexustierss-production.up.railway.app/api",
  "cacheMinutes": 10
}
```

The tagger is configured for the **D Tab** kit only (`"kit": "dtab"`).
