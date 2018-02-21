#!/usr/bin/env bash

clear

domain=$1

if [[ $# -eq 0 ]] ; then
    echo '*********************************************'
    echo 'no site passed to script, defaulting to suirtech.com'
    domain='http://suirtech.com'
    echo "usage is  ./run_cawler url_to_crawl"
    echo '*********************************************'
fi

echo "running crawler at  $domain "

echo "building carwler"
./mvnw clean package -DskipTests

java -jar target/webcrawler-0.0.1-SNAPSHOT.jar ${domain}

echo "crawler finished!"

if [ ${HOSTNAME} = "ip-10-10-208-213" ]; then
    cp sitemap.txt /var/www/html/sitemap.txt
    echo "map available @ http://crawler.shaneconnolly.io/sitemap.txt"
else
    cat sitemap.txt
fi
