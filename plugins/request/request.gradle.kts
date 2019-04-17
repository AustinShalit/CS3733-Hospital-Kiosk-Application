
repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(group = "com.jfoenix", name = "jfoenix", version = "8.0.8")

    fun googleHttp(name: String, version: String = "1.29.0") =
            create(group = "com.google.http-client", name = name, version = version)
    implementation(googleHttp(name = "google-http-client"))
    implementation(googleHttp(name = "google-http-client-jackson2"))

    fun junitJupiter(name: String, version: String = "5.4.1") =
            create(group = "org.junit.jupiter", name = name, version = version)
    testImplementation(junitJupiter(name = "junit-jupiter-api"))
    testImplementation(junitJupiter(name = "junit-jupiter-engine"))
    testImplementation(junitJupiter(name = "junit-jupiter-params"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
