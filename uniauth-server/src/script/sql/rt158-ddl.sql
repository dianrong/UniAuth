-- -----------------------------------------------------
-- Table `api_caller_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_caller_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT comment '主键id',
  `domain_id` INT(11) NOT NULL comment '对应的域id',
  `password` VARCHAR(64) NOT NULL comment '密码',
  `status` TINYINT(3) NOT NULL comment '状态:0:启用,1:禁用' DEFAULT 0,
  `comment` VARCHAR(120) comment 'comment',
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
  	PRIMARY KEY (`id`) comment '主键' ,
  	FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

  -- -----------------------------------------------------
-- Table `api_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `api_permission` (
  `id` INT(11) NOT NULL AUTO_INCREMENT comment '主键id',
  `uri` VARCHAR(80) NOT NULL comment '权限对应的请求uri',
  `status` TINYINT(3) NOT NULL comment '状态:0:启用,1:禁用' DEFAULT 0,
  `method` VARCHAR(10) NOT NULL comment '包括:GET,POST,ALL三种',
  `type` TINYINT(3) NOT NULL comment '权限类型:0:私有权限类型(需要主动分配),1:共有权限类型(默认每个域都有)' DEFAULT 0,
  `comment` VARCHAR(120) comment 'comment',
  `create_date` DATETIME  comment '创建时间',
  `last_update` DATETIME  comment '最近更新时间',
  	PRIMARY KEY (`id`) comment '主键' 
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

  -- -----------------------------------------------------
-- Table `caller_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caller_permission` ( 
  `api_caller_id` INT(11) NOT NULL comment 'api访问域的id',
  `api_permission_id` INT(11) NOT NULL comment '访问权限id',
  	UNIQUE KEY `unique_caller_permission` (`api_caller_id`,`api_permission_id`) ,
  	FOREIGN KEY (`api_caller_id`) REFERENCES `api_caller_info` (`id`),
  	FOREIGN KEY (`api_permission_id`) REFERENCES `api_permission` (`id`)
  )ENGINE = InnoDB DEFAULT CHARSET=utf8;

-- audit table update
alter table audit add request_domain_code varchar(64) default '' comment '请求访问者的域名code';
