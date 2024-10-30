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
  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '40', daysToKeepStr: '14', artifactDaysToKeepStr: '7', artifactNumToKeepStr: '10'))
    timeout(time: 3, unit:'HOURS')
  }
    stages {
        stage('Build') {
            steps {
                script {
                    currentBuild.displayName = "#${BUILD_NUMBER} - Build Image"
                    currentBuild.description = 'Image to build '
                }
                sh '''
                echo $BUILD_ID
                mvn clean install -DskipTests=false
                '''
            }
        }
        stage('Release') {
            steps {
                sh 'export DOCKER_HOST=tcp://3.234.205.203:2375'
                sh '''
                    docker -H tcp://3.234.205.203:2375 ps
                    docker -H tcp://3.234.205.203:2375 build -t maven:$BUILD_ID -f Dockerfile .
                    docker -H tcp://3.234.205.203:2375 tag maven:$BUILD_ID sbhalsing0/maven:$BUILD_ID 
                    docker -H tcp://3.234.205.203:2375 push sbhalsing0/maven:$BUILD_ID
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
