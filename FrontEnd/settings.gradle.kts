pluginManagement {
    plugins{
        id("com.google.devtools.ksp") apply false
    }
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://repository.map.naver.com/archive/maven")
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven ("https://jitpack.io" )
    }
}

rootProject.name = "SANSANMULMUL"
include(":app")
