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
          sh './gradlew build'
        }
      }
    }
  }
}