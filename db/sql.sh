#!/bin/bash

echo "Please enter root user MySQL password!"
echo "Note: password will be hidden when typing"
mysql -u root -p -e "CREATE DATABASE RASBET"
mysql -u root -p -e "CREATE USER 'utilizador'@'127.0.0.1' IDENTIFIED BY 'Bdli42122!';"
mysql -u root -p -e "GRANT ALL PRIVILEGES ON RASBET.* TO 'utilizador'@'127.0.0.1';"
mysql -u root -p RASBET < RASBET.sql
mysql -u root -p RASBET < populate.sql