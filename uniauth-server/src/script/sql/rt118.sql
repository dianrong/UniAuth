ALTER TABLE `grp` DROP INDEX `code_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `name_UNIQUE`;
ALTER TABLE `domain` DROP INDEX `display_name_UNIQUE`;
ALTER TABLE `role` DROP INDEX `domain_rolename_unique`;
ALTER TABLE `permission` DROP INDEX `perm_unique`;

ALTER TABLE `cfg` CHANGE COLUMN `id` `id` INT(11) NOT NULL AUTO_INCREMENT ;
