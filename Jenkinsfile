pipeline {
    agent any

    environment {
        IMAGE_NAME = 'mezin139/finsecure-pay-api'
        DOCKER_CREDENTIALS_ID = '3c71c5a4-56a6-48c6-9b77-4e714b4e8c72'
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

        stage('Container Security Scan') {
            steps {
                sh "/opt/homebrew/bin/trivy image --severity HIGH,CRITICAL --exit-code 1 ${IMAGE_NAME}:${BUILD_NUMBER}"
            }
        }

        stage('Push to Docker Hub') {
                    steps {
                        withCredentials([usernamePassword(credentialsId: "${DOCKER_CREDENTIALS_ID}", usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                            sh "echo ${DOCKER_PASS} | /usr/local/bin/docker login -u ${DOCKER_USER} --password-stdin"
                            sh "/usr/local/bin/docker push ${IMAGE_NAME}:${BUILD_NUMBER}"
                            sh "/usr/local/bin/docker push ${IMAGE_NAME}:latest"
                        }
                    }
                }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "sed -i '' 's|mezin139/finsecure-pay-api:v1.0.2|${IMAGE_NAME}:${BUILD_NUMBER}|g' k8s/deployment.yaml"
                    sh '/usr/local/bin/kubectl apply -f k8s/'
                }
            }
        }
    }

 post {
        success {
            emailext body: "SUCCESS: Build #${env.BUILD_NUMBER} passed successfully.\n\nDetails: ${env.BUILD_URL}",
                     subject: "Jenkins Build Success: ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                     to: 'mezinphyu@gmail.com'
        }
        failure {
            emailext body: "FAILURE: Build #${env.BUILD_NUMBER} failed. Check console output: ${env.BUILD_URL}console",
                     subject: "Jenkins Build FAILED: ${env.JOB_NAME} - #${env.BUILD_NUMBER}",
                     to: 'mezinphyu@gmail.com'
        }
    }
}