## CL-Reddit
## (Command-Line)

### Description

CL-Reddit is a basic implementation of a reddit-style message board available
over the command line. The server can be hosted to allow an unlimited number of
connections via Java Sockets. Users can perform account functions (Login,
Register) and registered users are able to create, read and upvote posts. Upvotes can be
seen on the list of posts to show what's popular in the community.

![image](https://github.com/Abbas-Rizvi/CL-Reddit/assets/73917749/1b34d1d3-f3d3-457b-89e0-13d40503de67)
![image](https://github.com/Abbas-Rizvi/CL-Reddit/assets/73917749/d8cc6f31-2187-4249-8139-1eb030109a6a)


### Technology

The CL-Reddit application used Java sockets and a mutlithreaded server for
handling connections. All user data is stored securely in an encrypted SQLite
database on the server to reasure users of their security in the event of a
cyber threat.


### How To Run

To run CL-Reddit, conduct the following steps:

1. Clone repository on target device

Includes files for both server and clients.

`git clone https://github.com/Abbas-Rizvi/CL-Reddit.git`

2. Run the target file via command line

`javac`
