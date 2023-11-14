pluginManagement {
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://maven.aliyun.com/repository/public/")
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
}

rootProject.name = "Utils"
include(":app")
include(":res")
include(":nav")
