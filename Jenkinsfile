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

        stage("build and test the project") {
            stages {
               stage("build") {
                   steps {
                       sh "terraform -version"
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
