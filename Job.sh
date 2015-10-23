#!/bin/bash

SHELL_NAME=$(basename $0)
APP_NAME=$(basename $0 .sh)
APP_DIR=$(cd $(dirname $0) && pwd)
JAR_NAME="spark-smalljob-assembly-1.0.0.jar"

# Functions
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

usage() {
  echo "Usage: ${SHELL_NAME} <in> <out>"
  echo "  in     : /data/test/*.json | hdfs://.."
  echo "  out    : /data/test/*.json | hdfs://.."
}

echo_date() {
  echo "$(date +'%Y-%m-%d %H:%M:%S') $@"
}

# Main
# # # # # # # # # # # # # # # # # # # # # # # # # # # # # #

if [ $# -lt 1 ]; then
  usage
  exit 1
fi

IN=$1
OUT=$2

echo_date "[BEGIN] ${SHELL_NAME}"
BEGIN_SEC=$(date +%s)

cd ${APP_DIR}
jar uf ${JAR_NAME} *.conf

# 10 min = 600 sec
timeout 600 /opt/spark/bin/spark-submit \
  --master "mesos://`cat /etc/mesos/zk`" \
  --conf "spark.driver.extraJavaOptions=-Dconfig.resource=application.conf" \
  --conf "spark.executor.extraJavaOptions=-Dconfig.resource=application.conf" \
  --class com.inkenkun.x1.spark.smalljob.${APP_NAME} \
  ${APP_DIR}/${JAR_NAME} \
  ${IN} ${OUT}

RESULT=$?
TOTAL_SEC=$(($(date +%s) - BEGIN_SEC))

case ${RESULT} in
  0   ) echo_date "[END] ${SHELL_NAME} ( ${TOTAL_SEC} sec. )" ;;
  124 ) echo_date "[FATAL ERROR] Timeout ${SHELL_NAME} ( ${TOTAL_SEC} sec. )" ;;
  *   ) echo_date "[FATAL ERROR] Failed ${SHELL_NAME} ( ${TOTAL_SEC} sec. )" ;;
esac

exit ${RESULT}