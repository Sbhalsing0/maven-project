pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                mvn clean package
                docker build -t myapp .
                d
            }
        }
        stage('Demo') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
