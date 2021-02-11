pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        withMaven(maven: 'maven-3.6.0', jdk: 'OpenJDK-11') {
          sh 'mvn verify'
        }

      }
    }

  }
}