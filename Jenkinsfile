pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub')
    }
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
        stage('Login') {
            steps {
                script {
                    sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                }
            }
        }
        stage('Push') {
            steps {
                script {
                    dockerImage.push()
                }
            }
        }
        stage('Deploy to k8s') {
            steps {
                script {
                    sh 'kind load docker-image pedrojgonzalo/my-jenkins-created-image:$BUILD_NUMBER -n development'
                    // ojo con la sintaxis de sed dependiendo del sistema operativo donde se ejecuta jenkins!! MAC:
                    sh "sed -i '' 's|IMAGE_NAME|pedrojgonzalo/my-jenkins-created-image:$BUILD_NUMBER|' deploymentservice.yaml"
                    sh 'kubectl apply -f deploymentservice.yaml'
                }
            }
        }
        stage('Remove Docker Image') {
            steps {
                script {
                    sh 'docker rmi pedrojgonzalo/my-jenkins-created-image:${env.BUILD_NUMBER}'
                }
            }
        }
    }
    post {
        always {
            
            script {
                sh 'docker logout'
            }
            
        }
    }
}
