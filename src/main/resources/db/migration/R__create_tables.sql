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

-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 26, 2021 at 03:38 PM
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

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_user` (IN `email` VARCHAR(100), IN `role` VARCHAR(100), IN `nic` VARCHAR(50), IN `password` VARCHAR(300), IN `first_name` VARCHAR(100), IN `last_name` VARCHAR(100), IN `hospital_id` INT(11))  BEGIN
        DECLARE u_id bigint(20) DEFAULT NULL;
        IF NOT EXISTS (SELECT user.email FROM user
        WHERE user.email = email) THEN
			IF ( role = 'ADMIN' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic AND user.email = email;
                    COMMIT;

			ELSEIF ( role = 'MOH_ADMIN' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic AND user.email = email;
                    COMMIT;

            ELSEIF ( role = 'MOH_USER' ) THEN
                START TRANSACTION;
                    INSERT INTO `user` (`password`, `email`, `role`,`nic`,`first_name`,`last_name`)
                        VALUES (password,email,role,nic,first_name,last_name);
                    SELECT * FROM `user` WHERE user.nic = nic AND user.email = email;
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
                    SELECT * FROM `user` NATURAL JOIN `hospital_user` WHERE user.nic = nic AND user.email = email;
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
                    SELECT * FROM `user` NATURAL JOIN `hospital_user` WHERE user.nic = nic AND user.email = email;
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




-- --------------------------------------------------------
--
-- Event for getting cumultive statistice
--

DELIMITER $$

CREATE DEFINER=`root`@`localhost` EVENT `update_all_satistics` ON SCHEDULE EVERY 1 DAY STARTS '2021-10-29 20:15:02' ON COMPLETION NOT PRESERVE ENABLE DO BEGIN


SET @total_pcrs = (SELECT COUNT(*) FROM pcrtests);
SET @total_antigens = (SELECT COUNT(*) AS c FROM rapidantigentest);
SET @total_covid_patients = (SELECT COUNT(*) AS c FROM covidpatient);
SET @total_deaths = (SELECT COUNT(*) AS c FROM covidpatient WHERE patient_status = 'DEATH');
SET @total_actives = (SELECT COUNT(*) AS c FROM covidpatient WHERE patient_status = 'ACTIVE');
SET @total_antigens_positive = (SELECT COUNT(*) AS c FROM rapidantigentest WHERE test_result='POSITIVE');
SET @total_antigens_negative = (SELECT COUNT(*) AS c FROM rapidantigentest WHERE test_result='NEGATIVE');
SET @total_antigens_pending = (SELECT COUNT(*) AS c FROM rapidantigentest WHERE test_result='PENDING');
SET @total_pcrs_positive = (SELECT COUNT(*) AS c FROM pcrtests WHERE test_result='POSITIVE');
SET @total_pcrs_negative = (SELECT COUNT(*) AS c FROM pcrtests WHERE test_result='NEGATIVE');
SET @total_pcrs_pending = (SELECT COUNT(*) AS c FROM pcrtests WHERE test_result='PENDING');
SET @total_recovered = (SELECT COUNT(*) AS c FROM covidpatient WHERE patient_status = 'RECOVERED');
SET @total_positives = (SELECT COUNT(*) AS c FROM covidpatient);
INSERT INTO statistics (`date`,  `total_pcrs`,`total_pcrs_positive`,`total_pcrs_negative`,`total_pcrs_pending`,
   `total_antigens`,`total_antigens_positive`,
   `total_antigens_negative`,`total_antigens_pending`,`total_covid_patients`, `total_deaths`, `total_actives`, `total_recovered`, `total_positives`)
 VALUES (CURDATE(), @total_pcrs, @total_pcrs_positive, @total_pcrs_negative, @total_antigens_pending,
        @total_antigens, @total_antigens_positive, @total_antigens_negative,@total_antigens_pending,@total_covid_patients,@total_deaths,@total_actives,@total_recovered,@total_positives);

END $$

DELIMITER ;



-- --------------------------------------------------------
CREATE TABLE `covidpatient` (
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `patient_status` enum('ACTIVE','DEATH','RECOVERED','') NOT NULL,
  `verified_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `covidpatient`
--

INSERT INTO `covidpatient` (`patient_id`, `hospital_id`, `patient_status`, `verified_date`) VALUES
(8, 9, 'ACTIVE', '2021-10-21');



-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(18);

-- --------------------------------------------------------
--
-- Table structure for table `patient`
--

