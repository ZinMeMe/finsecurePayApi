pipeline {
    agent any

    environment {
        IMAGE_NAME = 'zinmeme/finsecure-pay-api'
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Test') {
            steps {
                sh '/opt/homebrew/bin/mvn clean test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Security & Quality Check') {
            steps {
                sh '/opt/homebrew/bin/mvn verify -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "/usr/local/bin/docker build -t ${IMAGE_NAME}:${BUILD_NUMBER} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${IMAGE_NAME}:${BUILD_NUMBER}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "sed -i '' 's|<your-dockerhub-username>/finsecure-pay-api:v1.0.0|${IMAGE_NAME}:${BUILD_NUMBER}|g' k8s/deployment.yaml"
                    sh 'kubectl apply -f k8s/'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully! FinSecure Pay API deployed.'
        }
        failure {
            echo 'Pipeline failed. Deployment halted.'
        }
    }
}