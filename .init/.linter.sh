#!/bin/bash
cd /home/kavia/workspace/code-generation/ticket-data-metrics-service-91884-91893/devx_dashboard_backend
./gradlew checkstyleMain
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

