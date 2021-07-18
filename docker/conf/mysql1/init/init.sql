/*
 * 第一步初始, 调用此脚本恢复数据库(MySQL)
 */
source /opt/sql/base.sql;
source /opt/sql/nacos-mysql.sql;
source /opt/sql/seata-mysql.sql;
source /opt/sql/seata_config_info.sql;
source /opt/sql/spring-security-oauth2-client-details.sql;
/*
* 以上是项目框架基础数据库, nacos、seata, oauth2所必须的的，先执行搭建好架构。
* 以下时项目业务数据库。july_v1里。
 */
source /opt/sql/user.sql;
source /opt/sql/category.sql;
source /opt/sql/consult.sql;
source /opt/sql/container.sql;
source /opt/sql/expert.sql;
source /opt/sql/file_helper.sql;
source /opt/sql/message.sql;
source /opt/sql/pay.sql;
source /opt/sql/relation.sql;
source /opt/sql/series.sql;
source /opt/sql/tag.sql;
source /opt/sql/video.sql;

