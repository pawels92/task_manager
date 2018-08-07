-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `task_manager` DEFAULT CHARACTER SET utf8 ;
USE `task_manager` ;

-- -----------------------------------------------------
-- Table `task_manager`.`course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`course` (
  `ID_k` INT(11) NOT NULL AUTO_INCREMENT,
  `course_name` VARCHAR(100) NOT NULL,
  `course_agenda` TEXT NOT NULL,
  `course_category` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID_k`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task_manager`.`edition`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`edition` (
  `date` DATE NOT NULL,
  `course_ID_k` INT(11) NOT NULL,
  `course_ID_k1` INT(11) NOT NULL,
  INDEX `fk_edition_course1_idx` (`course_ID_k` ASC),
  CONSTRAINT `fk_edition_course1`
    FOREIGN KEY (`course_ID_k`)
    REFERENCES `course` (`ID_k`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_edition_course2`
    FOREIGN KEY (`course_ID_k1`)
    REFERENCES `task_manager`.`course` (`ID_k`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task_manager`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`users` (
  `ID_user` INT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `lastname` VARCHAR(45) NOT NULL,
  `gender` ENUM('Kobieta', 'Mężczyzna') NOT NULL,
  `permission` TINYINT(4) NOT NULL,
  PRIMARY KEY (`ID_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task_manager`.`submission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task_manager`.`submission` (
  `users_ID_user` INT(11) NOT NULL,
  `course_ID_k` INT(11) NOT NULL,
  `feed` ENUM('normalne', 'wegetarianskie', 'bezglutenowe') NOT NULL,
  `fv_decsision` TINYINT NOT NULL,
  `fv_details` TEXT NULL,
  `confirm` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`users_ID_user`, `course_ID_k`),
  INDEX `fk_users_has_course_course1_idx` (`course_ID_k` ASC),
  INDEX `fk_users_has_course_users1_idx` (`users_ID_user` ASC),
  CONSTRAINT `fk_users_has_course_users1`
    FOREIGN KEY (`users_ID_user`)
    REFERENCES `task_manager`.`users` (`ID_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_course_course1`
    FOREIGN KEY (`course_ID_k`)
    REFERENCES `task_manager`.`course` (`ID_k`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

SELECT course_name,course_agenda,course_category, date FROM course
join edition on (course.ID_k = edition.course_ID_k1);