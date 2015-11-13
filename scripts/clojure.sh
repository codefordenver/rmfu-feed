# Accept Oracle's License
echo Installing Java...
sudo apt-get -y install default-jdk

# Install Leiningen
echo Installing Leiningen
wget https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
chmod a+x lein
sudo mv lein /usr/local/bin