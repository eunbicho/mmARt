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

// pipeline {
//   environment {
//     dockerimagename = "teqteqteqteq/red-limo-backend"
//     dockerImage = ""
//   }
//   agent {
//     kubernetes {
//       yaml """
//         apiVersion: v1
//         kind: Pod
//         metadata:
//         labels:
//           component: ci
//         spec:
//           # Use service account that can deploy to all namespaces
//           serviceAccountName: devops-tools
//           containers:
//           - name: docker
//             image: docker
//             command:
//             - cat
//             tty: true
//           - name: golang
//             image: golang:1.10
//             command:
//             - cat
//             tty: true
//           - name: gcloud
//             image: gcr.io/cloud-builders/gcloud
//             command:
//             - cat
//             tty: true
//           - name: kubectl
//             image: gcr.io/cloud-builders/kubectl
//             command:
//             - cat
//             tty: true
//         """
//     }
//   }
//   // tools {
//   //   gradle "7.6.1"
//   //   dockerTool "myDocker"
//   //   jdk "jdk"
//   // }
//   stages {
//     stage('Build image') {
//       container('docker') {
//         sh "docker build -t $dockerimagename -f ./Dockerfile ."
//       }
//       // steps{
//       //   dir('backend/mmart') {
//       //     script {
//       //       sh "docker --version"
//       //       dockerImage = docker.build dockerimagename
//       //     }
//       //   }
//       // }
//     }
//     stage('Push Image') {
//       environment {
//         registryCredential = 'dockerhub-teq'
//       }
//       container('docker') {
//         withDockerRegistry([credentialsId: $registryCredential, url: "https://registry.hub.docker.com"]) {
//           docker.image("$dockerimagename:latest").push()
//         }
//       }
//       // steps{
//       //   script {
//       //     docker.withRegistry( 'https://registry.hub.docker.com', registryCredential ) {
//       //       dockerImage.push("latest")
//       //     }
//       //   }
//       // }
//     }
//     // stage('Deploy container to Kubernetes') {
//     //   steps {
//     //     script {
//     //       kubernetesDeploy(configs: "deployment.yaml", "service.yaml")
//     //     }
//     //   }
//     // }
//   }
// }
