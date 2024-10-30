pipeline {
  agent {
    kubernetes {
    defaultContainer 'slave'
      yaml """
        apiVersion: v1
        kind: Pod
        spec:
          containers:
          - name: slave
            image: sbhalsing0/jenkins-docker-desktop:v1.2
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
    stages {
        stage('Build') {
            steps {
              sh 'echo $BUILD_ID'
              sh 'java -version'
              sh 'ls -la'
            }
        }
        stage('Release') {
            steps {
                sh 'export DOCKER_HOST=tcp://44.197.215.6:2375'
                sh 'docker -H tcp://44.197.215.6:2375 ps'
            }
        }
    }
}
