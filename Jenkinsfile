pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests=false'
            }
        }
        stage('Test') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)'
                    dockerImage = docker.build("pedrojgonzalo/my-jenkins-created-image:${env.BUILD_NUMBER}")
                }
            }
        }
        stage('Push Image') {
          steps {
            script {
              docker.withRegistry('https://index.docker.io/v1/', 'dockerhub') {
                dockerImage.push()
              }
            }
          }
        }
    }
}
