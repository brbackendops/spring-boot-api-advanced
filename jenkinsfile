pipeline {
    agent any

    environment {
        NAME = "john"
        LASTNAME = "doe"
        secret = credentials('TEST')
    }

    stages {
        stage("Build") {
            steps {
                sh "echo $NAME $LASTNAME"
                sh "echo jenkins -> credentials -> system -> Global credentials -> add credentials"
            }
        }

        stage("Test") {
            steps {

            }
        }

        stage("Deploy") {
            steps {

                timeout(time: 3 , unit: 'SECONDS') {
                    sh 'sleep 5'
                }
            }
        }

        stage("Timeout") {
            steps {
                retry(3) {
                    sh "Hy...!"
                }
            }
        }
    }

    post {
        always {
            echo "always going to be executed"
        }
        success {
            echo "will be executed if this is success"
        }
        failure {
            echo "will be executed only if this is a failure"
        }
        unstable {
            echo "only get executed if this is unstable"
        }
    }
}