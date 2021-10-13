pipeline {
    agent any
    stages {
        stage("checkout code") {
            steps {
               echo "Running in docker"
	           git branch: 'main',
		           credentialsId: 'Github_Sanket',
                   url: 'https://github.com/Sbhalsing0/jenkins-terraform.git'
            }
        }

        stage("build and test the project") {
	   agent any 
	   stages {
               stage("build") {
                   steps {
                       sh "docker --version"
                   }
               }
               stage("test") {
                   steps {
                     withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'DockerhubPassword', usernameVariable: 'DockerhubUser')]) {
                       sh "docker login -u ${env.DockerhubUser} -p ${env.DockerhubPassword}"
                       }
                    }
               }
            }
        }
    }
}
