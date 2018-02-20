#!/usr/bin/env bash

clear

echo "running crawler from sconnolly....."

./mvnw clean package -DskipTests

java -jar target/webcrawler-0.0.1-SNAPSHOT.jar http://suirtech.com

echo "crawler finished!"

cat sitemap.txt

echo "to run crawl another site run ::>> java -jar target/webcrawler-0.0.1-SNAPSHOT.jar SITE_URL"