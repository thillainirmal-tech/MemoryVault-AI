# MemoryVault AI

Persistent, privacy-aware conversational memory for Java applications.

[![CI](https://github.com/thillainirmal-tech/MemoryVault-AI/actions/workflows/ci.yml/badge.svg)](https://github.com/thillainirmal-tech/MemoryVault-AI/actions/workflows/ci.yml)

## Features

- durable H2-backed conversation ledger
- email, phone and secret redaction before persistence
- per-turn expiry and automatic cleanup
- rolling context window with older-turn compression
- multimodal content-type metadata
- explicit conversation deletion
- responsive inspection dashboard and REST API

Run with `mvn spring-boot:run`, open http://localhost:8085, and verify with `mvn verify`.

This is an offline architectural demo; it stores no embeddings and requires no model API key. Production deployments should add encryption at rest, authenticated tenants and audited deletion.

Apache-2.0. Use synthetic content only.
