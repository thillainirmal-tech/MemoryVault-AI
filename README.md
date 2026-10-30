# MemoryVault AI

Persistent, privacy-aware conversational memory for Java applications.

[![CI](https://github.com/thillainirmal-tech/MemoryVault-AI/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/MemoryVault-AI/actions/workflows/ci.yml)

## Features

- durable PostgreSQL conversation ledger with Flyway migrations
- AES-256-GCM authenticated encryption before content reaches the database
- email, phone and secret redaction before persistence
- per-turn expiry and automatic cleanup
- rolling context window with older-turn compression
- multimodal content-type metadata
- explicit conversation deletion
- responsive inspection dashboard and REST API

Set `DATABASE_PASSWORD` and a base64-encoded 32-byte `MEMORY_ENCRYPTION_KEY`, then run `docker compose up --build`. Verify with `mvn verify`.

This service stores no embeddings and requires no model API key. Content is redacted and then encrypted; expiry and explicit deletion remain authoritative database operations. Deploy behind an identity-aware gateway and managed secret store for tenant authentication and key rotation.

Apache-2.0. Use synthetic content only.
