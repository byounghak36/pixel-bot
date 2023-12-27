plugins {
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

application.mainClass = "com.pixel.jda.bot"
group = "com.pixel"
version = "1.0"

val jdaVersion = "5.0.0-beta.18"

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
}

dependencies {
    implementation("net.dv8tion:JDA:$jdaVersion")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("org.jsoup:jsoup:1.15.3")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.3.0")
    implementation("org.mybatis:mybatis:3.5.14")
    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
}


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true

    // Set this to the version of java you want to use,
    // the minimum required for JDA is 1.8
    sourceCompatibility = "17"
}