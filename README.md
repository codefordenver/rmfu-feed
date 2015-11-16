# Feed

[![Stories in Ready](https://badge.waffle.io/codefordenver/rmfu-feed.png?label=ready&title=Ready)](https://waffle.io/codefordenver/rmfu-feed)

Feed is a community engagement platform developed by Code for Denver for the 
Rocky Mountain Farmer's Union.

## Configuration

Feed requires the following environment variables to be set to run properly.

````
MANDRILL_API_KEY=<api key here>
RMFU_FROM_EMAIL=<example@mail.com>
RMFU_SECRET=<some secret>
````
During development, `RMFU_SECRET` and `RMFU_FROM_EMAIL` are arbitrary and can 
be whatever you like. `MANDRILL_API_KEY` on the other hand requires a valid 
Mandrill API Token, to get this token contact one of Code For Denver's 
volunteers.

## Installation

Feed uses virtual machines to isolate development environments from a 
developers host machine. The following steps will help get Feed installed 
on a development machine.

1. Install [VirtualBox](https://www.virtualbox.org).
2. Install [Vagrant](https://www.vagrantup.com).
3. Run `vagrant plugin install vagrant-hostmanager` to install the [Vagrant Hostmanager Plugin](https://github.com/smdahlen/vagrant-hostmanager).
4. `git clone` this repository to your machine.
5. `cd` into the repository.
6. Then `vagrant up`.

Running `vagrant up` sets a domain record in the `/etc/hosts` file (which will 
will require your password, this can be made [passwordless](https://github.com/smdahlen/vagrant-hostmanager#passwordless-sudo)),downloads an Ubuntu image to your host machine and 
provisions it with [Clojure](http://clojure.org/) and 
[Leiningen](http://leiningen.org), and [MongoDB](https://www.mongodb.org).
Running `vagrant up` for the first time might take a couple minutes. If 
`vagrant up` errors out, run `vagrant status` to view the status of your
Vagrant machine, then use `vagrant up` or `vagrant provision` as needed to
complete the provisioning.

## Running the Application

The following steps will help get Feed running on the development machine.

1. `vagrant ssh` to login into the virtual machine.
2. `cd` into `/vagrant`, where the project is located.
3. To start the server run `lein run 3000`.
4. In your browser on your development machine browse to http://feed.rmfu.dev:3000.

## UI Development

UI Development can be made easier by using [Figwheel](https://github.com/bhauman/lein-figwheel). 
Replace steps 3 and 4 from the previous section with the following steps:

1. Start the figwheel server with `lein figwheel`.
2. In the browser navigate to http://feed.rmfu.dev:3449.
3. Open the javascript console and execute `(js/alert "Am I connected?")` to 
test your browser connected repl.

## Ending Vagrant Development Session

The following steps will help end your Vagrant session, spin down the virtual 
machine and free up resources on your development machine.

1. Run `exit` to exit the virtual machine.
2. `vagrant halt` to halt the machine, freeing up resources on your 
development machine.
