pipeline {
    agent none

    stages {
        stage('Build') {
            agent { label 'docker_slave' }
            steps {
                echo 'Building..'
            }
        }
    }

    post {
        success {
            echo 'This will run only if successful'
        }
    }
}
