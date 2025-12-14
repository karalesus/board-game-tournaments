pipeline {
  agent any

  tools {
      maven 'maven-3.9'
    }

  options { timestamps() }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build contracts') {
      steps {
        sh '''
          set -e
          mvn -f events-contract/pom.xml clean install -DskipTests
          mvn -f board-game-tournaments-api/pom.xml clean install -DskipTests
        '''
      }
    }

    stage('Build services') {
      steps {
        sh '''
          set -e
          mvn -f board-game-tournaments-schedule-service/pom.xml clean package -DskipTests
          mvn -f analytics-service/pom.xml clean package -DskipTests
          mvn -f notification-service/pom.xml clean package -DskipTests
          mvn -f board-game-tournaments-gateway/pom.xml clean package -DskipTests
        '''
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true, allowEmptyArchive: true
    }
  }
}