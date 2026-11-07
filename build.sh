#!/usr/bin/env bash
set -euo pipefail

cd -- "$(dirname -- "${BASH_SOURCE[0]}")"

# Respect JAVA_HOME; otherwise prefer the JDK 17 installed on this machine.
if [[ -z "${JAVA_HOME:-}" && -x "$HOME/.jdks/jbr-17.0.12/bin/javac" ]]; then
    export JAVA_HOME="$HOME/.jdks/jbr-17.0.12"
fi

./gradlew "$@" :app:assembleRetroarchRelease :app:assembleRetroxRelease

printf '\nUnsigned release APKs:\n'
printf '  %s\n' \
    "$PWD/app/build/outputs/apk/retroarch/release/app-retroarch-release-unsigned.apk" \
    "$PWD/app/build/outputs/apk/retrox/release/app-retrox-release-unsigned.apk"
