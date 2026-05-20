pipeline {
    agent any
    tools {
        maven 'Maven3.8.2'        // 名称需与全局工具配置中一致
        jdk 'JDK8'
    }
    environment {
        APP_NAME = 'easytools'
        JAR_NAME = 'easytools-1.0.0.jar'
    }
    stages {
        stage('Checkout') {
            steps {
                echo '====== 第一步：从Git拉取代码 ======'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo '====== 第二步：Maven编译打包 ======'
                script {
                    if (isUnix()) {
                        sh 'mvn clean package -DskipTests'
                    } else {
                        bat 'mvn clean package -DskipTests'
                }
        }
            }
        }
        stage('Deploy') {
            steps {
                echo '====== 第三步：部署到远程服务器 ======'
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'myCentos7',
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "target/${JAR_NAME}",
                                    removePrefix: 'target/',
                                    remoteDirectory: 'app',
                                    execCommand: '''
                                        APP_NAME="easytools"                        # 应用名称，按你的项目名修改
										JAR_NAME="easytools-1.0.0.jar"    # 构建出来的JAR文件名，按实际修改
										DEPLOY_DIR="/opt/app"                    # 应用部署目录
										BACKUP_DIR="/opt/app-backup"             # 备份目录
										LOG_DIR="/opt/app/logs"                  # 日志目录
										JVM_OPTS="-Xms512m -Xmx2g "  # JVM参数

										# 1. 创建目录
										mkdir -p $DEPLOY_DIR $BACKUP_DIR $LOG_DIR

										# 2. 备份旧版本（如果存在）
										if [ -f $DEPLOY_DIR/$JAR_NAME ]; then
										BACKUP_FILE="$BACKUP_DIR/${JAR_NAME%.*}_$(date +%Y%m%d_%H%M%S).jar.bak"
										cp $DEPLOY_DIR/$JAR_NAME $BACKUP_FILE
										echo "旧版本已备份到: $BACKUP_FILE"
										fi
                                    '''
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }
    post {
        success {
            echo '====== ✅ 部署成功！ ======'
        }
        failure {
            echo '====== ❌ 部署失败，请查看日志 ======'
        }
    }
}
