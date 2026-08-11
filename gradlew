#!/usr/bin/env sh
set -eu

GRADLE_VERSION="8.13"
BASE_DIR="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/dists/gradle-$GRADLE_VERSION-bin/manual"
ZIP_FILE="$BASE_DIR/gradle-$GRADLE_VERSION-bin.zip"
GRADLE_HOME="$BASE_DIR/gradle-$GRADLE_VERSION"
URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$GRADLE_HOME/bin/gradle" ]; then
  mkdir -p "$BASE_DIR"
  if [ ! -f "$ZIP_FILE" ]; then
    echo "Download di Gradle $GRADLE_VERSION..."
    if command -v curl >/dev/null 2>&1; then
      curl -L --fail --retry 3 -o "$ZIP_FILE" "$URL"
    elif command -v wget >/dev/null 2>&1; then
      wget -O "$ZIP_FILE" "$URL"
    else
      echo "Serve curl oppure wget per scaricare Gradle." >&2
      exit 1
    fi
  fi
  rm -rf "$GRADLE_HOME"
  unzip -q "$ZIP_FILE" -d "$BASE_DIR"
fi

exec "$GRADLE_HOME/bin/gradle" "$@"
