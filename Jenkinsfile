pipeline {
  agent any
  stages {
    stage('clean') {
      steps {
        dir('backend/mmart') {
          sh './gradlew clean'
        }
      }
    }
    stage('build') {
      steps {
        dir('backend/mmart') {
          sh './gradlew build'
        }
      }
    }
  }
}