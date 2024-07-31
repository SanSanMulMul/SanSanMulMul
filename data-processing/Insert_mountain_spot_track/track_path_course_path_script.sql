-- -----------------------------------------------------
-- 구간(track), 코스(course) 테이블과 mountain테이블 연관지을 때
-- mountain_id 말고 mountain_code랑 FK관계맺음
-- 참고 : mountain테이블 PK값은 mountain_id(AI) , UNIQUE KEY로 mountain_code 설정함
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `sansanmulmul`.`track`
-- -----------------------------------------------------
CREATE TABLE `sansanmulmul`.`track` (
  `track_id` INT NOT NULL AUTO_INCREMENT,
  `track_no` INT NULL,
  `track_name` VARCHAR(50) NULL,
  `track_length` DOUBLE(5,2) NULL,
  `track_level` ENUM('EASY', 'MEDIUM', 'HARD') NULL,
  `track_uptime` INT NULL,
  `track_downtime` INT NULL,
  `track_best_path` BIGINT NULL,
  `mountain_code` INT NULL,
  PRIMARY KEY (`track_id`),
  INDEX `fk_track_mountain_idx` (`mountain_code` ASC) VISIBLE,
  CONSTRAINT `fk_track_mountain`
    FOREIGN KEY (`mountain_code`)
    REFERENCES `sansanmulmul`.`mountain` (`mountain_code`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
-- ENGINE = InnoDB
-- AUTO_INCREMENT = 222
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sansanmulmul`.`track_path`
-- -----------------------------------------------------
CREATE TABLE `sansanmulmul`.`track_path` (
  `track_path_id` BIGINT NOT NULL AUTO_INCREMENT,
  `track_path_lat` DOUBLE(9,6) NOT NULL,
  `track_path_lon` DOUBLE(9,6) NOT NULL,
  `track_id` INT NULL,
  PRIMARY KEY (`track_path_id`),
  INDEX `fk_track_path_track_idx` (`track_id` ASC) VISIBLE,
  CONSTRAINT `fk_track_path_track`
    FOREIGN KEY (`track_id`)
    REFERENCES `sansanmulmul`.`track` (`track_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
-- ENGINE = InnoDB
-- AUTO_INCREMENT = 8579
-- DEFAULT CHARACTER SET = utf8mb4
-- COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sansanmulmul`.`course`
-- -----------------------------------------------------
CREATE TABLE `sansanmulmul`.`course` (
  `course_id` BIGINT NOT NULL,
  `course_name` VARCHAR(100) NULL,
  `course_length` DOUBLE(10,2) NULL,
  `course_uptime` INT NULL,
  `course_downtime` INT NULL,
  `course_level` ENUM('EASY', 'MEDIUM', 'HARD') NULL,
  `course_best_track_id` INT UNSIGNED NULL,
  `mountain_code` INT NULL,
  PRIMARY KEY (`course_id`),
  INDEX `fk_course_mountain_idx` (`mountain_code` ASC) VISIBLE,
  CONSTRAINT `fk_course_mountain`
    FOREIGN KEY (`mountain_code`)
    REFERENCES `sansanmulmul`.`mountain` (`mountain_code`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
-- -----------------------------------------------------
-- Table `sansanmulmul`.`course_tracks`
-- -----------------------------------------------------
CREATE TABLE `sansanmulmul`.`course_tracks` (
  `course_tracks_id` INT NOT NULL AUTO_INCREMENT,
  `course_tracks_sequence` INT NOT NULL,
  `track_id` INT NOT NULL,
  `course_id` BIGINT NOT NULL,
  PRIMARY KEY (`course_tracks_id`),
  INDEX `fk_course_tracks_track_idx` (`track_id` ASC) VISIBLE,
  INDEX `fk_course_tracks_course_idx` (`course_id` ASC) VISIBLE,
  CONSTRAINT `fk_course_tracks_track`
    FOREIGN KEY (`track_id`)
    REFERENCES `sansanmulmul`.`track` (`track_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_course_tracks_course`
    FOREIGN KEY (`course_id`)
    REFERENCES `sansanmulmul`.`course` (`course_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);