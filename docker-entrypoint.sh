#!/bin/bash
set -e

CONFIG_DIR=/usr/local/tomcat/webapps/ROOT/WEB-INF/classes
CONFIG_FILE=$CONFIG_DIR/config.properties

# ── 1. Start H2 in server mode (Spec Section 10 reference setup step 5) ──
echo "[entrypoint] Starting H2 TCP server..."
java -cp /opt/h2/h2.jar org.h2.tools.Server \
  -tcp -tcpAllowOthers -tcpPort 9092 \
  -baseDir /opt/krishnamart/data \
  -ifNotExists &
H2_PID=$!

# Wait for H2 to be ready
sleep 3
echo "[entrypoint] H2 server started (PID $H2_PID)"

# ── 2. Write config.properties from env vars ──
# Spec Section 9: credentials must not be in version control
echo "[entrypoint] Writing config.properties..."
mkdir -p $CONFIG_DIR
cat > $CONFIG_FILE <<PROPS
db.url=${DB_URL}
db.user=${DB_USER}
db.password=${DB_PASSWORD}
db.pool.maxSize=${DB_POOL_MAX_SIZE}
db.pool.minIdle=${DB_POOL_MIN_IDLE}
ai.chatbot.provider=${AI_CHATBOT_PROVIDER}
ai.chatbot.apiKey=${AI_CHATBOT_API_KEY}
PROPS
echo "[entrypoint] config.properties written."

# ── 3. Start Tomcat (Spec Section 3: Tomcat 9.0.x) ──
echo "[entrypoint] Starting Tomcat..."
exec catalina.sh run
