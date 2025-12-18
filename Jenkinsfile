pipeline {
  agent any

  environment {
    APP_NAME = 'demo'
    IMAGE_TAG = "demo:${env.BUILD_NUMBER}"
    REGISTRY = '' // vide si local; sinon registry privé
    DEPLOY_HOST = '192.168.56.20'
    DEPLOY_USER = 'vagrant'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn -version'
        sh 'mvn -B clean test'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Package') {
      steps {
        sh 'mvn -B -DskipTests package'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${IMAGE_TAG} ."
      }
    }

    stage('Push (optional)') {
      when {
        expression { return env.REGISTRY?.trim() }
      }
      steps {
        sh """
          docker tag ${IMAGE_TAG} ${REGISTRY}/${IMAGE_TAG}
          docker push ${REGISTRY}/${IMAGE_TAG}
        """
      }
    }

    stage('Deploy to App VM') {
      steps {
        // Transférer l'image via save/load si pas de registry
        sh """
          docker save ${IMAGE_TAG} | bzip2 > image.tar.bz2
          scp -o StrictHostKeyChecking=no image.tar.bz2 ${DEPLOY_USER}@${DEPLOY_HOST}:/home/${DEPLOY_USER}/image.tar.bz2
          ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_HOST} 'bunzip2 -f /home/${DEPLOY_USER}/image.tar.bz2 && sudo docker load -i /home/${DEPLOY_USER}/image.tar'
          ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_HOST} 'sudo docker rm -f demo || true'
          ssh -o StrictHostKeyChecking=no ${DEPLOY_USER}@${DEPLOY_HOST} 'sudo docker run -d --name demo -p 8080:8080 ${IMAGE_TAG}'
        """
      }
    }
  }

  post {
    success {
      echo "Deployed ${IMAGE_TAG} to ${DEPLOY_HOST}"
    }
    failure {
      echo "Pipeline failed"
    }
  }
}
