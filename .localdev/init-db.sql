-- develop database
CREATE ROLE "user" WITH LOGIN;
CREATE ROLE admin WITH SUPERUSER;
CREATE DATABASE develop WITH OWNER postgres ENCODING 'UTF8';
grant all privileges on database develop to postgres, admin;