pipeline {
  environment {
    dockerimagename = "teqteqteqteq/red-limo-backend"
    dockerImage = ""
  }
  agent any
  tools {
    gradle '7.6.1'
    dockerTool 'docker'
  }
  stages {
    stage('Checkout Source') {
      steps {
        sh 'gradle --version'
        sh 'docker --version'
        git 'https://lab.ssafy.com/s08-ai-image-sub2/S08P22A401.git'
      }
    }
    stage('Build image') {
        steps{
            script {
                dockerImage = docker.build dockerimagename
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
