#!/bin/sh
# Gradle wrapper - run 'gradle wrapper' to generate full wrapper if Gradle is installed
if command -v gradle >/dev/null 2>&1; then
    exec gradle "$@"
else
    echo "Gradle not found. Install Gradle or run from IDE. To generate full wrapper: gradle wrapper"
    exit 1
fi
