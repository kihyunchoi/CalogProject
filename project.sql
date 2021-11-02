-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 02, 2021 at 05:43 AM
-- Server version: 10.4.20-MariaDB
-- PHP Version: 8.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `project`
--

-- --------------------------------------------------------

--
-- Table structure for table `diet`
--

CREATE TABLE `diet` (
  `diet_id` int(11) NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `diet_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `diet_type_id` int(11) NOT NULL,
  `diet_menu_id` int(11) NOT NULL,
  `diet_amount` int(11) NOT NULL,
  `sum_calorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `diet_menu`
--

CREATE TABLE `diet_menu` (
  `diet_menu_id` int(11) NOT NULL,
  `diet_menu_name` varchar(50) NOT NULL,
  `calorie` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `diet_type`
--

CREATE TABLE `diet_type` (
  `diet_type_id` int(11) NOT NULL,
  `diet_type_name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `fitness`
--

CREATE TABLE `fitness` (
  `fitness_id` int(11) NOT NULL,
  `user_id` varchar(30) NOT NULL,
  `fitness_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `fitness_menu_id` int(11) NOT NULL,
  `fitness_seconds` int(11) NOT NULL,
  `calorie_consumption` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `fitness_menu`
--

CREATE TABLE `fitness_menu` (
  `fitness_menu_id` int(11) NOT NULL,
  `fitness_menu_name` varchar(50) NOT NULL,
  `fitness_menu_image` varchar(200) NOT NULL,
  `unit_calorie` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `sleeping`
--

CREATE TABLE `sleeping` (
  `user_id` varchar(30) NOT NULL,
  `sleeping_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `sleeping_seconds` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` varchar(30) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `birthday` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `gender` char(10) DEFAULT NULL,
  `height` double DEFAULT 0,
  `weight` double DEFAULT 0,
  `bmi` double DEFAULT 0,
  `del_check_user_id` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Stand-in structure for view `user_diet_total_calories_view`
-- (See below for the actual view)
--
CREATE TABLE `user_diet_total_calories_view` (
`user_id` varchar(30)
,`sum_calorie` decimal(32,0)
,`diet_date` varchar(10)
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `user_diet_view`
-- (See below for the actual view)
--
CREATE TABLE `user_diet_view` (
`user_id` varchar(30)
,`diet_type_id` int(11)
,`diet_type_name` varchar(30)
,`diet_menu_name` varchar(50)
,`diet_amount` int(11)
,`calorie` int(11)
,`sum_calorie` int(11)
,`diet_date` timestamp
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `user_fitness_total_calories_view`
-- (See below for the actual view)
--
CREATE TABLE `user_fitness_total_calories_view` (
`user_id` varchar(30)
,`fitness_sum_seconds` decimal(32,0)
,`fitness_sum_calorie_consumption` double(18,1)
,`fitness_date` timestamp
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `user_fitness_view`
-- (See below for the actual view)
--
CREATE TABLE `user_fitness_view` (
`user_id` varchar(30)
,`fitness_menu_id` int(11)
,`fitness_menu_name` varchar(50)
,`fitness_seconds` int(11)
,`unit_calorie` double
,`calorie_consumption` double
,`fitness_date` timestamp
,`fitness_menu_image` varchar(200)
);

-- --------------------------------------------------------

--
-- Structure for view `user_diet_total_calories_view`
--
DROP TABLE IF EXISTS `user_diet_total_calories_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`calog`@`localhost` SQL SECURITY DEFINER VIEW `user_diet_total_calories_view`  AS   (select `user`.`user_id` AS `user_id`,sum(`diet`.`sum_calorie`) AS `sum_calorie`,date_format(`diet`.`diet_date`,'%Y-%m-%d') AS `diet_date` from (((`user` join `diet`) join `diet_menu`) join `diet_type`) where `user`.`user_id` = `diet`.`user_id` and `diet_menu`.`diet_menu_id` = `diet`.`diet_menu_id` and `diet_type`.`diet_type_id` = `diet`.`diet_type_id` group by `diet`.`diet_date` order by `user`.`user_id`)  ;

-- --------------------------------------------------------

--
-- Structure for view `user_diet_view`
--
DROP TABLE IF EXISTS `user_diet_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`calog`@`localhost` SQL SECURITY DEFINER VIEW `user_diet_view`  AS   (select `user`.`user_id` AS `user_id`,`diet_type`.`diet_type_id` AS `diet_type_id`,`diet_type`.`diet_type_name` AS `diet_type_name`,`diet_menu`.`diet_menu_name` AS `diet_menu_name`,`diet`.`diet_amount` AS `diet_amount`,`diet_menu`.`calorie` AS `calorie`,`diet`.`sum_calorie` AS `sum_calorie`,`diet`.`diet_date` AS `diet_date` from (((`user` join `diet`) join `diet_menu`) join `diet_type`) where `user`.`user_id` = `diet`.`user_id` and `diet`.`diet_type_id` = `diet_type`.`diet_type_id` and `diet`.`diet_menu_id` = `diet_menu`.`diet_menu_id` order by `user`.`user_id`,`diet`.`diet_date`,`diet_type`.`diet_type_id`)  ;

-- --------------------------------------------------------

--
-- Structure for view `user_fitness_total_calories_view`
--
DROP TABLE IF EXISTS `user_fitness_total_calories_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`calog`@`localhost` SQL SECURITY DEFINER VIEW `user_fitness_total_calories_view`  AS   (select `user`.`user_id` AS `user_id`,sum(`fitness`.`fitness_seconds`) AS `fitness_sum_seconds`,sum(round(`fitness`.`calorie_consumption`,1)) AS `fitness_sum_calorie_consumption`,`fitness`.`fitness_date` AS `fitness_date` from ((`user` join `fitness_menu`) join `fitness`) where `user`.`user_id` = `fitness`.`user_id` and `fitness_menu`.`fitness_menu_id` = `fitness`.`fitness_menu_id` group by `fitness`.`fitness_date` order by `user`.`user_id`)  ;

-- --------------------------------------------------------

--
-- Structure for view `user_fitness_view`
--
DROP TABLE IF EXISTS `user_fitness_view`;

CREATE ALGORITHM=UNDEFINED DEFINER=`calog`@`localhost` SQL SECURITY DEFINER VIEW `user_fitness_view`  AS   (select `user`.`user_id` AS `user_id`,`fitness_menu`.`fitness_menu_id` AS `fitness_menu_id`,`fitness_menu`.`fitness_menu_name` AS `fitness_menu_name`,`fitness`.`fitness_seconds` AS `fitness_seconds`,`fitness_menu`.`unit_calorie` AS `unit_calorie`,`fitness`.`calorie_consumption` AS `calorie_consumption`,`fitness`.`fitness_date` AS `fitness_date`,`fitness_menu`.`fitness_menu_image` AS `fitness_menu_image` from ((`user` join `fitness_menu`) join `fitness`) where `user`.`user_id` = `fitness`.`user_id` and `fitness_menu`.`fitness_menu_id` = `fitness`.`fitness_menu_id` order by `user`.`user_id`,`fitness`.`fitness_date`,`fitness_menu`.`fitness_menu_name`)  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `diet`
--
ALTER TABLE `diet`
  ADD PRIMARY KEY (`diet_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `diet_type_id` (`diet_type_id`),
  ADD KEY `diet_menu_id` (`diet_menu_id`);

--
-- Indexes for table `diet_menu`
--
ALTER TABLE `diet_menu`
  ADD PRIMARY KEY (`diet_menu_id`);

--
-- Indexes for table `diet_type`
--
ALTER TABLE `diet_type`
  ADD PRIMARY KEY (`diet_type_id`);

--
-- Indexes for table `fitness`
--
ALTER TABLE `fitness`
  ADD PRIMARY KEY (`fitness_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `fitness_menu_id` (`fitness_menu_id`);

--
-- Indexes for table `fitness_menu`
--
ALTER TABLE `fitness_menu`
  ADD PRIMARY KEY (`fitness_menu_id`);

--
-- Indexes for table `sleeping`
--
ALTER TABLE `sleeping`
  ADD PRIMARY KEY (`sleeping_date`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `diet`
--
ALTER TABLE `diet`
  MODIFY `diet_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `diet_menu`
--
ALTER TABLE `diet_menu`
  MODIFY `diet_menu_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `diet_type`
--
ALTER TABLE `diet_type`
  MODIFY `diet_type_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fitness`
--
ALTER TABLE `fitness`
  MODIFY `fitness_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `fitness_menu`
--
ALTER TABLE `fitness_menu`
  MODIFY `fitness_menu_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `diet`
--
ALTER TABLE `diet`
  ADD CONSTRAINT `diet_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `diet_ibfk_2` FOREIGN KEY (`diet_type_id`) REFERENCES `diet_type` (`diet_type_id`),
  ADD CONSTRAINT `diet_ibfk_3` FOREIGN KEY (`diet_menu_id`) REFERENCES `diet_menu` (`diet_menu_id`);

--
-- Constraints for table `fitness`
--
ALTER TABLE `fitness`
  ADD CONSTRAINT `fitness_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `fitness_ibfk_2` FOREIGN KEY (`fitness_menu_id`) REFERENCES `fitness_menu` (`fitness_menu_id`);

--
-- Constraints for table `sleeping`
--
ALTER TABLE `sleeping`
  ADD CONSTRAINT `sleeping_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
