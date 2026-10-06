FROM node:22-bookworm-slim

WORKDIR /app

RUN corepack enable && corepack prepare pnpm@10.26.1 --activate \
  && apt-get update \
  && apt-get install -y --no-install-recommends patch unzip \
  && rm -rf /var/lib/apt/lists/*

COPY NexusTiers-ticket-system-complete.zip /tmp/nexustiers.zip
COPY scripts/dtab-requested.patch /tmp/dtab-requested.patch

RUN unzip -q /tmp/nexustiers.zip -d /app \
  && patch --batch --forward -p1 -d /app -i /tmp/dtab-requested.patch \
  && rm /tmp/nexustiers.zip /tmp/dtab-requested.patch \
  && pnpm install --frozen-lockfile \
  && pnpm run typecheck:libs \
  && PORT=4173 BASE_PATH=/ pnpm --filter @workspace/minecraft-queue run build \
  && pnpm --filter @workspace/api-server run build

ENV NODE_ENV=production

EXPOSE 8080

CMD ["node", "--enable-source-maps", "artifacts/api-server/dist/index.mjs"]
