#!/usr/bin/env groovy

pipeline {
    agent {
        label 'build'
    }

    options {
        disableConcurrentBuilds()
    }

    parameters {
        string(name: 'PROJECT_BRANCH', defaultValue: 'develop', description: '')
        booleanParam(name: 'BUILD', defaultValue: true, description: 'Build the .deb package')
        booleanParam(name: 'PUSH', defaultValue: true, description: 'Push the .deb package to the repo')
        booleanParam(name: 'DOCKER', defaultValue: true, description: 'Build and push Docker image to ECR')
        booleanParam(name: 'DEBUG', defaultValue: false, description: 'Enable extra output from Gradle (--info and --stacktrace) to diagnose faults')
    }

    environment {
        def serviceName = 'jtest-1'
        def serviceUser = 'jtest'
        def dockerRegistry = '564623767830.dkr.ecr.eu-west-1.amazonaws.com'
        def dockerImage = "python3-app"
        def dockerFile = 'Dockerfile'
        def gitRepo='https://github.com/kavitaranu/jtest.git'
        def debianCredentialsName = 'nexmo-ops.pem'
        def debianRepoUser = 'jenkinsdeploy'
        //def debianRepoHost = 'qaservice1.internal'
        //def debianRepoPath = '/var/debrepo/incoming'
        //def debianFileSuffixList = '.deb,.changes'
        def debianRepoHost = 'deb-src.dev.nexmo.vip'
        def debianRepoPath = '/storage/repo/incoming'
        def debianFileSuffixList = '.deb'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Git Checkout') {
            steps {
                script {
                    echo "Checkout branch: origin/${params.PROJECT_BRANCH}"
                    checkout $class: 'GitSCM', branches: [[name:"origin/${params.PROJECT_BRANCH}"]], userRemoteConfigs: [[url:gitRepo, credentialsId:'git']]

                    env.hash = sh(returnStdout: true, script: "git rev-parse HEAD").trim().substring(0,7)
                    epochtime = sh(returnStdout: true, script: "git show -s HEAD --format=%cd --date=raw").trim().split(' ')[0]
                    env.timestamp = sh(returnStdout: true, script: "date -d@${epochtime} +%Y%m%d.%H%M%S.000").trim()
                    env.branch = params.PROJECT_BRANCH.replaceAll(/[^A-Za-z0-9.+:~-]/,'-')
                    if (env.branch.startsWith('origin/')) {
                        env.branch = env.branch.substring(7)
                    }
                }
            }
        }

        stage('Build Docker Image') {
            when {
                anyOf {
                    branch 'main'
                    expression {return params.DOCKER}
                }
            }
            steps {
                script {
                    // Docker image name
                    def escapedBranchName = env.branch.replaceAll("_", "-")
                    dockerTag = escapedBranchName + "-" + env.hash
                    echo "Docker tag = ${dockerTag}"
                    if (dockerTag.contains("/")) {
                        echo "Invalid Docker tag - contains forward-slash"
                    }

                    // ECR login
                    sh 'sudo $(aws ecr get-login --no-include-email --region eu-west-1)'
                    //sh 'aws ecr get-login-password --region eu-west-1 | sudo docker login --username AWS --password-stdin 760258808129.dkr.ecr.eu-west-1.amazonaws.com'




                    //sh "sudo docker build -f ${env.dockerFile} --no-cache --network=host  -t ${env.dockerImage}:${dockerTag} ."
                    sh "sudo docker build -f ${env.dockerFile} --no-cache --network=host -t ${env.dockerRegistry}/${env.dockerImage}:${dockerTag} ."


                    // Push image
                    status = sh(returnStatus:true, script:"aws ecr describe-images --repository-name=${env.dockerImage} --image-ids=imageTag=${dockerTag} --region eu-west-1")
                    if (status == 0) {
                        echo "Image already present, replacing."
                    }
                    sh "sudo docker push ${env.dockerRegistry}/${env.dockerImage}:${dockerTag}"
                }
            }
        }

        stage('Clean') {
            steps {
                //sh "make docker-clean"
                //sh "./gradlew clean --no-daemon"
                echo "Not cleaning up workspace"
            }
        }
    }
}

