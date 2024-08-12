-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema RASBET
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema RASBET
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `RASBET` DEFAULT CHARACTER SET utf8 ;
USE `RASBET` ;

-- -----------------------------------------------------
-- Table `RASBET`.`Role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Role` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `rolename` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRole`),
  UNIQUE INDEX `rolename_UNIQUE` (`rolename` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`User` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `pwd` VARCHAR(45) NOT NULL,
  `address` VARCHAR(100) NOT NULL,
  `phone_number` CHAR(9) NOT NULL,
  `nif` CHAR(9) NOT NULL,
  `cc` CHAR(8) NOT NULL,
  `birth` DATE NOT NULL,
  `role` INT NOT NULL,
  `codeUser` CHAR(10) NULL,
  `token` CHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  UNIQUE INDEX `nif_UNIQUE` (`nif` ASC) VISIBLE,
  UNIQUE INDEX `cc_UNIQUE` (`cc` ASC) VISIBLE,
  UNIQUE INDEX `codeUser_UNIQUE` (`codeUser` ASC) VISIBLE,
  INDEX `fk_User_1_idx` (`role` ASC) VISIBLE,
  CONSTRAINT `fk_User_1`
    FOREIGN KEY (`role`)
    REFERENCES `RASBET`.`Role` (`idRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Wallet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Wallet` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Wallet` (
  `idUser` INT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `balance` FLOAT NOT NULL DEFAULT 0,
  `currency` VARCHAR(15) NOT NULL,
  `bonusBalance` FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `idUser_UNIQUE` (`idUser` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_Wallet_1`
    FOREIGN KEY (`idUser`)
    REFERENCES `RASBET`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_Wallet_2`
    FOREIGN KEY (`currency`)
    REFERENCES `RASBET`.`Currency` (`currencyName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Transaction` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idWallet` INT NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `amount` FLOAT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Transaction_1`
    FOREIGN KEY (`idWallet`)
    REFERENCES `RASBET`.`Wallet` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `RASBET`.`Bet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Bet` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Bet` (
  `idBet` INT NOT NULL AUTO_INCREMENT,
  `money` FLOAT NOT NULL,
  `possible_earnings` FLOAT NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `idUser` INT NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idBet`),
  INDEX `fk_Bet_1_idx` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_Bet_1`
    FOREIGN KEY (`idUser`)
    REFERENCES `RASBET`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Sport`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Sport` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Sport` (
  `idSport` INT NOT NULL AUTO_INCREMENT,
  `sport` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL,
  `draw` TINYINT NOT NULL,
  `n_participant` INT NOT NULL,
  PRIMARY KEY (`idSport`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Game` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Game` (
  `idGame` VARCHAR(50) NOT NULL,
  `id_sport` INT NOT NULL,
  `state` VARCHAR(10) NOT NULL,
  `outcome` VARCHAR(4) NULL,
  `date` DATETIME NOT NULL,
  `X` FLOAT NULL,
  `spec_X` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`idGame`),
  INDEX `fk_Game_1_idx` (`id_sport` ASC) VISIBLE,
  CONSTRAINT `fk_Game_1`
    FOREIGN KEY (`id_sport`)
    REFERENCES `RASBET`.`Sport` (`idSport`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `RASBET`.`Participant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Participant` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Participant` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `idGame` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `odd` FLOAT NOT NULL,
  `idx` INT NOT NULL,
  `spec_odd` FLOAT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_Part_1_idx` (`idGame` ASC) VISIBLE,
  CONSTRAINT `fk_Part_1`
    FOREIGN KEY (`idGame`)
    REFERENCES `RASBET`.`Game` (`idGame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Game_on_Bet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Game_on_Bet` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Game_on_Bet` (
  `idBet` INT NOT NULL,
  `idGame` VARCHAR(50) NOT NULL,
  `guess` VARCHAR(4) NOT NULL,
  `odd_guess` FLOAT NOT NULL,
  `checked` TINYINT NOT NULL,
  PRIMARY KEY (`idBet`, `idGame`),
  INDEX `fk_Bet_has_Game2_Game2_idx` (`idGame` ASC) VISIBLE,
  INDEX `fk_Bet_has_Game2_Bet2_idx` (`idBet` ASC) VISIBLE,
  CONSTRAINT `fk_Bet_has_Game2_Bet2`
    FOREIGN KEY (`idBet`)
    REFERENCES `RASBET`.`Bet` (`idBet`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Bet_has_Game2_Game2`
    FOREIGN KEY (`idGame`)
    REFERENCES `RASBET`.`Game` (`idGame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `RASBET`.`Promotion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Promotion` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Promotion` (
  `idPromo` VARCHAR(15) NOT NULL,
  `amount` FLOAT NOT NULL,
  `minVal` FLOAT NOT NULL DEFAULT 0,
  `active` TINYINT NOT NULL,
  PRIMARY KEY (`idPromo`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `RASBET`.`Notification`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Notification` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Notification` (
  `email` VARCHAR(100) NOT NULL,
  `notification` VARCHAR(150) NOT NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `RASBET`.`Currency`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Currency` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Currency` (
  `currencyName` VARCHAR(15) NOT NULL,
  `valEuro` FLOAT NOT NULL,
  PRIMARY KEY (`currencyName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `RASBET`.`Game_Followers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `RASBET`.`Game_Followers` ;

CREATE TABLE IF NOT EXISTS `RASBET`.`Game_Followers` (
  `idGame` VARCHAR(50) NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idGame`,`idUser`),
  INDEX `fk_Game_Followed_idx` (`idGame` ASC) VISIBLE,
  INDEX `fk_Follower_idx` (`idUser` ASC) VISIBLE,
  CONSTRAINT `fk_Game_Followed_idx`
    FOREIGN KEY (`idGame`)
    REFERENCES `RASBET`.`Game` (`idGame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Follower_idx`
    FOREIGN KEY (`idUser`)
    REFERENCES `RASBET`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
