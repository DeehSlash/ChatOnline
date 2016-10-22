-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 22-Out-2016 às 20:25
-- Versão do servidor: 10.1.16-MariaDB
-- PHP Version: 5.6.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `menssageiro`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `grupo`
--

CREATE TABLE `grupo` (
  `id` int(3) NOT NULL,
  `nomeGrupo` varchar(10) NOT NULL,
  `idMembro1` int(3) UNSIGNED DEFAULT NULL,
  `idMembro2` int(3) UNSIGNED DEFAULT NULL,
  `idMembro3` int(3) UNSIGNED DEFAULT NULL,
  `idMembro4` int(3) UNSIGNED DEFAULT NULL,
  `idMembro5` int(3) UNSIGNED DEFAULT NULL,
  `idMembro6` int(3) UNSIGNED DEFAULT NULL,
  `idMembro7` int(3) UNSIGNED DEFAULT NULL,
  `idMembro8` int(3) UNSIGNED DEFAULT NULL,
  `idMembro9` int(3) UNSIGNED DEFAULT NULL,
  `idMembro10` int(3) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensagem`
--

CREATE TABLE `mensagem` (
  `idUsuarioOrigem` int(3) UNSIGNED NOT NULL,
  `idDestino` int(3) UNSIGNED NOT NULL,
  `idMensagem` int(5) UNSIGNED NOT NULL,
  `destinoTipo` enum('U','G') NOT NULL,
  `txtMensagem` text NOT NULL,
  `timeMensagem` datetime NOT NULL,
  `arquivo` blob,
  `tipoMens` enum('M','I','V','A') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `id` int(3) UNSIGNED NOT NULL,
  `usuario` varchar(20) NOT NULL,
  `senha` varchar(20) NOT NULL,
  `foto` blob
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `grupo`
--
ALTER TABLE `grupo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mensagem`
--
ALTER TABLE `mensagem`
  ADD PRIMARY KEY (`idUsuarioOrigem`,`idDestino`,`idMensagem`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
