pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                mvn clean package
                docker build -t myapp:$(env.BUILD_ID) .
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
