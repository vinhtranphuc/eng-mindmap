pipeline {
    agent any
    environment {
        DOCKER_CONTAINER = "myanmarWorkerAdmin"
        DOCKER_IMAGE = "myanmar_worker-admin_service"
        DOCKER_TAG = "${DOCKER_IMAGE}:${BUILD_NUMBER}"
        DOCKER_NETWORK = "mw_java_main-network"
        DOCKER_PORT = "83:8080"
        DOCKER_FILE_DIR = "./admin/Dockerfile"
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "WORKSPACE = ${WORKSPACE}"
                    echo "DOCKER_TAG = ${DOCKER_TAG}"
                    echo "DOCKER_NETWORK = ${DOCKER_NETWORK}"
                    echo "DOCKER_PORT = ${DOCKER_PORT}"
                    echo "GIT_COMMIT = ${GIT_COMMIT}"
                '''
            }
        }
        stage ('Build Tag') {
            steps {
                script {
                    sh 'docker build -f ${DOCKER_FILE_DIR} . -t ${DOCKER_TAG}'
                 }
            }
            post {
                success {
                    sh '''
                        echo "SUCCESSED : build tag ${DOCKER_TAG}"
                    '''
                }
            }
        }
        stage ('Stop & Remove Container') {
            steps {
                script {
                    sh 'docker ps -q --filter "name=${DOCKER_CONTAINER}" | xargs -r docker stop'
                    sh 'docker ps -aq --filter "name=${DOCKER_CONTAINER}" | xargs -r docker rm -v'
                }
            }
            post {
                success {
                    sh '''
                        echo "SUCCESSED : stop & remove Docker Container ${DOCKER_CONTAINER}"
                    '''
                }
            }
        }
        stage ('Run Tag') {
            steps {
                script {
                    sh 'docker run -v -it -d -p ${DOCKER_PORT} --net ${DOCKER_NETWORK} --name ${DOCKER_CONTAINER} ${DOCKER_TAG}'
                }
            }
            post {
                success {
                    script {
                        sh '''
                            echo "SUCCESSED : build & run ${DOCKER_TAG}"
                        '''
                    }
                }
            }
        }
    }
}

