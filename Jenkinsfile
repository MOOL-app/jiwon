pipeline {
    agent any
    environment {
        PROJECT_ID = 'mool-406305'
        CLUSTER_NAME = 'mool-cluster-k8s'
        LOCATION = 'asia-northeast3-a'
        CREDENTIALS_ID = '8745ddc9-1a45-42ec-9b0c-8f9d7050cb7d'
        DOCKER_IMAGE_TAG = "${env.BUILD_ID}"
        DOCKER_IMAGE_NAME = "jiwonlee42/spring-boot"
    }
    stages {
        stage("Checkout code") {
            steps {
                checkout scm
            }
        }

        stage('Build Spring Boot Project') {
            steps {
                script {
                    sh './gradlew clean build --warning-mode all'
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    // Docker 이미지 빌드
                    def dockerImage = docker.build("${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}", ".")

                    // Docker 이미지를 Docker Hub로 푸시
                    docker.withRegistry('https://registry.hub.docker.com', 'jiwonlee42') {
                        dockerImage.push("latest")
                        dockerImage.push("${DOCKER_IMAGE_TAG}")
                    }
                }
            }
        }

        stage('Deploy to GKE') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh "sed -i 's|${DOCKER_IMAGE_NAME}:latest|${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}|g' deployment.yaml"
                    step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME,
                    location: env.LOCATION, manifestPattern: 'deployment.yaml', credentialsId: env.CREDENTIALS_ID,
                    verifyDeployments: true])
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    sh 'docker run -p 8081:8080 -d --name=spring-boot-server ${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}'
                }
            }
        }

        stage('Clean Up Unused Docker Images') {
            steps {
                script {
                    sh 'docker rmi -f $(docker images -f "dangling=true" -q) || true'
                }
            }
        }
    }
}
