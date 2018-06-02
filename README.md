## Distributed Flight Reservation System

A distributed system desigend to reserve and manage flight tickets from 3 different locations: Montreal, Washington and New Delhi. All servers are replicated and they are managed by a Replica Manager (RM). The Front End (FE) forwards the request to RM and RM sequences the processes to the servers. It also checks their heartbeat to see which server is down and has to be backed up to the replicated ones.

## What has been used?

Java, Java RMI and CORBA are 3 main technologies which are used in this project. The connection are handled by a UDP connection which has been modified to become reliable.



Copyright Amir Sadra Khorramizadeh