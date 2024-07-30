CREATE TABLE `mountain` (
  `mountain_id` INT NOT NULL,
  `mountain_code` INT NOT NULL,
  `mountain_name` VARCHAR(10) NOT NULL,
  `mountain_location` VARCHAR(255) NOT NULL,
  `mountain_height` DOUBLE(5,1) NOT NULL,
  `mountain_description` VARCHAR(1000) NOT NULL,
  `mountain_img` VARCHAR(255) NULL,
  `mountain_weather` ENUM('SPRING', 'SUMMER', 'FALL', 'WINTER', 'ALL') NOT NULL,
	`mountain_lat` DOUBLE(9,6) NOT NULL,
	`mountain_lon` DOUBLE(9,6) NOT NULL,
  PRIMARY KEY (`mountain_id`),
  UNIQUE INDEX `mountain_code_UNIQUE` (`mountain_code` ASC) VISIBLE);

CREATE TABLE `mountain_spot` (
  `mountain_spot_id` INT NOT NULL AUTO_INCREMENT,
  `mountain_spot_lat` DOUBLE(9,6) NOT NULL,
  `mountain_spot_lon` DOUBLE(9,6) NOT NULL,
  `mountain_spot_type_id` CHAR(2) NULL,
  `mountain_spot_type` VARCHAR(45) NULL,
  `mountain_spot_detail` VARCHAR(255) NULL,
  `mountain_code` INT NOT NULL,
  PRIMARY KEY (`mountain_spot_id`),
  CONSTRAINT `fk_mountain_spot_mountain`
  FOREIGN KEY (`mountain_code`)
  REFERENCES `mountain` (`mountain_code`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE `test`.`mountain_spot` (
  `mountain_spot_id` INT NOT NULL AUTO_INCREMENT,
  `mountain_spot_lat` DOUBLE(9,6) NOT NULL,
  `mountain_spot_lon` DOUBLE(9,6) NOT NULL,
  `mountain_spot_type_id` CHAR(2) NULL,
  `mountain_spot_type` VARCHAR(45) NULL,
  `mountain_spot_detail` VARCHAR(255) NULL,
  `mountain_code` INT NOT NULL,
  PRIMARY KEY (`mountain_spot_id`),
  CONSTRAINT `fk_mountain_spot_mountain`
  FOREIGN KEY (`mountain_code`)
  REFERENCES `test`.`mountain` (`mountain_code`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
 