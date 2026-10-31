pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials' // Configure this in Jenkins Credentials
        IMAGE_NAME = 'mezin139/finsecure-pay-api'           // Replace with your Docker Hub username
        KUBECONFIG_CREDENTIALS_ID = 'kubeconfig-cred'    // Optional if using cluster local agent, or configure accordingly
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

  stage('Build & Unit Test') {
      steps {
          withMaven(maven: 'Maven 3') {
              sh 'mvn clean test'
          }
      }
  }

        stage('Security & Quality Check') {
            steps {
                // Example using OWASP Dependency-Check or Maven verify security plugin
                sh 'mvn verify -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    appImage = docker.build("${IMAGE_NAME}:${BUILD_NUMBER}")
                    appImage.tag("latest")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        appImage.push()
                        appImage.push("latest")
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Update image tag dynamically in Kubernetes deployment or apply manifests
                    sh "sed -i 's|mezin139/finsecure-pay-api:v1.0.0|${IMAGE_NAME}:${BUILD_NUMBER}|g' k8s/deployment.yaml"
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