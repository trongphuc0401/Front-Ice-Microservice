#!/bin/bash

if docker network inspect microservice-net >/dev/null 2>&1; then
  echo "🌐 Network 'microservice-net' đã tồn tại"
else
  echo "🔧 Đang tạo network 'microservice-net'..."
  docker network create microservice-net
  echo "✅ Network 'microservice-net' đã được tạo"
fi
