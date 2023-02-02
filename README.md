# Bataille Navale (2022-2023)
## Groupe

- Lazare K ASSIE
- Maximilien DENIS
- Ludivic GILOTAUX
- Alexandre Ernotte

## Introduction

Ce projet est développé dans le cadre du cours de "Programmation Orienté Objet" en Bac 2 Informatique de Gestion  à l'ESA. Ce dernier est exclusivement écrit en Java et doit répondre à certaines contraintes qui sont les suivantes :

- Implémenter un jeu de touché coulé Client - Serveur
- Utilisation du réseau
- Combat Client - Client
- Combat Client - Serveur
- Proposition de plusieurs plateaux de tailles différentes
- Possibilités de jouer à plusieurs : 2 - 3 - 4 joueurs
- Possibilités de jouer par équipes : 2 contre 2
- Possibilités de suivre des parties auxquelles on ne joue pas
- Partie avec des horloges

## Procédure d'exécution du programme

Veuillez utiliser POWERSHELL pour le confort d'exécution du projet.

```bash
git clone https://gitlab.com/denismaximilien98/bataille_navale.git
cd bataille_navale
```

Avant de lancer le serveur ou le client, il faut compiler l'ensemble des fichiers java. Pour cela, un fichier powershell se trouve dans le dossier exports et permet de supporter la compilation sur Linux ou Windows tant que l'outil javac est installé.

```powershell
cd exports
.\build.ps1
```

Une fois que c'est fait, l'ensemble des anciens fichiers compilés sont supprimés et les nouveaux sont créés dans le dossier exports avec la même structure que le projet. IL faut rester dans le dossier exports pour exécuter les commandes suivantes.

```powershell
.\server.ps1` # lance le serveur qui écoute sur le port constant 5000
.\client.ps1` # lance le client qui va se connecter au serveur
```

L'ensemble des logs du serveur se trouve dans `./data/server.log`.  
Le serveur et le client doivent chacun être exécuté dans un terminal.


## Les commandes client

Pour envoyer un message au serveur, il faut avant tout se connecter... Si vous souhaitez connaître la syntaxe de la commande à envoyer il suffit d'envoyer `/help`.

Voici une liste exhaustive des commandes disponibles :

|Commande|Description|
|-|-|
|`/help`|liste les commande disponibles|
|`/signin <username> <password>`|connexion à un utilisateur existant |
|`/signup <username> <password>`|création d'un nouvel utilisateur|
|`/ping`|ping le serveur|
|`/userlist`|liste l'ensemble des utilisateurs existants sur le serveur|
|`/invite <username>`|envoie une invitation à un utilisateur du serveur|
|`/confirm <username>`|confirme une invitation d'un utilisateur provenant du serveur|
|`/signout`|se déconnecte du serveur|

Faites attention que si vous êtes inactif plus de 5 minutes, le serveur vous déconnecte automatiquement.  
De plus, certaines commandes sont accessibles uniquement sous certaines conditions :

- Le rôle de l'utilisateur
- L'état de connexion du client

