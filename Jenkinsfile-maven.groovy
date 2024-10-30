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
                sh 'export DOCKER_HOST=tcp://3.234.205.203:2375'
                sh 'docker -H tcp://3.234.205.203:2375 ps'
                sh 'docker build -t maven:$BUILD_ID'
            }
        }
    }
}
