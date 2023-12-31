-- MySQL Script generated by MySQL Workbench
-- Wed Dec 13 17:44:14 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema jagamakan
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema jagamakan
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jagamakan` DEFAULT CHARACTER SET utf8 ;
USE `jagamakan` ;

-- -----------------------------------------------------
-- Table `jagamakan`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jagamakan`.`user` (
  `uuid` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jagamakan`.`riwayat_kesehatan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jagamakan`.`riwayat_kesehatan` (
  `id_riwayat` INT NOT NULL,
  `tinggi_badan` INT NOT NULL,
  `berat_badan` INT NOT NULL,
  `umur` INT NOT NULL,
  `intensitas_olahraga` INT NOT NULL,
  `user_uuid` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_riwayat`),
  INDEX `fk_riwayat_kesehatan_user1_idx` (`user_uuid` ASC),
  UNIQUE INDEX `user_uuid_UNIQUE` (`user_uuid` ASC),
  CONSTRAINT `fk_riwayat_kesehatan_user1`
    FOREIGN KEY (`user_uuid`)
    REFERENCES `jagamakan`.`user` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jagamakan`.`kalori_harian`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jagamakan`.`kalori_harian` (
  `idkalori_harian` INT NOT NULL,
  `kalori_harian` INT NOT NULL,
  `riwayat_kesehatan_id_riwayat` INT NOT NULL,
  PRIMARY KEY (`idkalori_harian`),
  INDEX `fk_kalori_harian_riwayat_kesehatan1_idx` (`riwayat_kesehatan_id_riwayat` ASC),
  CONSTRAINT `fk_kalori_harian_riwayat_kesehatan1`
    FOREIGN KEY (`riwayat_kesehatan_id_riwayat`)
    REFERENCES `jagamakan`.`riwayat_kesehatan` (`id_riwayat`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `jagamakan`.`history_makanan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jagamakan`.`history_makanan` (
  `id_histori` INT NOT NULL,
  `user_uuid` VARCHAR(45) NOT NULL,
  `combined_food` VARCHAR(45) NOT NULL,
  `total_calories_detected` INT NOT NULL,
  `image_url` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_histori`),
  INDEX `fk_history_makanan_user1_idx` (`user_uuid` ASC),
  CONSTRAINT `fk_history_makanan_user1`
    FOREIGN KEY (`user_uuid`)
    REFERENCES `jagamakan`.`user` (`uuid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
