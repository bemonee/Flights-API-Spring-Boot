-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema tp5-api
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `tp5-api` ;

-- -----------------------------------------------------
-- Schema tp5-api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `tp5-api` DEFAULT CHARACTER SET latin1 ;
USE `tp5-api` ;

-- -----------------------------------------------------
-- Table `tp5-api`.`countries`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`countries` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`countries` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `iso2` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `countries_iso2_uindex` (`iso2` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`states`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`states` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`states` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `id_country` BIGINT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `iata` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `states_iata_uindex` (`iata` ASC),
  INDEX `states_countries_id_fk` (`id_country` ASC),
  CONSTRAINT `states_countries_id_fk`
    FOREIGN KEY (`id_country`)
    REFERENCES `tp5-api`.`countries` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`cities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`cities` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`cities` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `id_state` BIGINT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `iata` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `cities_iata_uindex` (`iata` ASC),
  INDEX `cities_states_id_fk` (`id_state` ASC),
  CONSTRAINT `cities_states_id_fk`
    FOREIGN KEY (`id_state`)
    REFERENCES `tp5-api`.`states` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`airports`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`airports` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`airports` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `id_city` BIGINT NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `iata` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `airports_iata_uindex` (`iata` ASC),
  INDEX `airports_cities_id_fk` (`id_city` ASC),
  CONSTRAINT `airports_cities_id_fk`
    FOREIGN KEY (`id_city`)
    REFERENCES `tp5-api`.`cities` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`cabins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`cabins` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`cabins` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`routes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`routes` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`routes` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `origin_airport_id` BIGINT NOT NULL,
  `destination_airport_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `rutas_airports_id_fk` (`origin_airport_id` ASC),
  INDEX `rutas_airports_id_fk_2` (`destination_airport_id` ASC),
  UNIQUE INDEX `origin_airport_id_UNIQUE` (`origin_airport_id` ASC),
  UNIQUE INDEX `destination_airport_id_UNIQUE` (`destination_airport_id` ASC),
  CONSTRAINT `rutas_airports_id_fk`
    FOREIGN KEY (`origin_airport_id`)
    REFERENCES `tp5-api`.`airports` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `rutas_airports_id_fk_2`
    FOREIGN KEY (`destination_airport_id`)
    REFERENCES `tp5-api`.`airports` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`routes_by_cabins`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`routes_by_cabins` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`routes_by_cabins` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `id_cabin` BIGINT NOT NULL,
  `id_route` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `cabinRoutes_cabins_id_fk` (`id_cabin` ASC),
  INDEX `cabinRoutes_routes_id_fk` (`id_route` ASC),
  UNIQUE INDEX `id_cabin_UNIQUE` (`id_cabin` ASC),
  UNIQUE INDEX `id_route_UNIQUE` (`id_route` ASC),
  CONSTRAINT `cabinRoutes_cabins_id_fk`
    FOREIGN KEY (`id_cabin`)
    REFERENCES `tp5-api`.`cabins` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `cabinRoutes_routes_id_fk`
    FOREIGN KEY (`id_route`)
    REFERENCES `tp5-api`.`routes` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tp5-api`.`prices`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tp5-api`.`prices` ;

CREATE TABLE IF NOT EXISTS `tp5-api`.`prices` (
  `id` BIGINT NULL AUTO_INCREMENT,
  `id_rbc` BIGINT NOT NULL,
  `from_date` DATE NOT NULL,
  `to_date` DATE NOT NULL,
  `price` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `prices_cabinRoutes_id_fk` (`id_rbc` ASC),
  CONSTRAINT `prices_cabinRoutes_id_fk`
    FOREIGN KEY (`id_rbc`)
    REFERENCES `tp5-api`.`routes_by_cabins` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
