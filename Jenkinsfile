pipeline {
  agent {
    label "docker_slave_mvn"
  }
  stages {
    stage("checkout code") {
      steps {
        echo "Running in docker"
        git branch: 'main',
          credentialsId: 'Github_Sanket',
          url: 'https://github.com/Sbhalsing0/jenkins-terraform.git'
      }
    }
    stages {
      stage('Maven Install') {
        agent {
          docker {
            image 'maven:3.5.0'
          }
        }
        steps {
          sh 'mvn clean install'
        }
      }
      stage('Docker Build') {
        agent any
        steps {
          sh 'docker build -t myapp:latest .'
        }
      }
      stage('Docker Push') {
        agent any
        steps {
          withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'DockerhubPassword', usernameVariable: 'DockerhubUser')]) {
            sh "docker login -u ${env.DockerhubUser} -p ${env.DockerhubPassword}"
          }
        }
      }
    }
  }
