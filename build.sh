#!/bin/sh
#
# build.sh — compile JBlackJack AND stage its resources into bin/ (what Eclipse
# does automatically). Plain `javac` only writes .class files, so the images and
# sounds must be copied next to them or the game crashes on a missing resource.
#
#   ./build.sh           clean-build bin/  ->  run: java -cp bin view.JBlackJack
#
set -eu
PROJECT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
cd "$PROJECT"

# 1) fresh output dir
rm -rf bin && mkdir bin

# 2) compile every source into bin/
find src -name '*.java' > .sources.tmp
javac --release 17 -d bin @.sources.tmp
rm -f .sources.tmp

# 3) copy non-Java resources (img/, sounds/, accounts.txt, …) to the classpath root
( cd src && find . -type f ! -name '*.java' -exec sh -c \
    'for f; do mkdir -p "../bin/$(dirname "$f")"; cp "$f" "../bin/$f"; done' _ {} + )

classes=$(find bin -name '*.class' | wc -l | tr -d ' ')
assets=$(find bin -type f ! -name '*.class' | wc -l | tr -d ' ')
echo "Build complete: $classes classes + $assets assets in bin/"
echo "Run:  java -cp bin view.JBlackJack"