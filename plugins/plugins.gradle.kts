subprojects {
    apply(plugin = "java")
    apply(plugin = "application")
    apply(plugin = "com.github.johnrengelman.shadow")

    dependencies {
        implementation(rootProject)
    }

    afterEvaluate {
        val sourceJar = task<Jar>("sourceJar") {
            description = "Creates a JAR that contains the source code."
            from(project.the<SourceSetContainer>()["main"].allSource)
            archiveClassifier.set("sources")
        }
        val javadocJar = task<Jar>("javadocJar") {
            dependsOn("javadoc")
            description = "Creates a JAR that contains the javadocs."
            from(tasks.named("javadoc"))
            archiveClassifier.set("javadoc")
        }
    }
}
