CREATE TABLE `cfg` (
  `id` int(11) NOT NULL,
  `key` varchar(64) NOT NULL,
  `type` varchar(16) NOT NULL,
  `value` varchar(512) DEFAULT NULL,
  `file` blob,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_UNIQUE` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;