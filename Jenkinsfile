pipeline {
  environment {
    dockerimagename = "teqteqteqteq/red-limo-backend"
    dockerImage = ""
  }
  agent any
  tools {
    gradle '7.6.1'
    jdk 'jdk'
    dockerTool 'myDocker'
  }
  stages {
    stage('Build image') {
      steps{
        dir('backend/mmart') {
          script {
            sh "docker --version"
            dockerImage = docker.build dockerimagename
          }
        }
      }
    }
    stage('Push Image') {
      environment {
        registryCredential = 'dockerhub-teq'
      }
      steps{
        script {
          docker.withRegistry( 'https://registry.hub.docker.com', registryCredential ) {
            dockerImage.push("latest")
          }
        }
      }
    }
    stage('Deploy container to Kubernetes') {
      steps {
        script {
          kubernetesDeploy(configs: "deployment.yaml", "service.yaml")
        }
      }
    }
  }
}
