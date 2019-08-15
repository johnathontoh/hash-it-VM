# Setting Up Oracle DB

1. Creating of VM on OpenStack
2. Downloading of Oracle XE
3. Setting up Oracle pre-installation steps
4. Installing Oracle 
5. Setting up SQL Developer
6. Creating User for work on Oracle DB

# Step 1: Creating of VM on Oracle DB
1. Get access to OpenStack (http://openstack/project/)
2. Launch a small instance (1 core of 20GB) with Centos 7.0
3. Associate Floating IP to it

# Step 2: Downloading Oracle XE
1. Download the Oracle XE rpm from https://www.oracle.com/technetwork/database/database-technologies/express-edition/downloads/index.html
2. Upload the rpm into the server

# Step 3: Setting up Oracle pre-installation steps
1. ssh into the VM created earlier with your private key as centos user
2. Run the commands (sudo groupadd -g 502 oinstall; sudo groupadd -g 503 dba; sudo groupadd -g 504 oper; sudo groupadd -g 505 asmadmin; sudo useradd -u 502 -g oinstall -G dba,asmadmin,oper -s /bin/bash -m oracle)
3. Set password for oracle user with sudo passwd oracle

# Step 4: Installing Oracle
1. Run yum -y localinstall oracle-database-xe-18c-1.0-1.x86_64.rpm
2. Run /etc/init.d/oracle-xe-18c configure and set the SYS, SYSTEM, and PDBADMIN password

# Step 5: Setting up SQL Developer
1. Download SQL Developer from https://www.oracle.com/technetwork/developer-tools/sql-developer/downloads/index.html (choose the one with JDK 8 included)

# Step 6: Creating User for work on Oracle DB
1. Login into the database with sys user, the password (set in Step 4), the IP (Floating IP that is associated with the VM created in Step 1) and Port 1521
2. Create the user with the sql (create user "user that you set" identified by "password that you set")
3. Give the user rights to edit the DB with the below sql (alter session set "_ORACLE_SCRIPT"=true; grant connect, resource to "user that you set"; grant create session to "user that you set"; grant unlimited tablespace to "user that you set"; grant all privileges to "user that you set")
