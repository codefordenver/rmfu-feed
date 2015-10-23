# RMFU

This repo contains both the server and the client 
code for Rocky Mountain Farmer Union project.

## setup

0. [Install leiningen](https://github.com/technomancy/leiningen)

1. Install mongodb: `$ brew install mongo`

2. Run mongodb deamon: `$ mongod` 
runs on default port

### run the server with:

    lein run 3000

There is no need to reload the server during development,
leinegen will reload your code automatically unless 
you add / remove a library.

# RMFU-UI


### Run interactive UI development environment with:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 