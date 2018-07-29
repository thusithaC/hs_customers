# Customers rest api

Based on https://github.com/heroku/scala-getting-started.git

## Running Locally

Make sure you have Play and sbt installed.  Also, install the [Heroku Toolbelt](https://toolbelt.heroku.com/).

```sh
$ sbt compile
$ sbt run
```

Your app should now be running on [localhost:9000](http://localhost:9000/).

## Deploying to Heroku

```sh
$ heroku create
$ git push heroku master
$ heroku open
```

## Setting up a database

A MySql database should be set up.
It could be hosted locally or in Heroku or any other cloud provider. The application.conf file should be updated based on the connection string and the username/password
Database schema is in scripts/db_schema.txt file as an insert table SQL statement.