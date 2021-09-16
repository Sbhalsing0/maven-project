pipeline {
    agent any

    stages {
        stage('Build') {
            agent { label 'docker_slave' }
            steps {
                sh 'mvn clean packages'
            }
        }
    }

    post {
        success {
            echo 'This will run only if successful'
        }
    }
}
