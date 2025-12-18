Vagrant.configure("2") do |config|
  # Box Ubuntu
  config.vm.box = "ubuntu/jammy64"

  # VM Jenkins
  config.vm.define "jenkins" do |jenkins|
    jenkins.vm.hostname = "jenkins.local"
    jenkins.vm.network "private_network", ip: "192.168.56.10"
    jenkins.vm.provider "virtualbox" do |vb|
      vb.name = "ci-jenkins"
      vb.memory = 3072
      vb.cpus = 2
    end
    jenkins.vm.provision "shell", inline: <<-SHELL
      sudo apt-get update
      # Java (JDK 17)
      sudo apt-get install -y openjdk-17-jdk git curl
      # Maven
      sudo apt-get install -y maven
      # Docker
      sudo apt-get install -y ca-certificates gnupg lsb-release
      sudo mkdir -p /etc/apt/keyrings
      curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
      echo \
        "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
        $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
      sudo apt-get update
      sudo apt-get install -y docker-ce docker-ce-cli containerd.io
      sudo usermod -aG docker vagrant
      # Jenkins
     # Jenkins installation (correct version)
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | \
  sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt-get update
sudo apt-get install -y jenkins
sudo systemctl enable jenkins
sudo systemctl start jenkins

      # Autoriser Jenkins à utiliser Docker
      sudo usermod -aG docker jenkins
      sudo systemctl restart jenkins
    SHELL
  end

  # VM App (Prod-like)
  config.vm.define "app" do |app|
    app.vm.hostname = "app.local"
    app.vm.network "private_network", ip: "192.168.56.20"
    app.vm.provider "virtualbox" do |vb|
      vb.name = "cd-app"
      vb.memory = 2048
      vb.cpus = 2
    end
    app.vm.provision "shell", inline: <<-SHELL
      sudo apt-get update
      # Installer Docker
      sudo apt-get install -y ca-certificates gnupg lsb-release curl
      sudo mkdir -p /etc/apt/keyrings
      curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
      echo \
        "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
        $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
      sudo apt-get update
      sudo apt-get install -y docker-ce docker-ce-cli containerd.io
      sudo usermod -aG docker vagrant
    SHELL
  end
end
