pipeline {
  agent any
  stages {
    stage('clean project') {
      steps {
        dir('backend/mmart') {
          sh 'chmod +x gradlew'
          sh './gradlew clean'
        }
      }
    }
    stage('build project') {
      steps {
        dir('backend/mmart') {
          sh 'chmod +x gradlew'
          sh './gradlew build -x test'
        }
      }
    }
    stage('dockerize project') {
      steps {
        dir('backend/mmart') {
          sh 'mkdir -p target/dependency && (cd target/dependency; jar -xf ../*-SNAPSHOT.jar)'
          sh 'docker build -t red-limo-backend:latest .'
        }
      }
    }
  }
}