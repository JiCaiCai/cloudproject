cloudproject
============

Index:

1.Install tomcat

2.Install mongoDB

3-6. Deploy the project (java parts)

7. Install LVS + Keepalived

8. Install XfreemFS

9. NTP - sync the time



Our project website: http://10.42.0.246:10001/cloudproject
To visit out of CS-Dpt. Network
Plz ssh username@gatekeeper.cs.hku.hk
And type ssh -L 12345:10.42.0.246:10001 -g student@202.45.128.135
Then you can visit http://gatekeeper.cs.hku.hk:12345/cloudproject

Our Github source code:  https://github.com/JiCaiCai/cloudproject



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



6.Visit localhost:8080/cloudproject in the browser.





7. Install LVS + Keepalived on Student37 and Student 55



sudo apt-get install ipvsadm

sudo apt-get install keepalived



The server config file is



    ync_group VG1 {

    group {

        VI_1

    }

}



vrrp_instance VI_1 {

    state MASTER

    interface xenbr0

    virtual_router_id 51

    priority 100

    advert_int 1

    authentication {

        auth_type PASS

        auth_pass 1111

    }

    virtual_ipaddress {

        10.42.0.246

    }

}



virtual_server 10.42.0.246 10001 {

    delay_loop 6

    lb_algo rr

    lb_kind DR

    persistence_timeout 300

    protocol TCP



    real_server 10.42.0.130 10001 {

        weight 500

        TCP_CHECK {

            connect_timeout 3

        }

    }

    real_server 10.42.0.131 10001 {

        weight 100

        TCP_CHECK {

            connect_timeout 3

        }

    }

    real_server 10.42.0.184 10001 {

        weight 100

        TCP_CHECK {

            connect_timeout 3

        }

    }

    real_server 10.42.0.185 10001 {

        weight 100

        TCP_CHECK {

            connect_timeout 3

        }

    }

    real_server 10.42.0.118 10001 {

        weight 100

        TCP_CHECK {

            connect_timeout 3

        }

    }

    real_server 10.42.0.119 10001 {

        weight 100

        TCP_CHECK {

            connect_timeout 3

        }

    }

}



Note, the student55 should change to BACKUP



For client machine, need to add lines as following to rc.local to bind the VIP and stop the ARP responding



ifconfig lo:0 10.42.0.246 netmask 255.255.255.255 broadcast 10.42.0.246 up

sudo route add -host 10.42.0.246 dev lo:0



sudo echo "1" >/proc/sys/net/ipv4/conf/lo/arp_ignore

sudo echo "2" >/proc/sys/net/ipv4/conf/lo/arp_announce

sudo echo "1" >/proc/sys/net/ipv4/conf/all/arp_ignore

sudo echo "2" >/proc/sys/net/ipv4/conf/all/arp_announce





8. Install XfreemFS



Add a file named xfs.list at /etc/apt/sources.list.d as following



(For Client)

deb http://download.opensuse.org/repositories/home:/xtreemfs/xUbuntu_10.04 ./



(For Server)

deb http://download.opensuse.org/repositories/home:/xtreemfs/xUbuntu_12.04 ./



wget -q http://download.opensuse.org/repositories/home:/xtreemfs/xUbuntu_13.10/Release.key -O - | sudo apt-key add -



sudo apt-get update

sudo apt-get install xtreemfs-client

sudo apt-get install xtreemfs-serve



Then, do some config work.



At server side:

The META,DIR,OSD server should execute

/etc/init.d/xtreemfs-dir start



/etc/init.d/xtreemfs-mrc start



/etc/init.d/xtreemfs-osd start



At client side:

The client should use 

mount.xtreemfs student37/ximages ~/ximages # mount the fs



9. NTP

Each machine should sync the time with a centred server and we choose student37 as this server.



For each installation

sudo apt-get install ntp



Student37 is allowed sync with up-level server *.ubuntu.pool.ntp.org

Other clients are only allowed sync with student37



The configuration of student37 is as following

 

# /etc/ntp.conf, configuration for ntpd; see ntp.conf(5) for help



driftfile /var/lib/ntp/ntp.drift





# Enable this if you want statistics to be logged.

#statsdir /var/log/ntpstats/



statistics loopstats peerstats clockstats

filegen loopstats file loopstats type day enable

filegen peerstats file peerstats type day enable

filegen clockstats file clockstats type day enable



# Specify one or more NTP servers.



# Use servers from the NTP Pool Project. Approved by Ubuntu Technical Board

# on 2011-02-08 (LP: #104525). See http://www.pool.ntp.org/join.html for

# more information.

server 0.ubuntu.pool.ntp.org

server 1.ubuntu.pool.ntp.org

server 2.ubuntu.pool.ntp.org

server 3.ubuntu.pool.ntp.org



# Use Ubuntu's ntp server as a fallback.

server ntp.ubuntu.com

server 127.127.1.0

fudge 127.127.1.0 stratum 8





# Access control configuration; see /usr/share/doc/ntp-doc/html/accopt.html for

# details.  The web page <http://support.ntp.org/bin/view/Support/AccessRestrictions>

# might also be helpful.

#

# Note that "restrict" applies to both servers and clients, so a configuration

# that might be intended to block requests from certain clients could also end

# up blocking replies from your own upstream servers.



# By default, exchange time with everybody, but don't allow configuration.

restrict -4 default kod notrap nomodify nopeer noquery

restrict -6 default kod notrap nomodify nopeer noquery



# Local users may interrogate the ntp server more closely.

restrict 127.0.0.1

restrict ::1

restrict 10.42.0.0 mask 255.255.255.0 nomodify notrap

# Clients from this (example!) subnet have unlimited access, but only if

# cryptographically authenticated.

#restrict 192.168.123.0 mask 255.255.255.0 notrust





# If you want to provide time to your local subnet, change the next line.

# (Again, the address is an example only.)

#broadcast 192.168.123.255



# If you want to listen to time broadcasts on your local subnet, de-comment the

# next lines.  Please do this only if you trust everybody on the network!

#disable auth

#broadcastclient

