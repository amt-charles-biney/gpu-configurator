pipeline {
    agent any

    stages {
        stage('Build With Maven') {
            when {
                anyOf {
                    branch 'PresentationLayDev'
                    branch 'staging'
                }
            }
            steps {
                sh './mvnw wrapper:wrapper'
            }
        }

        stage('Build Docker Image') {
            when {
                anyOf {
                    branch 'PresentationLayDev'
                    branch 'staging'
                }
            }
            steps {
                sh 'docker build -t amalitechservices/gpu-configurator:latest .'
            }
        }

        stage('Login to Docker Hub') {
            when {
                anyOf {
                    branch 'PresentationLayDev'
                    branch 'staging'
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'DOCKER_HUB_CREDENTIALS', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                }
            }
        }

        stage('Push Docker Image to Registry') {
            when {
                anyOf {
                    branch 'staging'
                    branch 'PresentationLayDev'
                }
            }
            steps {
                sh 'docker push amalitechservices/gpu-configurator:latest'
            }
        }

        stage('Deploy') {
            when {
                anyOf {
                    branch 'PresentationLayDev'
                    branch 'staging'
                }
            }
            steps {
                script {
                    withCredentials([
                        file(credentialsId: 'EC2_SSH_KEY', variable: 'EC2_SSH_KEY'),
                        usernamePassword(credentialsId: 'EC2_CRED', usernameVariable: 'USERNAME', passwordVariable: 'HOST_IP')]) {
                        sh 'cp $EC2_SSH_KEY ./sshkey'
                        sh 'chmod 400 sshkey'
                        sh """
                            ssh -i "sshkey" -o StrictHostKeyChecking=no $USERNAME@$HOST_IP '
                            cd /home/ubuntu/gpu-configurator
                            docker system prune --force
                            docker pull amalitechservices/gpu-configurator:latest
                            docker rm -f gpu-configurator
                            docker run -d -p 8081:8081 --name gpu-configurator amalitechservices/gpu-configurator:latest
                            docker logout '
                        """
                        }
                }
            }
        }

        stage('CleanUp') {
            steps {
                cleanWs()
            }
        }
    }
}
