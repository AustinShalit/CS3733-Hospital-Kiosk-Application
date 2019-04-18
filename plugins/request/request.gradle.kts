
repositories {
    mavenCentral()
    jcenter()
}

application {
    mainClassName = "edu.wpi.cs3733.d19.teamO.request.Project"
}

dependencies {
    fun googleHttp(name: String, version: String = "1.29.0") =
            create(group = "com.google.http-client", name = name, version = version)
    implementation(googleHttp(name = "google-http-client"))
    implementation(googleHttp(name = "google-http-client-jackson2"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
