#!/usr/bin/env sh

psql postgres://relay_test:relay_test@localhost:5433/relay_test < ../backend/src/test/resources/setup.sql
