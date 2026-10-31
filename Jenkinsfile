pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Test') {
            steps {
                // Using absolute path for Homebrew Maven on macOS
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
                script {
                    appImage = docker.build("zinmeme/finsecure-pay-api:${BUILD_NUMBER}")
                    appImage.tag("latest")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        appImage.push()
                        appImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    sh "sed -i '' 's|<your-dockerhub-username>/finsecure-pay-api:v1.0.0|zinmeme/finsecure-pay-api:${BUILD_NUMBER}|g' k8s/deployment.yaml"
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