pipeline {
    agent any
    environment {
        PROJECT_ID = 'mool-406305'
        CLUSTER_NAME = 'mool-cluster-k8s'
        LOCATION = 'asia-northeast3-a'
        CREDENTIALS_ID = '614438f5-f656-4ae6-8872-2d0ba0605760'
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
                    // Spring Boot 프로젝트 빌드
                    sh './gradlew clean build --warning-mode all'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Docker 이미지 빌드
                    myapp = docker.build("jiwonlee42/spring-boot:${env.BUILD_ID}", ".")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Docker 이미지를 Docker Hub로 푸시
                    docker.withRegistry('https://registry.hub.docker.com', 'jiwonlee42') {
                        myapp.push("latest")
                        myapp.push("${env.BUILD_ID}")
                    }
                }
            }
        }

        stage('Deploy to GKE') {
            when {
                branch 'main'
            }
            steps {
                sh "sed -i 's/spring-boot:latest/spring-boot:${env.BUILD_ID}/g' deployment.yaml"
                step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME,
                location: env.LOCATION, manifestPattern: 'deployment.yaml', credentialsId: env.CREDENTIALS_ID,
                verifyDeployments: true])
            }
        }

        stage('Stop and Remove Existing Container') {
            steps {
                script {
                    // 기존에 동작 중인 컨테이너 중지 및 삭제
                    sh 'docker ps -q --filter "name=spring-boot-server" | grep -q . && docker stop spring-boot-server && docker rm spring-boot-server || true'
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                script {
                    // Docker 컨테이너 실행
                    sh 'docker run -p 8081:8080 -d --name=spring-boot-server jiwonlee42/spring-boot:${env.BUILD_ID}'
                }
            }
        }

        stage('Clean Up Unused Docker Images') {
            steps {
                script {
                    // 태그가 겹친 이미지 삭제
                    sh 'docker rmi -f $(docker images -f "dangling=true" -q) || true'
                }
            }
        }
    }
}