CREATE TABLE `patient` (
  `patient_id` bigint(20) NOT NULL,
  `nic` varchar(12) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `address` varchar(400) NOT NULL,
  `gender` char(1) NOT NULL,
  `dob` date NOT NULL,
  `age` smallint(6) NOT NULL,
  `contact_no` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL CHECK (json_valid(`contact_no`)),
  `is_user` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `patient`
--

INSERT INTO `patient` (`patient_id`, `nic`, `hospital_id`, `address`, `gender`, `dob`, `age`, `contact_no`, `is_user`) VALUES
(8, '99999999', 3, 'aaaa', '0', '2021-10-13', 99, '99', 0);

-- --------------------------------------------------------

--
-- Table structure for table `hospital`
--

CREATE TABLE `hospital` (
  `hospital_id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL,
  `address` varchar(200) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `capacity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hospital`
--

INSERT INTO `hospital` (`hospital_id`, `name`, `address`, `telephone`, `capacity`) VALUES
(1, 'Hospital A', '999 p/o\r\nabcd road,\r\nabcd.', '0123456789', 8000),
(2, 'Hospital B', '999 p/o\r\npqrs road,\r\npqrs.', '0123456789', 8000),
(3, 'Hospital C', '999 p/o\r\nffff road,\r\nffff.', '0123456789', 8000),
(8, 'Hospital D', '999 p/o\r\nffff road,\r\nffff.', '0123456789', 8000),
(9, 'Hospital P', '999 p/o\r\nffff road,\r\nffff.', '0123456789', 8000);

-- --------------------------------------------------------

--
-- Table structure for table `hospitalvisithistory`
--

CREATE TABLE `hospitalvisithistory` (
  `visit_id` int(11) NOT NULL,
  `visit_date` date NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `ward_id` int(11) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `data` varchar(300) DEFAULT NULL,
  `visit_status` enum('QUARANTINED','ADMITTED','DISCHARGED','') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `hospitalvisithistory`
--

INSERT INTO `hospitalvisithistory` (`visit_id`, `visit_date`, `hospital_id`, `ward_id`, `patient_id`, `data`, `visit_status`) VALUES
(3, '2021-10-13', 3, 3, 8, 'aaaa', 'aaaa'),
(117, '2020-10-10', 9, 3, 8, 'new data updated', 'new visit status');

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
  `test_id` int(11) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `test_data` date NOT NULL,
  `test_result` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------
--
-- Table structure for table `statistics`
--

CREATE TABLE `statistics` (
  `id` bigint(20) NOT NULL,
  `total_pcrs` bigint(20) NOT NULL,
  `total_antigens` bigint(20) NOT NULL,
  `date` date NOT NULL,
  `total_covid_patients` bigint(20) NOT NULL,
  `total_deaths` bigint(20) NOT NULL,
  `total_actives` bigint(20) NOT NULL,
  `total_recovered` bigint(20) NOT NULL,
  `total_positives` bigint(11) NOT NULL,
  `total_pcrs_positive` bigint(20) NOT NULL,
  `total_pcrs_negative` bigint(20) NOT NULL,
  `total_pcrs_pending` int(11) NOT NULL,
  `total_antigens_pending` int(11) NOT NULL,
  `total_antigens_negative` bigint(20) NOT NULL,
  `total_antigens_positive` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `statistics`
--

INSERT INTO `statistics` (`id`, `total_pcrs`, `total_antigens`, `date`, `total_covid_patients`, `total_deaths`, `total_actives`, `total_recovered`, `total_positives`, `total_pcrs_positive`, `total_pcrs_negative`, `total_pcrs_pending`, `total_antigens_pending`, `total_antigens_negative`, `total_antigens_positive`) VALUES
(1, 1, 1, '2021-10-17', 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 1),
(2, 1, 1, '2021-10-18', 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 1),
(3, 1, 1, '2021-10-19', 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 1),
(4, 1, 1, '2021-10-20', 2, 0, 2, 0, 2, 0, 0, 0, 0, 0, 1);



-- --------------------------------------------------------

--
-- Table structure for table `rapidantigentest`
--

CREATE TABLE `rapidantigentest` (
  `antigen_test_id` int(11) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `test_data` date NOT NULL,
  `test_result` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL,
  `password` varchar(300) NOT NULL,
  `email` varchar(100) NOT NULL,
  `role` enum('ADMIN','MOH_ADMIN','MOH_USER','HOSPITAL_ADMIN','PATIENT','HOSPITAL_USER') NOT NULL,
  `nic` varchar(50) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `password`, `email`, `role`, `nic`, `first_name`, `last_name`) VALUES
(10000, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'a@gmail.com', 'ADMIN', '123456789v', 'first_name', 'last_name'),
(10001, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'abcdfg@gmail.com', 'ADMIN', '123456799v', 'first_name', 'last_name'),
(10002, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'mohusr@gmail.com', 'MOH_USER', '123456778v', 'first_name', 'last_name'),
(10003, '$2a$10$uHTQrwQWUSAgWMekvYgqduJ2odI9gdGpdFu7zXR5816/drVQfRroC', 'mohadmn@gmail.com', 'MOH_ADMIN', '1234567878v', 'first_name', 'last_name'),
(100441, '$2a$10$I1URLprHeAYnOwGEfk5CWeCb3xqSRE610tTpF5xGdtDuwCJPzGe6m', 'mohusr_new_new_new@gmail.com', 'MOH_USER', '988989899v', 'should_not_be_null', 'should_be_not_null');

-- --------------------------------------------------------

--
-- Table structure for table `ward`
--

CREATE TABLE `ward` (
  `hospital_id` int(11) NOT NULL,
  `ward_id` int(11) NOT NULL,
  `ward_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ward`
--

INSERT INTO `ward` (`hospital_id`, `ward_id`, `ward_name`) VALUES
(1, 1, 'ward 1'),
(1, 2, 'ward 2'),
(1, 3, 'ward 3'),
(2, 1, 'ward 1'),
(2, 2, 'ward 2'),
(2, 3, 'ward 3'),
(3, 1, 'ward 1'),
(3, 2, 'ward 2'),
(3, 3, 'ward 3');

-- --------------------------------------------------------

--
-- Table structure for table `wardtransfertable`
--

CREATE TABLE `wardtransfertable` (
  `transfer_id` int(11) NOT NULL,
  `patient_id` bigint(20) NOT NULL,
  `hospital_id` int(11) NOT NULL,
  `current_ward_id` int(11) NOT NULL,
  `transfer_date` date NOT NULL,
  `transfer_ward_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `covidpatient`
--
ALTER TABLE `covidpatient`
  ADD PRIMARY KEY (`patient_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `hospital`
--
ALTER TABLE `hospital`
  ADD PRIMARY KEY (`hospital_id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `hospitalvisithistory`
--
ALTER TABLE `hospitalvisithistory`
  ADD PRIMARY KEY (`visit_id`),
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
  ADD PRIMARY KEY (`patient_id`),
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
  ADD PRIMARY KEY (`test_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `rapidantigentest`
--
ALTER TABLE `rapidantigentest`
  ADD PRIMARY KEY (`antigen_test_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);


  --
  -- Indexes for table `statistics`
  --
  ALTER TABLE `statistics`
    ADD PRIMARY KEY (`id`);


--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `ward`
--
ALTER TABLE `ward`
  ADD KEY `ward_id` (`ward_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- Indexes for table `wardtransfertable`
--
ALTER TABLE `wardtransfertable`
  ADD PRIMARY KEY (`transfer_id`),
  ADD KEY `current_ward_id` (`current_ward_id`),
  ADD KEY `transfer_ward_id` (`transfer_ward_id`),
  ADD KEY `patient_id` (`patient_id`),
  ADD KEY `hospital_id` (`hospital_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `hospital`
--
ALTER TABLE `hospital`
  MODIFY `hospital_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `hospitalvisithistory`
--
ALTER TABLE `hospitalvisithistory`
  MODIFY `visit_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `patient`
--
ALTER TABLE `patient`
  MODIFY `patient_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `pcrtests`
--
ALTER TABLE `pcrtests`
  MODIFY `test_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `rapidantigentest`
--
ALTER TABLE `rapidantigentest`
  MODIFY `antigen_test_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100000;

--
-- AUTO_INCREMENT for table `wardtransfertable`
--
ALTER TABLE `wardtransfertable`
  MODIFY `transfer_id` int(11) NOT NULL AUTO_INCREMENT;


  --
  -- AUTO_INCREMENT for table `statistics`
  --
  ALTER TABLE `statistics`
    MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
  COMMIT;

--
-- Constraints for dumped tables
--


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
  ADD CONSTRAINT `hospitalvisithistory_ibfk_a` FOREIGN KEY (`patient_id`) REFERENCES `patient` (`patient_id`),
  ADD CONSTRAINT `hospitalvisithistory_ibfk_b` FOREIGN KEY (`ward_id`) REFERENCES `ward` (`ward_id`),
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
