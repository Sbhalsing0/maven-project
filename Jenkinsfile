pipeline {
    agent any

    stages {
        stage('Build') {
            agent { label 'docker_slave_mvn' }
            steps {
                sh 'ls -ltr'
            }
        }
    }

    post {
        success {
            echo 'This will run only if successful'
        }
    }
}
