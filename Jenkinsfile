pipeline {
  agent any
  stages {
    stage('clean') {
      steps {
        dir('backend/mmart') {
          sh 'chmod +x gradlew'
          sh './gradlew clean'
        }
      }
    }
    stage('build') {
      steps {
        dir('backend/mmart') {
          sh 'chmod +x gradlew'
          sh './gradlew build -x test'
        }
      }
    }
    stage('docker') {
      steps {
        dir('backend/mmart') {
          sh 'docker build -t red-limo-backend:latest .'
        }
      }
    }
    stage('deploy') {
      steps {
        dir('backend/mmart') {
          sh 'docker-compose up -d --build'
        }
      }
    }
  }
}