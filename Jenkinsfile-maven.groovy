pipeline {
  agent {
    kubernetes {
    defaultContainer 'slave'
      yaml """
        apiVersion: v1
        kind: Pod
        spec:
          serviceAccountName: jenkins
          containers:
          - name: slave
            image: sbhalsing0/jenkins-docker-desktop:v1.4
            command:
            - cat
            securityContext: 
              privileged: true     
            tty: true
            volumeMounts:
             - mountPath: /var/run/docker.sock
               name: docker-sock
          volumes:
          - name: docker-sock
            hostPath:
              path: /var/run/docker.sock
              type: Socket
        """
      }
  }
  environment {
        DOCKER_USERNAME = credentials('docker-id') // Replace with your Docker Hub username credential ID
        DOCKER_PASSWORD = credentials('docker-id') // Replace with your Docker Hub password credential ID
  }
  options {
    ansiColor('xterm')
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '40', daysToKeepStr: '14', artifactDaysToKeepStr: '7', artifactNumToKeepStr: '10'))
    timeout(time: 3, unit:'HOURS')
  }
    stages {
        stage('Login to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'docker-creds', variable: 'DOCKER_PASSWORD')]) {
                    script {
                        // Login to Docker Hub using PAT
                        sh 'export DOCKER_HOST=tcp://44.200.159.148:2375'
                        sh 'docker -H tcp://44.200.159.148:2375 login -u sbhalsing0 -p $DOCKER_PASSWORD'
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    currentBuild.displayName = "#${BUILD_NUMBER} - Build Image"
                    currentBuild.description = 'Image to build '
                }
                sh '''
                echo $BUILD_ID
                mvn clean install -DskipTests=false
                mvn package fabric8:build
                '''
            }
        }
        stage('Release') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-id', 
                            usernameVariable: 'DOCKER_USERNAME', 
                            passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh 'echo $DOCKER_USERNAME'
                        sh 'echo $DOCKER_PASSWORD'                            
                    }
                     sh 'export DOCKER_HOST=tcp://44.200.159.148:2375'
                     sh '''
                        docker -H tcp://44.200.159.148:2375 ps
                        docker -H tcp://44.200.159.148:2375 build -t maven:$BUILD_ID -f Dockerfile .
                        docker -H tcp://44.200.159.148:2375 tag maven:$BUILD_ID sbhalsing0/maven:$BUILD_ID 
                        docker -H tcp://44.200.159.148:2375 push sbhalsing0/maven:$BUILD_ID
                        kubectl version --client
                        sed -i "s/tag/${BUILD_ID}/g" k8s-deployment.yaml
                        cat  k8s-deployment.yaml
                        kubectl apply -f k8s-deployment.yaml
                        kubectl get pods
                    '''
                }
            }
        }
    }
}
