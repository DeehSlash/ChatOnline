-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 22-Out-2016 às 15:30
-- Versão do servidor: 10.1.16-MariaDB
-- PHP Version: 7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mensageiro`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `grupo`
--

CREATE TABLE `grupo` (
  `id` int(10) UNSIGNED NOT NULL,
  `nomeGrupo` varchar(10) DEFAULT NULL,
  `idAdministrador` int(10) UNSIGNED DEFAULT NULL,
  `idMembro1` int(10) UNSIGNED DEFAULT NULL,
  `idMembro2` int(10) UNSIGNED DEFAULT NULL,
  `idMembro3` int(10) UNSIGNED DEFAULT NULL,
  `idMembro4` int(10) UNSIGNED DEFAULT NULL,
  `idMembro5` int(10) UNSIGNED DEFAULT NULL,
  `idMembro6` int(10) UNSIGNED DEFAULT NULL,
  `idMembro7` int(10) UNSIGNED DEFAULT NULL,
  `idMembro8` int(10) UNSIGNED DEFAULT NULL,
  `idMembro9` int(10) UNSIGNED DEFAULT NULL,
  `idMembro10` int(10) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensagem`
--

CREATE TABLE `mensagem` (
  `idUsuarioOrigem` int(10) UNSIGNED DEFAULT NULL,
  `idUsuarioDestino` int(10) UNSIGNED DEFAULT NULL,
  `idMensagem` int(10) UNSIGNED DEFAULT NULL,
  `txtMensagem` text NOT NULL,
  `dataMensagem` date DEFAULT NULL,
  `horaMensagem` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `mensagemgrupo`
--

CREATE TABLE `mensagemgrupo` (
  `idUsuarioOrigem` int(10) UNSIGNED DEFAULT NULL,
  `idGrupo` int(10) UNSIGNED DEFAULT NULL,
  `idMensagem` int(10) UNSIGNED DEFAULT NULL,
  `txtMensagem` text NOT NULL,
  `dataMensagem` date DEFAULT NULL,
  `horaMensagem` time DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE `usuario` (
  `id` int(5) UNSIGNED NOT NULL,
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
  ADD PRIMARY KEY (`id`),
  ADD KEY `idAdministrador` (`idAdministrador`),
  ADD KEY `idMembro1` (`idMembro1`),
  ADD KEY `idMembro2` (`idMembro2`),
  ADD KEY `idMembro3` (`idMembro3`),
  ADD KEY `idMembro4` (`idMembro4`),
  ADD KEY `idMembro5` (`idMembro5`),
  ADD KEY `idMembro6` (`idMembro6`),
  ADD KEY `idMembro7` (`idMembro7`),
  ADD KEY `idMembro8` (`idMembro8`),
  ADD KEY `idMembro9` (`idMembro9`),
  ADD KEY `idMembro10` (`idMembro10`);

--
-- Indexes for table `mensagem`
--
ALTER TABLE `mensagem`
  ADD KEY `idUsuarioOrigem` (`idUsuarioOrigem`),
  ADD KEY `idUsuarioDestino` (`idUsuarioDestino`);

--
-- Indexes for table `mensagemgrupo`
--
ALTER TABLE `mensagemgrupo`
  ADD KEY `idUsuarioOrigem` (`idUsuarioOrigem`),
  ADD KEY `idGrupo` (`idGrupo`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `grupo`
--
ALTER TABLE `grupo`
  ADD CONSTRAINT `grupo_ibfk_1` FOREIGN KEY (`idAdministrador`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_10` FOREIGN KEY (`idMembro9`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_11` FOREIGN KEY (`idMembro10`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_2` FOREIGN KEY (`idMembro1`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_3` FOREIGN KEY (`idMembro2`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_4` FOREIGN KEY (`idMembro3`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_5` FOREIGN KEY (`idMembro4`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_6` FOREIGN KEY (`idMembro5`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_7` FOREIGN KEY (`idMembro6`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_8` FOREIGN KEY (`idMembro7`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `grupo_ibfk_9` FOREIGN KEY (`idMembro8`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `mensagem`
--
ALTER TABLE `mensagem`
  ADD CONSTRAINT `mensagem_ibfk_1` FOREIGN KEY (`idUsuarioOrigem`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `mensagem_ibfk_2` FOREIGN KEY (`idUsuarioDestino`) REFERENCES `usuario` (`id`);

--
-- Limitadores para a tabela `mensagemgrupo`
--
ALTER TABLE `mensagemgrupo`
  ADD CONSTRAINT `mensagemgrupo_ibfk_1` FOREIGN KEY (`idUsuarioOrigem`) REFERENCES `usuario` (`id`),
  ADD CONSTRAINT `mensagemgrupo_ibfk_2` FOREIGN KEY (`idGrupo`) REFERENCES `grupo` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
