drop table if exists audit;

CREATE TABLE `audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `req_date` datetime DEFAULT NULL,
  `domain_id` int(11) default null,
  `req_ip` varchar(128) DEFAULT NULL,
  `req_uuid` varchar(128) DEFAULT NULL,
  `req_url` varchar(64) DEFAULT NULL,
  `req_seq` bigint(20) DEFAULT NULL,
  `req_class` varchar(64) DEFAULT NULL,
  `req_method` varchar(64) DEFAULT NULL,
  `req_success` tinyint(3) DEFAULT NULL,
  `req_exp` varchar(3072) DEFAULT NULL,
  `req_elapse` bigint(20) DEFAULT NULL,
  `req_param` varchar(256) DEFAULT NULL,
  `req_result` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
