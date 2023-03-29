pipeline {
    agent {
       label "docker_slave"
    }
    stages {
        stage("checkout code") {
            steps {
               echo "Running in docker"
	           git branch: 'main',
		           //credentialsId: 'Github_Sanket',
                   url: 'https://github.com/Sbhalsing0/maven-project.git'
            }
        }

        stage("build and test the project") {
	   agent any 
	   stages {
               stage("build") {
                   steps {
                       sh "apk update"
		               sh "apk add maven"
                   }
               }
               stage("test") {
                   steps {
                     //withCredentials([usernamePassword(credentialsId: 'Dockerhub', passwordVariable: 'DockerhubPassword', usernameVariable: 'DockerhubUser')]) {
                     //sh "docker login -u ${env.DockerhubUser} -p ${env.DockerhubPassword}"
		              sh "mvn clean install"
                    }
                }
            }
        }
    }
}
