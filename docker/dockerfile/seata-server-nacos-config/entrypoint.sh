#!/bin/bash
#set -x
#******************************************************************************
# @file    : watch_port_nacos_config_dash.sh
# @author  : simon
# @date    : 2018-08-28 15:18:43
#
# @brief   : entry point for manage service start order
# history  : init
#******************************************************************************

: ${SLEEP_SECOND:=2}

wait_for() {
  echo Waiting for $1 to listen on $2...
  while ! nc -z $1 $2; do
    echo waiting...
    sleep $SLEEP_SECOND
  done
}


while getopts "d:c:" arg; do
  case $arg in
  d)
    DEPENDS=$OPTARG
    ;;
  c)
    CMD=$OPTARG
    ;;
  ?)
    echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
    exit 1
    ;;
  esac
done

for var in ${DEPENDS//,/ }; do
  watching_host=${var%:*}
  watching_port=${var#*:}
  wait_for $watching_host $watching_port
done

echo "done"
#避免执行完命令之后退出容器
tail -f /dev/null
