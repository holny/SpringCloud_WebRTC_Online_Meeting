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


while getopts "d:c:h:p:g:t:u:w:" arg; do
  case $arg in
  d)
    DEPENDS=$OPTARG
    ;;
  c)
    CMD=$OPTARG
    ;;
  h)
    host=$OPTARG
    ;;
  p)
    port=$OPTARG
    ;;
  g)
    group=$OPTARG
    ;;
  t)
    tenant=$OPTARG
    ;;
  u)
    username=$OPTARG
    ;;
  w)
    password=$OPTARG
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
echo "start exec seata nacos config script"
echo $host $port $username $password

# Copyright 1999-2019 Seata.io Group.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at、
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#while getopts ":h:p:g:t:u:w:" opt; do
#  case $opt in
#  h)
#    host=$OPTARG
#    ;;
#  p)
#    port=$OPTARG
#    ;;
#  g)
#    group=$OPTARG
#    ;;
#  t)
#    tenant=$OPTARG
#    ;;
#  u)
#    username=$OPTARG
#    ;;
#  w)
#    password=$OPTARG
#    ;;
#  ?)
#    echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
#    exit 1
#    ;;
#  esac
#done

urlencode() {
  for i in `seq ${#1}`
  do
    char="${1:$i:1}"
    case $char in
    [a-zA-Z0-9.~_-]) echo $char ;;
    *) echo '%%%02X' "'$char" ;;
    esac
  done
}

if [[ -z ${host} ]]; then
  host=localhost
fi
if [[ -z ${port} ]]; then
  port=8848
fi
if [[ -z ${group} ]]; then
  group="SEATA_GROUP"
fi
if [[ -z ${tenant} ]]; then
  tenant=""
fi
if [[ -z ${username} ]]; then
  username=""
fi
if [[ -z ${password} ]]; then
  password=""
fi

nacosAddr=$host:$port
contentType="content-type:application/json;charset=UTF-8"

echo "set nacosAddr=$nacosAddr"
echo "set group=$group"

failCount=0
tempLog=$(mktemp -u)
addConfig() {
  echo $1 $2
  dataId=$1
  content=$2
  echo $dataId $content
  curl -X POST -H "${contentType}" "http://$nacosAddr/nacos/v1/cs/configs?dataId=$dataId&group=$group&content=$content&tenant=$tenant&username=$username&password=$password" >"${tempLog}" 2>/dev/null
  echo "log"
  echo ${tempLog}
  if [[ -z $(cat "${tempLog}") ]]; then
    echo " Please check the cluster status. "
    exit 1
  fi
  if [[ $(cat "${tempLog}") =~ "true" ]]; then
    echo "Set $1=$2 successfully "
  else
    echo "Set $1=$2 failure "
    failCount=$((failCount+1))
  fi
}

count=0
while read line
do
#  line=$line | sed s/[[:space:]]//g
  echo ${line}
  count=$((count+1))
  key=${line%%=*}
  value=${line#*=}
  echo "read-key=$key-value=$value"
  addConfig "${key}" "${value}"
done < '/usr/local/bin/nacos/config.txt';

echo "========================================================================="
echo " Complete initialization parameters,  total-count:$count ,  failure-count:$failCount "
echo "========================================================================="

if [[ ${failCount} -eq 0 ]]; then
  echo " Init nacos config finished, please start seata-server. "
else
  echo " init nacos config fail. "
fi
echo "done"
#避免执行完命令之后退出容器
tail -f /dev/null
