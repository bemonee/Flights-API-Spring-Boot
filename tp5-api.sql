-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 25-05-2018 a las 22:47:55
-- Versión del servidor: 5.7.20
-- Versión de PHP: 7.1.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tp5-api`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `airports`
--

CREATE TABLE `airports` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `iata` varchar(50) DEFAULT NULL,
  `id_cities` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cabinRoutes`
--

CREATE TABLE `cabinRoutes` (
  `id` int(11) NOT NULL,
  `id_cabin` int(11) DEFAULT NULL,
  `id_route` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cabins`
--

CREATE TABLE `cabins` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cities`
--

CREATE TABLE `cities` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `id_state` int(11) DEFAULT NULL,
  `iata` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `countries`
--

CREATE TABLE `countries` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `iso2` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prices`
--

CREATE TABLE `prices` (
  `id` int(11) NOT NULL,
  `from` date DEFAULT NULL,
  `to` date DEFAULT NULL,
  `price` double DEFAULT NULL,
  `id_cabinRoutes` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `routes`
--

CREATE TABLE `routes` (
  `id` int(11) NOT NULL,
  `origin_airport_id` int(11) DEFAULT NULL,
  `destination_airport_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `states`
--

CREATE TABLE `states` (
  `id` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `id_country` int(11) DEFAULT NULL,
  `iata` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `airports`
--
ALTER TABLE `airports`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `airports_id_uindex` (`id`),
  ADD UNIQUE KEY `airports_iata_uindex` (`iata`),
  ADD KEY `airports_cities_id_fk` (`id_cities`);

--
-- Indices de la tabla `cabinRoutes`
--
ALTER TABLE `cabinRoutes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cabinRoutes_id_uindex` (`id`),
  ADD KEY `cabinRoutes_cabins_id_fk` (`id_cabin`),
  ADD KEY `cabinRoutes_routes_id_fk` (`id_route`);

--
-- Indices de la tabla `cabins`
--
ALTER TABLE `cabins`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cabins_id_uindex` (`id`);

--
-- Indices de la tabla `cities`
--
ALTER TABLE `cities`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `cities_id_uindex` (`id`),
  ADD UNIQUE KEY `cities_iata_uindex` (`iata`),
  ADD KEY `cities_states_id_fk` (`id_state`);

--
-- Indices de la tabla `countries`
--
ALTER TABLE `countries`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `countries_id_uindex` (`id`),
  ADD UNIQUE KEY `countries_iso2_uindex` (`iso2`);

--
-- Indices de la tabla `prices`
--
ALTER TABLE `prices`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `prices_id_uindex` (`id`),
  ADD KEY `prices_cabinRoutes_id_fk` (`id_cabinRoutes`);

--
-- Indices de la tabla `routes`
--
ALTER TABLE `routes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `rutas_id_uindex` (`id`),
  ADD KEY `rutas_airports_id_fk` (`origin_airport_id`),
  ADD KEY `rutas_airports_id_fk_2` (`destination_airport_id`);

--
-- Indices de la tabla `states`
--
ALTER TABLE `states`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `states_id_uindex` (`id`),
  ADD UNIQUE KEY `states_iata_uindex` (`iata`),
  ADD KEY `states_countries_id_fk` (`id_country`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `airports`
--
ALTER TABLE `airports`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cabinRoutes`
--
ALTER TABLE `cabinRoutes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cabins`
--
ALTER TABLE `cabins`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cities`
--
ALTER TABLE `cities`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `countries`
--
ALTER TABLE `countries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `prices`
--
ALTER TABLE `prices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `routes`
--
ALTER TABLE `routes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `states`
--
ALTER TABLE `states`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `airports`
--
ALTER TABLE `airports`
  ADD CONSTRAINT `airports_cities_id_fk` FOREIGN KEY (`id_cities`) REFERENCES `cities` (`id`);

--
-- Filtros para la tabla `cabinRoutes`
--
ALTER TABLE `cabinRoutes`
  ADD CONSTRAINT `cabinRoutes_cabins_id_fk` FOREIGN KEY (`id_cabin`) REFERENCES `cabins` (`id`),
  ADD CONSTRAINT `cabinRoutes_routes_id_fk` FOREIGN KEY (`id_route`) REFERENCES `routes` (`id`);

--
-- Filtros para la tabla `cities`
--
ALTER TABLE `cities`
  ADD CONSTRAINT `cities_states_id_fk` FOREIGN KEY (`id_state`) REFERENCES `states` (`id`);

--
-- Filtros para la tabla `prices`
--
ALTER TABLE `prices`
  ADD CONSTRAINT `prices_cabinRoutes_id_fk` FOREIGN KEY (`id_cabinRoutes`) REFERENCES `cabinRoutes` (`id`);

--
-- Filtros para la tabla `routes`
--
ALTER TABLE `routes`
  ADD CONSTRAINT `rutas_airports_id_fk` FOREIGN KEY (`origin_airport_id`) REFERENCES `airports` (`id`),
  ADD CONSTRAINT `rutas_airports_id_fk_2` FOREIGN KEY (`destination_airport_id`) REFERENCES `airports` (`id`);

--
-- Filtros para la tabla `states`
--
ALTER TABLE `states`
  ADD CONSTRAINT `states_countries_id_fk` FOREIGN KEY (`id_country`) REFERENCES `countries` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
