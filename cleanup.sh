#!/bin/sh
#
# cleanup.sh — remove transient scratch / build files for JBlackJack.
#
#   ./cleanup.sh          remove temp scratch files + macOS cruft        (safe)
#   ./cleanup.sh --deep   ALSO remove bin/ (compiled output) and ~/.JBlackJack
#
# Nothing under src/ is ever touched.

set -u
PROJECT="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
TMP=/tmp

echo "JBlackJack cleanup — project: $PROJECT"

# 1) macOS Finder artifacts inside the project (they regenerate on their own).
find "$PROJECT" -name '.DS_Store' -type f -delete 2>/dev/null || true
find "$PROJECT" -maxdepth 1 -name 'Icon*' -size 0 -delete 2>/dev/null || true

# 1b) build / JVM scratch that can land in the project root.
rm -f "$PROJECT"/sources.txt "$PROJECT"/.sources.tmp \
      "$PROJECT"/hs_err_pid*.log "$PROJECT"/replay_pid*.log 2>/dev/null || true

# 2) scratch files left under /tmp by build / test runs.
rm -rf \
  "$TMP"/jbj_* "$TMP"/jbj_sources.txt "$TMP"/sources.txt \
  "$TMP"/accounts.bak "$TMP"/c.log "$TMP"/final.log "$TMP"/binbuild.log \
  "$TMP"/f.out "$TMP"/f.err "$TMP"/f2.out "$TMP"/f2.err \
  "$TMP"/r.out "$TMP"/r.err "$TMP"/run.out "$TMP"/run.err \
  "$TMP"/CurSize.* "$TMP"/PathChk.* "$TMP"/Res.* \
  2>/dev/null || true

echo "  - removed temp scratch files"

# 3) optional deep clean: compiled output + the old (now unused) home accounts file.
if [ "${1:-}" = "--deep" ]; then
  rm -rf "$PROJECT/bin" "$HOME/.JBlackJack"
  echo "  - deep: removed bin/ and ~/.JBlackJack  (recompile bin/ before running again)"
fi

echo "Done."