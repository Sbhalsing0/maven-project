pipeline {
    agent any
    stages {
        stage("checkout code") {
            steps {
               echo "Running in docker"
	           git branch: 'master',
		           //credentialsId: 'Github_Sanket',
                   url: 'https://github.com/Sbhalsing0/maven-project.git'
            }
        }

    stage("build and test the project") {
    agent {
       label "docker_slave"
    }
	   stages {
            stage("build") {
               steps {
                    sh "apk update"
		            sh "apk add maven"
			        sh "mvn clean install"
			        sh "apk add docker"
			        sh "docker build -t webapp ."
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
