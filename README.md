cloudproject
============

Cloud Project

1.Install tomcat:
Download http://mirrors.devlib.org/apache/tomcat/tomcat-7/v7.0.53/bin/apache-tomcat-7.0.53.zip

2.Install mongoDB:
a.sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 7F0CEB10
b.echo 'deb http://downloads-distro.mongodb.org/repo/ubuntu-upstart dist 10gen' | sudo tee /etc/apt/sources.list.d/mongodb.list
c.sudo apt-get update
d.sudo apt-get install mongodb-org

3.Config data base connection:
config cloudproject/WEB-INF/classes/config.properties file:
sourceImagePath=/home/hduser/ximages/ 
mongoDB.fingerprint=mongodb://10.42.0.117/photo.fingerprint
mongoDB.output=mongodb://10.42.0.117/photo.out
mongoDB.searchResult=mongodb://10.42.0.117/photo.searchResult
mongoDB.statistic=mongodb://10.42.0.117/photo.statistic
collection.searchResult=searchResult
collection.fingerprint=fingerprint
HOST=10.42.0.117
dbName=photo

4.Copy cloudproject folder into tomcat webapps folder

5.Execute /home/hduser/apache-tomcat-7.0.53/bin/startup.sh to run Tomcat server.

7.Visit localhost:8080/cloudproject in the browser. Then you can use handsome face website to do similiar photo search.

