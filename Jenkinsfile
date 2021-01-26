pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        build(job: 'TP-CORE', propagate: true)
      }
    }

  }
  environment {
    JDK11 = 'jdk11'
    Maven = 'default'
    Branch = 'master'
  }
}