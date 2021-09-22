-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 17, 2021 at 08:04 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `springboot`
--

-- --------------------------------------------------------

--
-- Table structure for table `covidpatient`
--
DROP TABLE IF EXISTS `covidpatient`;
DROP TABLE IF EXISTS `hospitalvisithistory`;
DROP TABLE IF EXISTS `hospital_user`;
DROP TABLE IF EXISTS `patient_user`;
DROP TABLE IF EXISTS `pcrtests`;
DROP TABLE IF EXISTS `rapidantigentest`;
DROP TABLE IF EXISTS `wardtransfertable`;
DROP TABLE IF EXISTS `ward`;
DROP TABLE IF EXISTS `patient`;
DROP TABLE IF EXISTS `hospital`;
DROP TABLE IF EXISTS `user`;


CREATE TABLE `covidpatient` (
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `verified_date` date NOT NULL,
  `patient_status` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
--
-- Table structure for table `hospital`
--

CREATE TABLE `hospital` (
  `hospital_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `capacity` int(11) NOT NULL,
  PRIMARY KEY (`hospital_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- Dumping data for table `hospital`
--

INSERT INTO `hospital` (`hospital_id`, `name`, `address`, `telephone`, `capacity`) VALUES
(1, 'Hospital A', '999 p/o\r\nabcd road,\r\nabcd.', '0123456789', 8000),
(2, 'Hospital B', '999 p/o\r\npqrs road,\r\npqrs.', '0123456789', 8000),
(3, 'Hospital C', '999 p/o\r\nffff road,\r\nffff.', '0123456789', 8000);

-- --------------------------------------------------------



--
-- Table structure for table `hospital_user`
--

CREATE TABLE `hospital_user` (
  `user_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
--
-- Table structure for table `patient`
--

CREATE TABLE `patient` (
  `patient_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nic` varchar(12) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `address` varchar(400) NOT NULL,
  `gender` char(1) NOT NULL,
  `dob` date NOT NULL,
  `age` smallint(6) NOT NULL,
  `contact_no` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`contact_no`)),
  `is_user` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY(`patient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
--
-- Table structure for table `hospitalvisithistory`
--
CREATE TABLE `hospitalvisithistory` (
  `visit_id` int(11) NOT NULL AUTO_INCREMENT,
  `visit_date` date NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `ward_id` int(11) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `treatment_data` varchar(300) NOT NULL,
  `visit_status` varchar(20) NOT NULL,
  PRIMARY KEY(`visit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `patient_user`
--

CREATE TABLE `patient_user` (
  `user_id` bigint(20) NOT NULL,
  `patient_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `pcrtests`
--

CREATE TABLE `pcrtests` (
  `test_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `test_data` date NOT NULL,
  `test_result` varchar(20) NOT NULL,
  PRIMARY KEY(`test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `rapidantigentest`
--

CREATE TABLE `rapidantigentest` (
  `antigen_test_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `test_data` date NOT NULL,
  `test_result` varchar(30) NOT NULL,
  PRIMARY KEY (`antigen_test_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `password` varchar(300) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role` enum('ADMIN','MOH_ADMIN','MOH_USER','HOSPITAL_ADMIN','PATIENT','HOSPITAL_USER') NOT NULL,
  `nic` varchar(50) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100),
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `password`, `email`, `role`,`nic`,`first_name`,`last_name`) VALUES
(10000, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'adminaaa@gmail.com', 'ADMIN','909090909V','first_name','last_name'),
(10001, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'abcdfg@gmail.com', 'ADMIN','909090909V','first_name','last_name'),
(10002, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'mohusr@gmail.com', 'MOH_USER','909090909V','first_name','last_name'),
(10003, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'mohadmn@gmail.com', 'MOH_ADMIN','909090909V','first_name','last_name');

-- --------------------------------------------------------

--
-- Table structure for table `ward`
--

CREATE TABLE `ward` (
  `hospital_id` int(11) NOT NULL,
  `ward_id` int(11) NOT NULL,
  `ward_name` varchar(20) NOT NULL,
   KEY (`ward_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
--
-- Table structure for table `wardtransfertable`
--

CREATE TABLE `wardtransfertable` (
  `transfer_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `current_ward_id` int(11) NOT NULL,
  `transfer_date` date NOT NULL,
  `transfer_ward_id` int(11) NOT NULL,
  PRIMARY KEY (`transfer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
-- Indexes for table `covidpatient`
ALTER TABLE `covidpatient`
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `hostpialvisithistory`
--
ALTER TABLE `hospitalvisithistory`
  ADD KEY `ward_id` (`ward_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `hospital_user`
--

ALTER TABLE `hospital_user`
  ADD KEY `user_id` (`user_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `patient`
--

ALTER TABLE `patient`
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `patient_user`
--

ALTER TABLE `patient_user`
  ADD KEY `user_id` (`user_id`),
  ADD KEY `patient_id` (`patient_id`);

--
-- Indexes for table `pcrtests`
--

ALTER TABLE `pcrtests`
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `rapidantigentest`
--

ALTER TABLE `rapidantigentest`
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `ward`
--

ALTER TABLE `ward`
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `wardtransfertable`
--

ALTER TABLE `wardtransfertable`
  ADD KEY `current_ward_id` (`current_ward_id`),
  ADD KEY `transfer_ward_id` (`transfer_ward_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Constraints for table `covidpatient`
--

ALTER TABLE `covidpatient`
  ADD CONSTRAINT `covidpatient_ibfk_2` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`),
  ADD CONSTRAINT `covidpatient_ibfk_3` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`);

--
-- Constraints for table `hospitalvisithistory`
--

ALTER TABLE `hospitalvisithistory`
  ADD CONSTRAINT `hospitalvisithistory_ibfk_b` FOREIGN KEY (`ward_id`) REFERENCES `ward` (`ward_id`),
  ADD CONSTRAINT `hospitalvisithistory_ibfk_a` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `hospitalvisithistory_ibfk_c` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `hospital_user`
--

ALTER TABLE `hospital_user`
  ADD CONSTRAINT `hospital_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `hospital_user_ibfk_3` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `patient`
--

ALTER TABLE `patient`
  ADD CONSTRAINT `patient_ibfk_1` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `patient_user`
--

ALTER TABLE `patient_user`
  ADD CONSTRAINT `patient_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `patient_user_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`);

--
-- Constraints for table `pcrtests`
--

ALTER TABLE `pcrtests`
  ADD CONSTRAINT `pcrtests_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `pcrtests_ibfk_3` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `rapidantigentest`
--

ALTER TABLE `rapidantigentest`
  ADD CONSTRAINT `rapidantigentest_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `rapidantigentest_ibfk_2` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `ward`
--

ALTER TABLE `ward`
  ADD CONSTRAINT `ward_ibfk_1` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);

--
-- Constraints for table `wardtransfertable`
--

ALTER TABLE `wardtransfertable`
  ADD CONSTRAINT `wardtransfertable_ibfk_1` FOREIGN KEY (`current_ward_id`) REFERENCES `ward` (`ward_id`),
  ADD CONSTRAINT `wardtransfertable_ibfk_2` FOREIGN KEY (`transfer_ward_id`) REFERENCES `ward` (`ward_id`),
  ADD CONSTRAINT `wardtransfertable_ibfk_3` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `wardtransfertable_ibfk_4` FOREIGN KEY (`hospital_id`) REFERENCES `hospital` (`hospital_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- This procedure used to add ADMINS,MOH_ADMINS,MOH_USERS.HOSPITAL_USERS and HOSPITAL_ADMINS to the database
-- Use this procedure to add users under above roles
-- TODO: need new procedure to add PATIENT to the system

DELIMITER $$
CREATE or replace DEFINER=`root`@`localhost` PROCEDURE `add_user` (IN `email` VARCHAR(100),
                                                                         IN `role` VARCHAR(100),
                                                                         IN `nic` varchar(50),
                                                                         IN `password` varchar(300),
                                                                         IN `first_name` varchar(100),
                                                                         IN `last_name` varchar(100),
                                                                         IN `hospital_id` int(11))  BEGIN
        DECLARE u_id bigint(20) DEFAULT NULL;
        IF NOT EXISTS (SELECT user.email FROM user
        WHERE user.email = email) THEN
			IF ( role = 'ADMIN' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic;
                    COMMIT;

			ELSEIF ( role = 'MOH_ADMIN' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic;
                    COMMIT;

            ELSEIF ( role = 'MOH_USER' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic;
                    COMMIT;

			ELSEIF ( role = 'HOSPITAL_ADMIN' ) THEN
				IF hospital_id IS NOT NULL AND EXISTS (SELECT hospital.hospital_id FROM
                                                            hospital WHERE
                                                            hospital.hospital_id = hospital_id)
                                                            THEN
                    START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT user_id INTO u_id FROM `user` WHERE user.nic = nic AND user.email = email;
                    INSERT INTO `hospital_user` (`user_id`,`hospital_id`)
                        VALUES (u_id,hospital_id);
                    SELECT * FROM `user` NATURAL JOIN `hospital_user` WHERE user.nic = nic;
                    COMMIT;

                    ELSE
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT = "invalid hospital id or hospital doesn't exists";
                    END IF;

            ELSEIF ( role = 'HOSPITAL_USER' ) THEN
                IF hospital_id IS NOT NULL AND EXISTS (SELECT hospital.hospital_id FROM
                                                            hospital WHERE
                                                            hospital.hospital_id = hospital_id)
                                                            THEN
                    START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT `user_id` INTO u_id FROM user WHERE user.nic = nic AND user.email = email;
                    INSERT INTO `hospital_user` (`user_id`,`hospital_id`) VALUES
                        (u_id,hospital_id);
                    SELECT * FROM `user` NATURAL JOIN `hospital_user` WHERE user.nic = nic;
                    COMMIT;

                    ELSE
                        SIGNAL SQLSTATE '45000'
                        SET MESSAGE_TEXT = "invalid hospital id or hospital doesn't exists";
                    END IF;
            ELSE
                SIGNAL SQLSTATE '45000'
			    SET MESSAGE_TEXT = 'invalid role';
            END IF;
		ELSE
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'user already exists in db';
		END IF;
    END$$
DELIMITER ;

-- This procedure used to update ADMINS,MOH_ADMINS,MOH_USERS,HOSPITAL_USERS and HOSPITAL_ADMINS to the database
-- Use this procedure to add users under above roles
