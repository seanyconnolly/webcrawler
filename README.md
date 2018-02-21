# SC WebCrawler

to run on linux ( java installed )

chmod a+x ./run_crawler.sh

./run_crawler.sh


theres a small site added in script by default

to crawl any other site

#### java -jar target/webcrawler-0.0.1-SNAPSHOT.jar SITE_URL

#### Results are in sitemap.txt

## Remote Execution

also if you don't have java or linux based system 

### you can ssh to ubuntu@crawler.shaneconnolly.io
with the private key shared in email 

follow the terminal instructions then.



## Logic:

- get all links from domain given
- add domain to set of visited links
- while unvisited has links, take a link from unvisited and get all its links
- if link follows rules and is not in unvisited or visited sets
- add it to unvisited

- there is a max loop count too of MAX_PAGES_TO_LOAD = 2000


### Would be nice to have:
- Tests, they are not completed yet.
- endpoint to execute crawler over http
 - returns sitemap
 
